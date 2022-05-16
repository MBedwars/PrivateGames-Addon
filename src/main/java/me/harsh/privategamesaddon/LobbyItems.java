package me.harsh.privategamesaddon;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.game.lobby.LobbyItem;
import de.marcely.bedwars.api.game.lobby.LobbyItemHandler;
import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.menu.PrivateGameMenu;
import me.harsh.privategamesaddon.party.PafParty;
import me.harsh.privategamesaddon.party.PartiesIParty;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;


public class LobbyItems extends LobbyItemHandler {
    public LobbyItems() {
        super("private", PrivateGamesAddon.getInstance());
    }

    @Override
    public void handleUse(Player player, Arena arena, LobbyItem lobbyItem) {
        if (Utility.getManager().privateArenas.contains(arena)){
            final PrivateGameMenu menu = new PrivateGameMenu();
            if (Utility.getManager().arenaArenaBuffMap.contains(arena)){
                menu.displayTo(player);
            }
            if (!Utility.getManager().arenaArenaBuffMap.contains(arena)){
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
        }
        return false;
    }
}
