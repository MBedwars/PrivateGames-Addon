package me.harsh.privategamesaddon.buffs;

import lombok.Getter;

@Getter
public class ArenaBuff {
    private int health = 20;
    private int respawnTime = 5;
    private boolean oneHitKill = false;
    private boolean lowGravity = false;
    private int speedModifier = 1;

    private boolean isFallDamageEnabled = true;
    private boolean isCraftingAllowed = false;
    private boolean isBlocksProtected = true;
    private double spawnRateMultiplier = 3;
    private float knockBackMultiper = 1;

    public void setHealth(int health) {
        this.health = health;
    }

    public void setLowGravity(boolean lowGravity) {
        this.lowGravity = lowGravity;
    }


    public void setSpawnRateMultiplier(double spawnRateMultiplier) {
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

    public void setKnockBackMultiper(float knockBackMultiper) {
        this.knockBackMultiper = knockBackMultiper;
    }

    public void setFallDamageEnabled(boolean fallDamageEnabled) {
        isFallDamageEnabled = fallDamageEnabled;
    }

    public void setCraftingAllowed(boolean craftingAllowed) {
        isCraftingAllowed = craftingAllowed;
    }
}
