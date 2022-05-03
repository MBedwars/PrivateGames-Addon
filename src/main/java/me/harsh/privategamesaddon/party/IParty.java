package me.harsh.privategamesaddon.party;

import de.marcely.bedwars.api.arena.Arena;
import org.bukkit.entity.Player;

public abstract class IParty{
    private final Arena arena;
    private final Player player;

    public IParty(Arena arena, Player player){
        this.arena = arena;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Arena getArena() {
        return arena;
    }


}