package me.harsh.privategamesaddon.events;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;


public class InventoryListener implements Listener {


    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event){
        final ItemStack item = event.getCurrentItem();
        if (!(event.getWhoClicked() instanceof  Player)) return;
        final Player player = ((Player) event.getWhoClicked()).getPlayer();
        final Arena arena = GameAPI.get().getArenaByPlayer(player);
        if (arena != null){
            if (arena.getStatus() == ArenaStatus.LOBBY){
                GameAPI.get().getLobbyItems().forEach(lobbyItem -> {
                    if (lobbyItem.getItem() == item){
                        Common.log("Blocking movement of lobby item");
                        event.setCancelled(true);
                    }else {
                        event.setCancelled(false);
                    }
                });
            }
        }
    }
}
