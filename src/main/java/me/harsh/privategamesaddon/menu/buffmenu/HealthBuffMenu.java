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

public class HealthBuffMenu extends ChestGUI {

  private final PrivateGameMenu parentMenu;

  public HealthBuffMenu(PrivateGameMenu parentMenu, @Nullable CommandSender sender) {
    super(3, Message.buildByKey("PrivateGames_ModifyBuffHealth_MenuTitle").done(sender));
    this.parentMenu = parentMenu;

    addCloseListener(player -> parentMenu.open(player));
  }

  @Override
  public void open(Player player) {
    draw(player);

    super.open(player);
  }

  public void draw(Player player) {
    clear();

    final AddItemCondition condition = AddItemCondition.withinY(1, 1);

    addItem(getItem(player, 5), condition);

    for (int i = 10; i <= 40; i += 10)
      addItem(getItem(player, i), condition);

    formatRow(1, CenterFormat.CENTRALIZED_EVEN);
  }

  private GUIItem getItem(Player player, int hearts) {
    final String name = Message.buildByKey("PrivateGames_ModifyBuffHealth_Name")
        .placeholder("amount", Helper.get().formatNumber(hearts))
        .done(player);
    final int health = hearts * 2;
    final ArenaBuff buffState = this.parentMenu.getBuffState();
    ItemStack is = NMSHelper.get().hideAttributes(Helper.get().parseItemStack("GOLDEN_APPLE"));
    final ItemMeta im = is.getItemMeta();
    final boolean active = health == buffState.getHealth();

    im.setDisplayName((active ? ChatColor.GREEN : ChatColor.GOLD) + name);
    is.setItemMeta(im);

    if (active)
      is = NMSHelper.get().setGlowEffect(is, true);

    return new GUIItem(is, (g0, g1, g2) -> {
      buffState.setHealth(health);

      Message.buildByKey("PrivateGames_ModifyBuffHealth")
          .placeholder("hearts", Helper.get().formatNumber(hearts))
          .send(player);
      draw(player);
    });
  }
}
