package me.harsh.privategamesaddon.commands;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.api.party.PartyManager;
import de.simonsator.partyandfriends.api.party.PlayerParty;
import me.harsh.privategamesaddon.api.events.PrivateGameWarpEvent;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

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
        final Player p2 = getPlayer();
        if (!Utility.hasPermision(p2)){
            Common.tell(p2,  Settings.NO_PERM_EROR);
            return;
        }
        final Arena arena = GameAPI.get().getArenaByPlayer(p2);
        if (arena == null) {
            Common.tell(p2,  " " + Settings.NOT_IN_ARENA);
            return;

        }
        if (!Utility.getManager().getPlayerPrivateMode(p2)){
            Common.tell(p2,  " " + Settings.NOT_IN_PRIVATE_GAME_MODE);
            return;
        }
        if (!Utility.getManager().privateArenas.contains(arena)){
            Common.tell(p2,  " " + Settings.NOT_PRIVATE_ROOM_WARP);
            return;
        }
        if (Utility.isParty){
            final PartyPlayer partyPlayer = Parties.getApi().getPartyPlayer(p2.getUniqueId());
            if (partyPlayer.isInParty()){
                final Party party = Parties.getApi().getParty(partyPlayer.getPartyId());
                if (party.getMembers().size() == 1){
                    tell( " " + Settings.ONLY_LEADER_IN_PARTY);
                    return;
                }
                if (party == null) Common.tell(p2,  "&c Party not found!");
                party.getMembers().forEach(uuid -> {
                    final Player p = Utility.getPlayerByUuid(uuid);
                    if (p == null) Common.log("Player is Null!");
                    if (p == p2) return;
                    Common.tell(p2,  "&aWarping " + p.getName());
                    arena.addPlayer(p);
                });
                Bukkit.getServer().getPluginManager().callEvent(new PrivateGameWarpEvent(party.getMembers(), arena));
            }else {
                Common.tell(p2, " " + Settings.NOT_IN_PARTY);
            }
        }else if (Utility.isPfa){
            final OnlinePAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(p2);
            if (pafPlayer.getParty() != null){
                final PlayerParty party = PartyManager.getInstance().getParty(pafPlayer);
                if (party.getPlayers().size() == 0){
                    tell( " " + Settings.ONLY_LEADER_IN_PARTY);
                    return;
                }
                party.getPlayers().forEach(player -> {
                    Common.tell(p2,  "&aWarping " + player.getName());
                    arena.addPlayer(player.getPlayer());
                });
            }

        }else if (Utility.isBedwarParty){
            final me.harsh.bedwarsparties.party.PartyManager manager = me.harsh.bedwarsparties.Utils.Utility.getManager();
            if (manager.isInPartyAsLeader(p2.getUniqueId())){
                final me.harsh.bedwarsparties.party.Party party = manager.getPartyByLeader(p2.getUniqueId());
                if (party.getPlayers().size() == 1){
                    tell( " " + Settings.ONLY_LEADER_IN_PARTY);
                    return;
                }
                if (party == null) Common.tell(p2,  "&c Party not found!");
                party.getPlayers().forEach(uuid -> {
                    final Player p = Utility.getPlayerByUuid(uuid);
                    if (p == null) Common.log("Player is Null!");
                    if (p == p2) return;
                    Common.tell(p2,  "&aWarping " + p.getName());
                    arena.addPlayer(p);
                });
                Bukkit.getServer().getPluginManager().callEvent(new PrivateGameWarpEvent((Set<UUID>) party.getPlayers(), arena));
            }else {
                Common.tell(p2, " " + Settings.NOT_IN_PARTY);
            }
        }

    }
}
