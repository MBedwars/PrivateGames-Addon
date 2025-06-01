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

public class SpeedBuffMenu extends ChestGUI {


  private final PrivateGameMenu parentMenu;

  public SpeedBuffMenu(PrivateGameMenu parentMenu, @Nullable CommandSender sender) {
    super(3, Message.buildByKey("PrivateGames_ModifyBuffSpeed_MenuTitle").done(sender));
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

    addItem(getItem(player, null, -1), condition);
    addItem(getItem(player, "I", 0), condition);
    addItem(getItem(player, "II", 1), condition);
    addItem(getItem(player, "III", 2), condition);
    addItem(getItem(player, "VI", 3), condition);

    formatRow(1, CenterFormat.CENTRALIZED_EVEN);
  }

  private GUIItem getItem(Player player, @Nullable String tier, int val) {
    final String name = Message.buildByKey(tier != null ? "PrivateGames_ModifyBuffSpeed_NameTier" : "PrivateGames_ModifyBuffSpeed_NameNone")
        .placeholder("tier", tier != null ? tier : "")
        .done(player);
    final ArenaBuff buffState = this.parentMenu.getBuffState();
    ItemStack is = Helper.get().parseItemStack("SUGAR");
    final ItemMeta im = is.getItemMeta();
    final boolean active = val == buffState.getSpeedAmplification();

    im.setDisplayName((active ? ChatColor.GREEN : ChatColor.GOLD) + name);
    is.setItemMeta(im);

    if (active)
      is = NMSHelper.get().setGlowEffect(is, true);

    return new GUIItem(is, (g0, g1, g2) -> {
      buffState.setSpeedAmplification(val);

      Message.buildByKey("PrivateGames_ModifyBuffSpeed")
          .placeholder("effect", name.toLowerCase())
          .send(player);
      draw(player);
    });
  }
}
