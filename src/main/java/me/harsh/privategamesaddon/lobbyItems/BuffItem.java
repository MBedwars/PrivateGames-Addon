package me.harsh.privategamesaddon.lobbyitems;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.game.lobby.LobbyItem;
import de.marcely.bedwars.api.game.lobby.LobbyItemHandler;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.menu.PrivateGameMenu;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class BuffItem extends LobbyItemHandler {

    private final LobbyItemCache cache;

    public BuffItem(Plugin plugin, LobbyItemCache cache) {
        super("private_games:leader_menu", plugin);

        this.cache = cache;
    }

    @Override
    public void handleUse(Player player, Arena arena, LobbyItem lobbyItem) {
        final PrivateGameManager manager = Utility.getManager();

        if (manager.isPrivateArena(arena)){
            ArenaBuff buffState = manager.getBuffState(arena);

            if (buffState == null)
                manager.setBuffState(arena, buffState = new ArenaBuff(arena));

            new PrivateGameMenu(buffState, player).open(player);
        }
    }

    @Override
    public boolean isVisible(Player player, Arena arena, LobbyItem lobbyItem) {
        return this.cache.isLeader(player, arena);
    }
}
