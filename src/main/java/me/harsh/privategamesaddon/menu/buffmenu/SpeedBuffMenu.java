package me.harsh.privategamesaddon.menu.buffmenu;

import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.tools.Helper;
import de.marcely.bedwars.tools.NMSHelper;
import de.marcely.bedwars.tools.gui.AddItemCondition;
import de.marcely.bedwars.tools.gui.CenterFormat;
import de.marcely.bedwars.tools.gui.GUIItem;
import de.marcely.bedwars.tools.gui.type.ChestGUI;
import java.util.Locale;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.menu.PrivateGameMenu;
import me.harsh.privategamesaddon.settings.Settings;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpeedBuffMenu extends ChestGUI {


  private final PrivateGameMenu parentMenu;

  public SpeedBuffMenu(PrivateGameMenu parentMenu) {
    super(3, Message.build(Settings.SPEED_BUFF_MENU).done());
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

    addItem(getItem(player, "No speed effect", 1), condition);
    addItem(getItem(player, "Speed I", 2), condition);
    addItem(getItem(player, "Speed II", 3), condition);
    addItem(getItem(player, "Speed III", 4), condition);
    addItem(getItem(player, "Speed VI", 5), condition);

    formatRow(1, CenterFormat.CENTRALIZED_EVEN);
  }

  private GUIItem getItem(Player player, String name, int val) {
    final ArenaBuff buffState = this.parentMenu.getBuffState();
    ItemStack is = Helper.get().parseItemStack("SUGAR");
    final ItemMeta im = is.getItemMeta();
    final boolean active = val == buffState.getSpeedModifier();

    im.setDisplayName((active ? ChatColor.GREEN : ChatColor.GOLD) + name);
    is.setItemMeta(im);

    if (active)
      is = NMSHelper.get().setGlowEffect(is, true);

    return new GUIItem(is, (g0, g1, g2) -> {
      buffState.setSpeedModifier(val);

      Message.build(ChatColor.GREEN + "Players now spawn with " + name.toLowerCase(Locale.ROOT)).send(player);
      draw(player);
    });
  }
}
