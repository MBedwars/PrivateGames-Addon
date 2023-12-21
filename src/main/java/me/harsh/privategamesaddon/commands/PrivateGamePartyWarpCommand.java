package me.harsh.privategamesaddon.commands;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.hook.PartiesHook.Member;
import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.api.player.PlayerDataAPI;
import de.marcely.bedwars.api.remote.RemoteAPI;
import de.marcely.bedwars.api.remote.RemotePlayer;
import java.util.Collection;
import java.util.Collections;
import me.harsh.privategamesaddon.PrivateGamesPlugin;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


public class PrivateGamePartyWarpCommand extends Command.Executor {

    public PrivateGamePartyWarpCommand(PrivateGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        final PrivateGameManager manager = Utility.getManager();
        final Player player = (Player) sender;
        final Arena arena = GameAPI.get().getArenaByPlayer(player);

        if (arena == null) {
            Message.build(Settings.NOT_IN_ARENA).send(sender);
            return;
        }

        if (!manager.isPrivateArena(arena)) {
            Message.build(Settings.NOT_PRIVATE_ROOM_WARP).send(sender);
            return;
        }

        PlayerDataAPI.get().getProperties(player, props -> {
            if (!manager.getPlayerPrivateMode(props)) {
                Message.build(Settings.NOT_IN_PRIVATE_GAME_MODE).send(sender);
                return;
            }

            manager.getParty(player, leader -> {
                if (!leader.isPresent() || !leader.get().isLeader()) {
                    Message.build(Settings.NOT_IN_PARTY).send(sender);
                    return;
                }

                final Collection<Member> members = leader.get().getParty().getMembers(false);
                boolean sent = false;

                for (Member member : members) {
                    final RemotePlayer remote = RemoteAPI.get().getOnlinePlayer(member.getUniqueId());

                    if (remote == null)
                        continue;

                    Message.build("&aWarping " + member.getUsername()).send(sender);

                    sent = true;
                    arena.asRemote().addPlayer(remote);
                }

                if (!sent)
                    Message.build(Settings.ONLY_LEADER_IN_PARTY).send(sender);
            });
        });
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
