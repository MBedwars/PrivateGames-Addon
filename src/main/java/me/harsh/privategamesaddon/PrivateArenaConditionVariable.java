package me.harsh.privategamesaddon;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.picker.condition.variable.ArenaConditionVariable;
import de.marcely.bedwars.api.arena.picker.condition.variable.ArenaConditionVariableValueNumber;
import de.marcely.bedwars.api.remote.RemoteArena;
import me.harsh.privategamesaddon.utils.Utility;

public class PrivateArenaConditionVariable extends ArenaConditionVariable<ArenaConditionVariableValueNumber> {
    public PrivateArenaConditionVariable() {
        super(PrivateGamesPlugin.getInstance(), "is_private_arena", ArenaConditionVariableValueNumber.class);
    }

    public ArenaConditionVariableValueNumber getValue(Arena arena) {
        return getValue(arena.asRemote());
    }

    public ArenaConditionVariableValueNumber getValue(RemoteArena arena) {
        if (Utility.getManager().isPrivateArena(arena))
            return new ArenaConditionVariableValueNumber(1);
        else
            return new ArenaConditionVariableValueNumber(0);
    }
}
