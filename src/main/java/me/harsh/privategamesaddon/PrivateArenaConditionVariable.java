package me.harsh.privategamesaddon;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.picker.condition.variable.ArenaConditionVariable;
import de.marcely.bedwars.api.arena.picker.condition.variable.ArenaConditionVariableValueNumber;
import de.marcely.bedwars.api.remote.RemoteArena;
import me.harsh.privategamesaddon.utils.Utility;

public class PrivateArenaConditionVariable extends ArenaConditionVariable<ArenaConditionVariableValueNumber> {
    public PrivateArenaConditionVariable() {
        super(PrivateGamesAddon.getInstance(), "is_private_arena", ArenaConditionVariableValueNumber.class);
    }

    public ArenaConditionVariableValueNumber getValue(Arena arena) {
        if (Utility.getManager().getPrivateArenas().contains(arena))
        return new ArenaConditionVariableValueNumber(1);
        else return new ArenaConditionVariableValueNumber(0);
    }

    public ArenaConditionVariableValueNumber getValue(RemoteArena arena) {
        return new ArenaConditionVariableValueNumber(0); // there is currently no easy api for that
    }

}
