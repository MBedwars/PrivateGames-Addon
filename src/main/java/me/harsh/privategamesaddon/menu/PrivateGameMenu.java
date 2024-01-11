package me.harsh.privategamesaddon.menu;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.tools.Helper;
import de.marcely.bedwars.tools.NMSHelper;
import de.marcely.bedwars.tools.gui.AddItemCondition;
import de.marcely.bedwars.tools.gui.CenterFormat;
import de.marcely.bedwars.tools.gui.GUIItem;
import de.marcely.bedwars.tools.gui.type.ChestGUI;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.Getter;
import me.harsh.privategamesaddon.PrivateGamesPlugin;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.menu.buffmenu.HealthBuffMenu;
import me.harsh.privategamesaddon.menu.buffmenu.RespawnBuffMenu;
import me.harsh.privategamesaddon.menu.buffmenu.SpawnRateBuffMenu;
import me.harsh.privategamesaddon.menu.buffmenu.SpeedBuffMenu;
import me.harsh.privategamesaddon.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

public class PrivateGameMenu extends ChestGUI {

  @Getter
  private final ArenaBuff buffState;

  public PrivateGameMenu(ArenaBuff buffState, @Nullable CommandSender sender) {
    super(5, Message.buildByKey("PrivateGames_ModifyBuff_MenuTitle").done(sender));

    this.buffState = buffState;
  }

  @Override
  public void open(Player player) {
    draw(player);

    Bukkit.getScheduler().runTaskLater(PrivateGamesPlugin.getInstance(), () -> {
      final Arena arena = GameAPI.get().getArenaByPlayer(player);

      if (arena == null || arena.getStatus() != ArenaStatus.LOBBY)
        return;

      super.open(player);
    }, 1);
  }

  private void draw(Player player) {
    clear();

    addItem(getNoSpecialSpawnersItem(player), AddItemCondition.withinY(1, 1));
    addItem(getMaxUpgradeItem(player), AddItemCondition.withinY(1, 1));
    addItem(getFallDamageItem(player), AddItemCondition.withinY(1, 1));
    addItem(getRespawnTimeItem(player), AddItemCondition.withinY(1, 1));
    addItem(getBlockProtItem(player), AddItemCondition.withinY(1, 1));
    addItem(getSpawnRateMultiplierItem(player), AddItemCondition.withinY(3, 3));
    addItem(getSpeedItem(player), AddItemCondition.withinY(3, 3));
    addItem(getGravityItem(player), AddItemCondition.withinY(3, 3));
    addItem(getOneHitItem(player), AddItemCondition.withinY(3, 3));
    addItem(getHealthItem(player), AddItemCondition.withinY(3, 3));

    formatRow(1, CenterFormat.ALIGNED);
    formatRow(3, CenterFormat.ALIGNED);
  }

  private GUIItem createItem(Player player, String materialName, String permission, Boolean isActive, Runnable onUse, Message name, String... lore) {
    ItemStack is = NMSHelper.get().hideAttributes(Helper.get().parseItemStack(materialName));
    ChatColor color = ChatColor.YELLOW;

    if (isActive != null) {
      if (isActive) {
        color = ChatColor.GREEN;
        is = NMSHelper.get().setGlowEffect(is, true);
      } else
        color = ChatColor.RED;
    }

    final ItemMeta im = is.getItemMeta();

    im.setDisplayName(color + ChatColor.stripColor(name.done(player)));
    im.setLore(Arrays.stream(lore)
        .map(l -> ChatColor.GRAY + Message.build(l).done(player))
        .collect(Collectors.toList()));
    is.setItemMeta(im);

    return new GUIItem(is, (g0, g1, g2) -> {
      if (!player.hasPermission(permission)) {
        Message.buildByKey("No_Permissions").send(player);
        return;
      }

      onUse.run();
    });
  }

  private GUIItem createToggleItem(Player player, String materialName, String permission, boolean isActive, Consumer<Boolean> toggle, Message name, String... lore) {
    return createItem(
        player,
        materialName,
        permission,
        isActive,
        () -> {
          final boolean newState = !isActive;

          toggle.accept(newState);

          Message.buildByKey(newState ? "PrivateGames_ModifyBuff_ToggleOn" : "PrivateGames_ModifyBuff_ToggleOff")
              .placeholder("buff", name)
              .send(player);

          draw(player);
        },
        name,
        lore
    );
  }

