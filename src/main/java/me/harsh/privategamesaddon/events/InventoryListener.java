package me.harsh.privategamesaddon.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;


public class InventoryListener implements Listener {


    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event){
        final Inventory inventory = event.getClickedInventory();
        if (inventory != null && inventory.getType() != InventoryType.PLAYER){
            event.setCancelled(false);
        }

    }
}
