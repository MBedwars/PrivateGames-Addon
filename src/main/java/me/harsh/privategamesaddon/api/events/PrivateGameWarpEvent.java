package me.harsh.privategamesaddon.api.events;

import de.marcely.bedwars.api.arena.Arena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Set;
import java.util.UUID;

public class PrivateGameWarpEvent extends Event {
    private static final HandlerList list = new HandlerList();
    Set<UUID> partyPlayers;
    Arena arena;


    public PrivateGameWarpEvent(Set<UUID> partyPlayers, Arena arena){
        this.partyPlayers = partyPlayers;
        this.arena = arena;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public Arena getArena() {
        return arena;
    }

    public Set<UUID> getPartyPlayers() {
        return partyPlayers;
    }
}
