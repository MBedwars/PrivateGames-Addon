package me.harsh.privategamesaddon.buffs;

import lombok.Data;

@Data
public class ArenaBuff {

    private int health = 20;
    private int respawnTime = 5;
    private boolean oneHitKill = false;
    private boolean noEmeralds = false;
    private boolean lowGravity = false;
    private int speedModifier = 1;

    private boolean maxUpgrades = false;
    private boolean fallDamageEnabled = true;
    private boolean isCraftingAllowed = false;
    private boolean blocksProtected = true;
    private double spawnRateMultiplier = 3;
}
