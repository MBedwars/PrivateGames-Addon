package me.harsh.privategamesaddon.lobbyItems;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.game.lobby.LobbyItem;
import de.marcely.bedwars.api.game.lobby.LobbyItemHandler;
import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import me.harsh.bedwarsparties.party.Party;
import me.harsh.bedwarsparties.party.PartyManager;
import me.harsh.privategamesaddon.PrivateGamesAddon;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.menu.PrivateGameMenu;
import me.harsh.privategamesaddon.party.BwParty;
import me.harsh.privategamesaddon.party.PafParty;
import me.harsh.privategamesaddon.party.PartiesIParty;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.entity.Player;

import java.util.UUID;


public class BuffItem extends LobbyItemHandler {
    public BuffItem() {
        super("private", PrivateGamesAddon.getInstance());
    }

    @Override
    public void handleUse(Player player, Arena arena, LobbyItem lobbyItem) {
        if (Utility.getManager().privateArenas.contains(arena)){
            final PrivateGameMenu menu = new PrivateGameMenu();
            if (Utility.getManager().arenaArenaBuffMap.containsKey(arena)){
                menu.displayTo(player);
            }
            if (!Utility.getManager().arenaArenaBuffMap.containsKey(arena)){
                Utility.getManager().arenaArenaBuffMap.put(arena, new ArenaBuff());
            }
        }
    }

    @Override
    public boolean isVisible(Player player, Arena arena, LobbyItem lobbyItem) {
        if (!Utility.getManager().privateArenas.contains(arena)) return false;
        if (Utility.isParty){
            final PartyPlayer partyPlayer = Parties.getApi().getPartyPlayer(player.getUniqueId());
            if (partyPlayer.isInParty()) {
                final PartiesIParty party = (PartiesIParty) Utility.getManager().partyMembersMangingMap.get(arena);
                final Player p = Utility.getPlayerByUuid(party.getLeader());
                if (p == null) return false;
                return p == player;
            }else return false;
        } else if (Utility.isPfa) {
            final OnlinePAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(player);
            if (pafPlayer.getParty() == null) return false;
            final PafParty party = (PafParty) Utility.getManager().partyMembersMangingMap.get(arena);
            final Player p = Utility.getPlayerByUuid(party.getLeader().getUniqueId());
            if (p == null) return false;
            return p == player;
        } else if (Utility.isBedwarParty){
            final PartyManager manager = me.harsh.bedwarsparties.Utils.Utility.getManager();
            if (!manager.isInPartyAsLeader(player.getUniqueId())) return false;
            final Party party = manager.getPartyByLeader(player.getUniqueId());
            final BwParty bwParty = (BwParty) Utility.getManager().partyMembersMangingMap.get(arena);
            final UUID leader = bwParty.getParty().getLeader();
            if (leader == null) return false;
            return leader.toString().equalsIgnoreCase(party.getLeader().toString());
        }
        return false;
    }
}
