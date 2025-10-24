package me.harsh.privategamesaddon.api;

import de.marcely.bedwars.api.arena.Arena;

/**
 * Represents the state of buffs applied to a specific arena.
 * <p>
 *   Value changes during an active match may not be applied immediately.
 * </p>
 */
public interface ArenaBuffState {

  /**
   * Get the arena associated with this buff state.
   *
   * @return The arena
   */
  Arena getArena();

  /**
   * Check if this buff state is still valid for its arena.
   *
   * @return <code>true</code> if valid, <code>false</code> otherwise
   */
  boolean isValid();

  /**
   * Set the health players will have in the arena.
   * <p>
   *   Default value is 20.
   * </p>
   *
   * @param health The health value to set
   */
  void setHealth(int health);

  /**
   * Get the health players have in the arena.
   * <p>
   *   Default value is 20.
   * </p>
   *
   * @return The health value
   */
  int getHealth();

  /**
   * Set the respawn time for players in the arena.
   * <p>
   *   Default value is 5 seconds.
   * </p>
   *
   * @param respawnTime The respawn time in seconds
   */
  void setRespawnTime(int respawnTime);

  /**
   * Get the respawn time for players in the arena.
   * <p>
   *   Default value is 5 seconds.
   * </p>
   *
   * @return The respawn time in seconds
   */
  int getRespawnTime();

  /**
   * Set whether one-hit kill is enabled in the arena.
   * <p>
   *   Default value is <code>false</code>.
   * </p>
   *
   * @param oneHitKill <code>true</code> to enable, <code>false</code> to disable
   */
  void setOneHitKill(boolean oneHitKill);

  /**
   * Check if one-hit kill is enabled in the arena.
   * <p>
   *   Default value is <code>false</code>.
   * </p>
   *
   * @return <code>true</code> if enabled, <code>false</code> otherwise
   */
  boolean isOneHitKill();

  /**
   * Set whether only base spawners are active in the arena.
   * <p>
   *   Default value is <code>false</code>.
   * </p>
   *
   * @param baseSpawnersOnly <code>true</code> to enable, <code>false</code> to disable
   */
  void setBaseSpawnersOnly(boolean baseSpawnersOnly);

  /**
   * Check if only base spawners are active in the arena.
   * <p>
   *   Default value is <code>false</code>.
   * </p>
   *
   * @return <code>true</code> if enabled, <code>false</code> otherwise
   */
  boolean isBaseSpawnersOnly();

  /**
   * Set whether low gravity is enabled in the arena.
   * <p>
   *   Default value is <code>false</code>.
   * </p>
   *
   * @param lowGravity <code>true</code> to enable, <code>false</code> to disable
   */
  void setLowGravity(boolean lowGravity);

  /**
   * Check if low gravity is enabled in the arena.
   * <p>
   *   Default value is <code>false</code>.
   * </p>
   *
   * @return <code>true</code> if enabled, <code>false</code> otherwise
   */
  boolean isLowGravity();

  /**
   * Set the movement speed amplification level for players in the arena.
   * <p>
   *   Default value is -1 (no amplification).
   * </p>
   *
   * @param speedAmplification The speed amplification level to set (0 = level 1)
   */
  void setSpeedAmplification(int speedAmplification);

  /**
   * Get the movement speed amplification level for players in the arena.
   * <p>
   *   Default value is -1 (no amplification).
   * </p>
   *
   * @return The speed amplification level (0 = level 1)
   */
  int getSpeedAmplification();

  /**
   * Set whether teams will start with max upgrades in the arena.
   * <p>
   *   Default value is <code>false</code>.
   * </p>
   *
   * @param maxUpgrades <code>true</code> to enable, <code>false</code> to disable
   */
  void setMaxUpgrades(boolean maxUpgrades);

  /**
   * Check if teams will start with max upgrades in the arena.
   * <p>
   *   Default value is <code>false</code>.
   * </p>
   *
   * @return <code>true</code> if enabled, <code>false</code> otherwise
   */
  boolean isMaxUpgrades();

  /**
   * Set whether fall damage is enabled in the arena.
   * <p>
   *   Default value is <code>true</code>.
   * </p>
   *
   * @param fallDamageEnabled <code>true</code> to enable, <code>false</code> to disable
   */
  void setFallDamageEnabled(boolean fallDamageEnabled);

  /**
   * Check if fall damage is enabled in the arena.
   * <p>
   *   Default value is <code>true</code>.
   * </p>
   *
   * @return <code>true</code> if enabled, <code>false</code> otherwise
   */
  boolean isFallDamageEnabled();

  /**
   * Set whether crafting is allowed in the arena.
   * <p>
   *   Default value is <code>false</code>.
   * </p>
   *
   * @param isCraftingAllowed <code>true</code> to enable, <code>false</code> to disable
   */
  void setCraftingAllowed(boolean isCraftingAllowed);

  /**
   * Check if crafting is allowed in the arena.
   * <p>
   *   Default value is <code>false</code>.
   * </p>
   *
   * @return <code>true</code> if enabled, <code>false</code> otherwise
   */
  boolean isCraftingAllowed();

  /**
   * Set whether blocks are protected in restricted areas in the arena.
   * <p>
   *   Default value is <code>true</code>.
   * </p>
   *
   * @param blocksProtected <code>true</code> to enable, <code>false</code> to disable
   */
  void setBlocksProtected(boolean blocksProtected);

  /**
   * Check if blocks are protected in restricted areas in the arena.
   * <p>
   *   Default value is <code>true</code>.
   * </p>
   *
   * @return <code>true</code> if enabled, <code>false</code> otherwise
   */
  boolean isBlocksProtected();
}
