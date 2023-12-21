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
import me.harsh.privategamesaddon.settings.Settings;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class RespawnBuffMenu extends ChestGUI {

  private final PrivateGameMenu parentMenu;

  public RespawnBuffMenu(PrivateGameMenu parentMenu) {
    super(3, Message.build(Settings.RESPAWN_BUFF_MENU).done());
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

    addItem(getItem(player, 0), condition);
    addItem(getItem(player, 2), condition);
    addItem(getItem(player, 5), condition);
    addItem(getItem(player, 10), condition);
    addItem(getItem(player, 15), condition);

    formatRow(1, CenterFormat.CENTRALIZED_EVEN);
  }

  private GUIItem getItem(Player player, int sec) {
    final ArenaBuff buffState = this.parentMenu.getBuffState();
    ItemStack is = Helper.get().parseItemStack("PAPER");
    final ItemMeta im = is.getItemMeta();
    final boolean active = sec == buffState.getRespawnTime();

    im.setDisplayName("" + (active ? ChatColor.GREEN : ChatColor.GOLD) + sec + " second" + (sec != 1 ? "s" : ""));
    is.setItemMeta(im);

    if (active)
      is = NMSHelper.get().setGlowEffect(is, true);

    return new GUIItem(is, (g0, g1, g2) -> {
      buffState.setRespawnTime(sec);

      Message.build(ChatColor.GREEN + "Players will need " + sec + " second" + (sec != 1 ? "s" : "") + " to respawn").send(player);
      draw(player);
    });
  }
}
