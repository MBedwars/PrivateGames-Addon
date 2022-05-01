package me.harsh.privategamesaddon.events;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.mineacademy.fo.menu.Menu;


public class InventoryListener implements Listener {


    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event){
        final Inventory inventory = event.getClickedInventory();
        final Arena arena = GameAPI.get().getArenaByPlayer((Player) event.getWhoClicked());
        if (arena == null) return;
        if (inventory != null && inventory.getType() != InventoryType.PLAYER){
            if (arena.getStatus() == ArenaStatus.LOBBY){
                final Menu menu = Menu.getMenu(((Player) event.getWhoClicked()).getPlayer());
                if (menu != null){
                    event.setCancelled(false);
                }
            }
        }

    }
}
