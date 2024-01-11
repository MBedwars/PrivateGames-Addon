package me.harsh.privategamesaddon.menu.buffmenu;

import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.tools.Helper;
import de.marcely.bedwars.tools.NMSHelper;
import de.marcely.bedwars.tools.gui.AddItemCondition;
import de.marcely.bedwars.tools.gui.CenterFormat;
import de.marcely.bedwars.tools.gui.GUIItem;
import de.marcely.bedwars.tools.gui.type.ChestGUI;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.menu.PrivateGameMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;


public class SpawnRateBuffMenu extends ChestGUI {

  private final PrivateGameMenu parentMenu;

  public SpawnRateBuffMenu(PrivateGameMenu parentMenu, @Nullable CommandSender sender) {
    super(3, Message.buildByKey("PrivateGames_ModifyBuffSpawnRate_MenuTitle").done(sender));
    this.parentMenu = parentMenu;

    addCloseListener(player -> parentMenu.open(player));
  }

  @Override
  public void open(Player player) {
    draw(player);

    super.open(player);
  }

  private void draw(Player player) {
    clear();

    final AddItemCondition condition = AddItemCondition.withinY(1, 1);

    addItem(getItem(player, 0.5), condition);
    addItem(getItem(player, 0.75), condition);
    addItem(getItem(player, 1), condition);
    addItem(getItem(player, 1.5), condition);
    addItem(getItem(player, 2), condition);

    formatRow(1, CenterFormat.CENTRALIZED_EVEN);
  }

  private GUIItem getItem(Player player, double val) {
    final ArenaBuff buffState = this.parentMenu.getBuffState();
    ItemStack is = Helper.get().parseItemStack("GOLD_INGOT");
    final ItemMeta im = is.getItemMeta();
    final boolean active = val == buffState.getSpawnRateMultiplier();

    im.setDisplayName((active ? ChatColor.GREEN : ChatColor.GOLD) + Helper.get().formatNumber(val) + "x");
    is.setItemMeta(im);

    if (active)
      is = NMSHelper.get().setGlowEffect(is, true);

    return new GUIItem(is, (g0, g1, g2) -> {
      buffState.setSpawnRateMultiplier(val);

      Message.buildByKey("PrivateGames_ModifyBuffSpawnRate")
          .placeholder("amount", Helper.get().formatNumber(val))
          .send(player);
      draw(player);
    });
  }
}
