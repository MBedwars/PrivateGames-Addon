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
    public void onExecute(CommandSender sender, String[] args, String fullUsage) {
        final PrivateGameManager manager = Utility.getManager();
        final Player player = (Player) sender;
        final Arena arena = GameAPI.get().getArenaByPlayer(player);

        if (arena == null) {
            Message.buildByKey("Not_Ingame").send(sender);
            return;
        }

        if (!manager.isPrivateArena(arena)) {
            Message.buildByKey("PrivateGames_WarpButNotPrivate").send(sender);
            return;
        }

        PlayerDataAPI.get().getProperties(player, props -> {
            if (!manager.getPlayerPrivateMode(props, player)) {
                Message.buildByKey("PrivateGames_GameCreationInactive").send(sender);
                return;
            }

            manager.getParty(player, leader -> {
                if (!leader.isPresent()) {
                    Message.buildByKey("PrivateGames_WarpButNotParty").send(sender);
                    return;
                }
                if (!leader.get().isLeader()) {
                    Message.buildByKey("PrivateGames_WarpButNotPartyLeader").send(sender);
                    return;
                }

                final Collection<Member> members = leader.get().getParty().getMembers(false);
                boolean sentSent = false, hasPlaying = false;

                for (Member member : members) {
                    final RemotePlayer remote = RemoteAPI.get().getOnlinePlayer(member.getUniqueId());

                    if (remote == null)
                        continue;
                    if (remote.isLocal() && GameAPI.get().getArenaByPlayer(remote.asBukkit()) == arena) {
                        hasPlaying = true;
                        continue;
                    }

                    Message.buildByKey("PrivateGames_Warp")
                        .placeholder("name", member.getUsername())
                        .send(sender);

                    sentSent = true;
                    arena.asRemote().addPlayer(remote);
                }

                if (!sentSent) {
                    if (hasPlaying)
                        Message.buildByKey("PrivateGames_WarpAllMembersPlaying").send(sender);
                    else
                        Message.buildByKey("PrivateGames_WarpNoMembers").send(sender);
                }
            });
        });
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
