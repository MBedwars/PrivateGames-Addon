package me.harsh.privategamesaddon;

import com.alessiodp.parties.api.interfaces.PartyPlayer;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.game.lobby.LobbyItem;
import de.marcely.bedwars.api.game.lobby.LobbyItemHandler;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.menu.PrivateGameMenu;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.entity.Player;



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
            Utility.getManager().arenaArenaBuffMap.put(arena, new ArenaBuff());
            menu.displayTo(player);
        }
    }

    @Override
    public boolean isVisible(Player player, Arena arena, LobbyItem lobbyItem) {
        final PartyPlayer partyPlayer = Utility.getPlayer(player);
        if (!Utility.getManager().privateArenas.contains(arena)) return false;
        if (partyPlayer.isInParty()) {
            final Player p = Utility.getPlayerByUuid(Utility.getManager().partyMembersMangingMap.get(arena).getLeader());
            if (p == null) return false;
            return p == player;
        }else return false;
    }
}
