package me.harsh.privategamesaddon.api.events;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.event.arena.ArenaEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PrivateGameCreateEvent extends Event implements ArenaEvent {

    private static final HandlerList list = new HandlerList();
    Player player;
    Arena arena;

    public PrivateGameCreateEvent(Player player, Arena arena){
        this.player = player;
        this.arena = arena;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public Arena getArena() {
        return arena;
    }

    public Player getPlayer() {
        return player;
    }
}