  private GUIItem getNoSpecialSpawnersItem(Player player) {
    return createToggleItem(
        player,
        "EMERALD",
        Settings.NO_SPECIAL_SPAWNER_BUFF_PERM,
        this.buffState.isNoEmeralds(),
        newState -> this.buffState.setNoEmeralds(newState),
        Message.buildByKey("PrivateGames_BuffNoSpecialSpawners"),
        "",
        "Enabled you to disable",
        "All the special spawners like",
        "Emeralds and diamonds!"
    );
  }

  private GUIItem getMaxUpgradeItem(Player player) {
    return createToggleItem(
        player,
        "DIAMOND_CHESTPLATE",
        Settings.MAX_UPGRADE_BUFF_PERM,
        this.buffState.isMaxUpgrades(),
        newState -> this.buffState.setMaxUpgrades(newState),
        Message.buildByKey("PrivateGames_BuffMaxUpgrades"),
        "",
        "Enables you to to give",
        "max upgrade buffs at start"
    );
  }

  private GUIItem getFallDamageItem(Player player) {
    return createToggleItem(
        player,
        "ENDER_PEARL",
        Settings.NO_FALL_DAMAGE_BUFF_PERM,
        this.buffState.isFallDamageEnabled(),
        newState -> this.buffState.setFallDamageEnabled(newState),
        Message.buildByKey("PrivateGames_BuffFallDamage"),
        "",
        "Enables you to disable",
        "or enable fall damage"
    );
  }

  private GUIItem getRespawnTimeItem(Player player) {
    return createItem(
        player,
        "PAPER",
        Settings.RESPAWN_BUFF_PERM,
        null,
        () -> new RespawnBuffMenu(this, player).open(player),
        Message.buildByKey("PrivateGames_BuffRespawnTime"),
        "",
        "Enables you to ",
        "Change respawn time"
    );
  }

  private GUIItem getBlockProtItem(Player player) {
    return createToggleItem(
        player,
        "GRASS_BLOCK",
        Settings.BLOCK_PROT_BUFF_PERM,
        this.buffState.isBlocksProtected(),
        newState -> this.buffState.setBlocksProtected(newState),
        Message.buildByKey("PrivateGames_BuffDisabledBlockProtection"),
        "",
        "Enables you to stop the",
        "Block protection in arenas"
    );
  }

  private GUIItem getSpawnRateMultiplierItem(Player player) {
    return createItem(
        player,
        "IRON_INGOT",
        Settings.SPAWN_RATE_MUTLIPLER_BUFF_PERM,
        null,
        () -> new SpawnRateBuffMenu(this, player).open(player),
        Message.buildByKey("PrivateGames_BuffSpawnRate"),
        "",
        "Enables people multiply",
        "spawn rate of base spawners!"
    );
  }

  private GUIItem getSpeedItem(Player player) {
    return createItem(
        player,
        "SUGAR",
        Settings.SPEED_BUFF_PERM,
        null,
        () -> new SpeedBuffMenu(this, player).open(player),
        Message.buildByKey("PrivateGames_BuffSpeed"),
        "",
        "Enables you to play",
        "in more speed than normal"
    );
  }

  private GUIItem getGravityItem(Player player) {
    return createToggleItem(
        player,
        "RABBIT_FOOT",
        Settings.GRAVITY_BUFF_PERM,
        this.buffState.isLowGravity(),
        newState -> this.buffState.setLowGravity(newState),
        Message.buildByKey("PrivateGames_BuffLowGravity"),
        "",
        "Enables you to play in",
        "low gravity in bedwars"
    );
  }

  private GUIItem getOneHitItem(Player player) {
    return createToggleItem(
        player,
        "DIAMOND_SWORD",
        Settings.ONE_HIT_BUFF_PERM,
        this.buffState.isOneHitKill(),
        newState -> this.buffState.setOneHitKill(newState),
        Message.buildByKey("PrivateGames_BuffOneHit"),
        "",
        "Enables to one hit anyone",
        "in the bedwars game"
    );
  }

  private GUIItem getHealthItem(Player player) {
    return createItem(
        player,
        "GOLDEN_APPLE",
        Settings.CUSTOM_HEALTH_BUFF_PERM,
        null,
        () -> new HealthBuffMenu(this, player).open(player),
        Message.buildByKey("PrivateGames_BuffHealth"),
        "",
        "Enables you to",
        "increase custom health"
    );
  }
}
