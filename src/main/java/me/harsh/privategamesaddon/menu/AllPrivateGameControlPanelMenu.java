package me.harsh.privategamesaddon.menu;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.api.player.PlayerDataAPI;
import de.marcely.bedwars.api.remote.RemoteAPI;
import de.marcely.bedwars.api.remote.RemoteArena;
import de.marcely.bedwars.api.remote.RemotePlayer;
import de.marcely.bedwars.api.remote.RemotePlayerAddResult;
import de.marcely.bedwars.tools.Helper;
import de.marcely.bedwars.tools.NMSHelper;
import de.marcely.bedwars.tools.gui.GUIItem;
import de.marcely.bedwars.tools.gui.type.ChestGUI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AllPrivateGameControlPanelMenu extends ChestGUI {

  private int page = 1;

  public AllPrivateGameControlPanelMenu() {
    super(3, Message.buildByKey("PrivateGames_AdminMenuTitle").done());
  }

  @Override
  public void open(Player player) {
    draw(player);

    super.open(player);
  }

  private void draw(Player player) {
    // calc which arenas to display
    final int pageItemsCount = 9 * 4;
    final Collection<RemoteArena> allArenas = RemoteAPI.get().getArenas().stream()
        .filter(a -> Utility.getManager().isPrivateArena(a))
        .collect(Collectors.toList());
    final List<RemoteArena> arenas = allArenas.stream()
        .sorted(Comparator.comparing(a -> a.getName().replace("@", "").toLowerCase()))
        .skip((this.page - 1) * pageItemsCount)
        .limit(pageItemsCount)
        .collect(Collectors.toList());
    final boolean hasMore = allArenas.size() > arenas.size() + (this.page - 1) * pageItemsCount;

    if (this.page > 1 && arenas.isEmpty()) {
      this.page--;
      draw(player);
      return;
    }

    clear();

    // display arenas
    if (arenas.isEmpty()) {
      setItem(
          getNoArenasItem(player),
          4,
          0
      );

    } else {
      setHeight((int) Math.ceil(arenas.size() / (double) pageItemsCount) + 2);

      for (RemoteArena arena : arenas)
        addItem(getArenaItem(arena, player));
    }

    // bottom bar
    {
      final ItemStack barItem = Helper.get().parseItemStack("GRAY_STAINED_GLASS_PANE {DisplayName: \" \"}");

      for (int x = 0; x < 9; x++)
        setItem(barItem, x, getHeight() - 1);

      if (this.page > 1) {
        setItem(
            Helper.get().parseItemStack("ARROW {DisplayName: \"<--\"}"),
            0,
            getHeight() - 1,
            (g0, g1, g2) -> {
              this.page--;
              draw(player);
            }
        );
      }

      if (hasMore) {
        setItem(
            Helper.get().parseItemStack("ARROW {DisplayName: \"-->\"}"),
            8,
            getHeight() - 1,
            (g0, g1, g2) -> {
              this.page++;
              draw(player);
            }
        );
      }
    }
  }

  private ItemStack getNoArenasItem(Player player) {
    final ItemStack is = Helper.get().parseItemStack("BARRIER");
    final ItemMeta im = is.getItemMeta();

    im.setDisplayName(ChatColor.RED + Message.buildByKey("SetupGUI_NoArenasCreated").done(player));
    is.setItemMeta(im);

    return is;
  }

  private GUIItem getArenaItem(RemoteArena arena, Player player) {
    ItemStack is = NMSHelper.get().hideAttributes(arena.getIcon());
    final ItemMeta im = is.getItemMeta();

    im.setDisplayName(ChatColor.WHITE + arena.getName());
    im.setLore(Arrays.asList(
        "",
        ChatColor.YELLOW + "[LEFT]" + ChatColor.GRAY + " to stop the private game",
        ChatColor.YELLOW + "[RIGHT]" + ChatColor.GRAY + " to enter the private game"
    ));
    is.setItemMeta(im);

    if (arena.getStatus() == ArenaStatus.RUNNING)
      is = NMSHelper.get().setGlowEffect(is, true);

    return new GUIItem(is, (g0, leftClick, g1) -> {
      if (leftClick)
        onStopClick(arena, player);
      else
        onEnterClick(arena, player);
    });
  }

  private void onStopClick(RemoteArena arena, Player player) {
    /*if (arena.getStatus() == ArenaStatus.RUNNING) {
      Message.build(Settings.ILLEGAL_JOIN_MESSAGE).send(player);
      arena.addSpectator(RemoteAPI.get().getOnlinePlayer(player));
    }*/

    if (!arena.isLocal()) {
      Message.buildByKey("PrivateGames_AdminNotSameServer")
          .placeholder("server", arena.getRemoteServer().getBungeeChannelName())
          .send(player);
      return;
    }

    final PrivateGameManager manager = Utility.getManager();
    final Arena localArena = arena.getLocal();

    localArena.broadcast(Message.buildByKey("PrivateGames_AdminForceStop"));

    manager.unsetPrivateArena(localArena);
    localArena.kickAllPlayers();
  }

  private void onEnterClick(RemoteArena arena, Player player) {
    if (arena.getStatus() != ArenaStatus.LOBBY)
      return;

    Message.buildByKey("PrivateGames_AdminJoinMatch")
        .placeholder("player", player.getDisplayName())
        .send(player);

    PlayerDataAPI.get().getProperties(player, props -> {
      final RemotePlayer remotePlayer = RemoteAPI.get().getOnlinePlayer(player);
      final PrivateGameManager manager = Utility.getManager();

      if (remotePlayer == null)
        return;

      manager.setJoinEnforced(props, true);
      RemoteAPI.get().flushQueuedPlayerData();

      arena.addPlayer(remotePlayer, result -> {
        manager.setJoinEnforced(props, false);

        final RemotePlayerAddResult.GeneralResult genres = result.getGeneralResult();

        if (genres == RemotePlayerAddResult.GeneralResult.SUCCESS)
          return;
        if (genres == RemotePlayerAddResult.GeneralResult.ARENA_NOT_LOBBY) {
          final ArenaStatus status = arena.getStatus();

          if (status == ArenaStatus.END_LOBBY || status == ArenaStatus.RESETTING) {
            Message.buildByKey("JoinMessage_reseting")
                .placeholder("arena", arena.getDisplayName())
                .send(player);
          } else {
            Message.buildByKey("JoinMessage_stopped")
                .placeholder("arena", arena.getDisplayName())
                .send(player);
          }
        } else {
          final RemotePlayerAddResult.PlayerResult pres = result.getPlayerResult(player.getUniqueId());

          Message.buildByKey("Join_Fail")
              .placeholder("reason", "[" + genres.name() + ", " + pres.name() + "]")
              .send(player);
        }
      });
    });
  }
}
