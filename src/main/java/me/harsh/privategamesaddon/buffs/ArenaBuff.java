package me.harsh.privategamesaddon.buffs;

import de.marcely.bedwars.api.arena.Arena;
import lombok.Data;
import me.harsh.privategamesaddon.api.ArenaBuffState;
import me.harsh.privategamesaddon.utils.Utility;

@Data
public class ArenaBuff implements ArenaBuffState {

    private final Arena arena;

    private int health = 20;
    private int respawnTime = 5;
    private boolean oneHitKill = false;
    private boolean baseSpawnersOnly = false;
    private boolean lowGravity = false;
    private int speedAmplification = -1; // amplification; 0 = lvl 1

    private boolean maxUpgrades = false;
    private boolean fallDamageEnabled = true;
    private boolean isCraftingAllowed = false;
    private boolean blocksProtected = true;
    private double spawnRateMultiplier = 1;

    @Override
    public boolean isValid() {
        return Utility.getManager().getBuffState(this.arena) == this;
    }
}
