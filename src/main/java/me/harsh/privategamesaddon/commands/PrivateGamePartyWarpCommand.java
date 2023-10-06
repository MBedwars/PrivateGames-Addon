package me.harsh.privategamesaddon.commands;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.hook.PartiesHook.Member;
import de.marcely.bedwars.api.player.PlayerDataAPI;
import de.marcely.bedwars.api.remote.RemoteAPI;
import de.marcely.bedwars.api.remote.RemotePlayer;
import java.util.Collection;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class PrivateGamePartyWarpCommand extends SimpleSubCommand {

    protected PrivateGamePartyWarpCommand(SimpleCommandGroup parent) {
        super(parent, "warp");
        setPermission(null);
    }

    @Override
    protected void onCommand() {
        checkConsole();

        final PrivateGameManager manager = Utility.manager;
        final Player player = getPlayer();

        if (!Utility.hasPermision(player)) {
            Common.tell(player, Settings.NO_PERM_EROR);
            return;
        }

        final Arena arena = GameAPI.get().getArenaByPlayer(player);

        if (arena == null) {
            Common.tell(player, " " + Settings.NOT_IN_ARENA);
            return;
        }

        if (!manager.privateArenas.contains(arena)) {
            Common.tell(player, " " + Settings.NOT_PRIVATE_ROOM_WARP);
            return;
        }

        PlayerDataAPI.get().getProperties(player, props -> {
            if (!manager.getPlayerPrivateMode(props)) {
                Common.tell(player, " " + Settings.NOT_IN_PRIVATE_GAME_MODE);
                return;
            }

            manager.getParty(player, leader -> {
                if (!leader.isPresent() || !leader.get().isLeader()) {
                    Common.tell(player, " " + Settings.NOT_IN_PARTY);
                    return;
                }

                final Collection<Member> members = leader.get().getParty().getMembers(false);
                boolean sent = false;

                for (Member member : members) {
                    final RemotePlayer remote = RemoteAPI.get().getOnlinePlayer(member.getUniqueId());

                    if (remote == null)
                        continue;

                    Common.tell(player, "&aWarping " + member.getUsername());

                    sent = true;
                    arena.asRemote().addPlayer(remote);
                }

                if (!sent)
                    tell(" " + Settings.ONLY_LEADER_IN_PARTY);
            });
        });
    }

    private Set<UUID> convertListToSet(List<UUID> list){
        return new HashSet<>(list);
    }
}
