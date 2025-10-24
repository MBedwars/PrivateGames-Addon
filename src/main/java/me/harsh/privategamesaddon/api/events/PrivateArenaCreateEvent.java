package me.harsh.privategamesaddon.api.events;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.event.arena.ArenaEvent;
import de.marcely.bedwars.api.hook.PartiesHook.Party;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Gets called when a party leader is about to turn an arena private.
 * <p>
 *   More specifically, the arena was non-private before, and is now being set to private for
 *   the party of the leader.
 * </p>
 */
public class PrivateArenaCreateEvent extends Event implements ArenaEvent, Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Arena arena;
    private final Party party;
    private final Player partyLeader;

    @Getter @Setter
    private boolean cancelled = false;

    public PrivateArenaCreateEvent(Arena arena, Party party, Player partyLeader) {
        this.arena = arena;
        this.party = party;
        this.partyLeader = partyLeader;
    }

    /**
     * Get the arena that is about to be set to private.
     *
     * @return The involved arena
     */
    @Override
    public Arena getArena() {
        return this.arena;
    }

    /**
     * Get the party for which the arena is being set to private.
     *
     * @return The involved party
     */
    public Party getParty() {
        return this.party;
    }

    /**
     * Get the player (party leader) who is setting the arena to private.
     *
     * @return The causing party leader
     */
    public Player getPartyLeader() {
        return this.partyLeader;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
