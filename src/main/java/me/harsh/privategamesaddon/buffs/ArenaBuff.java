package me.harsh.privategamesaddon.buffs;

import lombok.Data;

@Data
public class ArenaBuff {

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
}
