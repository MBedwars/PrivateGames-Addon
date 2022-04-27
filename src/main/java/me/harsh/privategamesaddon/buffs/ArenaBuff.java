package me.harsh.privategamesaddon.buffs;

import lombok.Getter;

@Getter
public class ArenaBuff {
    private int health = 20;
    private int respawnTime = 5;
    private boolean oneHitKill = false;
    private boolean lowGravity = false;
    private int speedModifier = 1;
    private boolean isBlocksProtected = true;
    private boolean isBedInstaBreakEnabled = false;
    private  int spawnRateMultiplier = 3;

    public void setHealth(int health) {
        this.health = health;
    }

    public void setLowGravity(boolean lowGravity) {
        this.lowGravity = lowGravity;
    }


    public void setSpawnRateMultiplier(int spawnRateMultiplier) {
        this.spawnRateMultiplier = spawnRateMultiplier;
    }

    public void setOneHitKill(boolean oneHitKill) {
        this.oneHitKill = oneHitKill;
    }

    public void setRespawnTime(int respawnTime) {
        this.respawnTime = respawnTime;
    }

    public void setSpeedModifier(int speedModifier) {
        this.speedModifier = speedModifier;
    }

    public void setBlocksProtection(boolean blocksProtected) {
        isBlocksProtected = blocksProtected;
    }

    public void setBedInstaBreakEnabled(boolean bedInstaBreakEnabled) {
        isBedInstaBreakEnabled = bedInstaBreakEnabled;
    }
}
