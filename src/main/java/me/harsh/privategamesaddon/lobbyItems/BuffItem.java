package me.harsh.privategamesaddon.lobbyItems;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.game.lobby.LobbyItem;
import de.marcely.bedwars.api.game.lobby.LobbyItemHandler;
import me.harsh.privategamesaddon.PrivateGamesAddon;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.menu.PrivateGameMenu;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicReference;


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
        if (!Utility.getManager().privateArenas.contains(arena))
            return false;

        final AtomicReference<Boolean> ref = new AtomicReference<>(null);

        Utility.getManager().getParty(player, member -> {
            // method hasn't returned yet, no manual labor
            if (ref.getAndSet(member.isPresent()) == null)
                return;

            // he is not a leader
            if (!member.isPresent() || !member.get().isLeader())
                return;
            // he is not a part of the arena anymore
            if (GameAPI.get().getArenaByPlayer(player) != arena || arena.getStatus() != ArenaStatus.LOBBY)
                return;

            // callback got returned later. lets make it visible ourselves
            System.out.println("Player is leader. " +player.getName());
            player.getInventory().setItem(lobbyItem.getSlot(), lobbyItem.getItem());
        });

        return ref.getAndSet(false);
    }
}
