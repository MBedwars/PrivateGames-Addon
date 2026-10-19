package me.harsh.privategamesaddon.lobbyitems;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.game.lobby.LobbyItem;
import de.marcely.bedwars.api.game.lobby.LobbyItemHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ForceStartItem extends LobbyItemHandler {

  private final LobbyItemCache cache;
  private final LobbyItemHandler wrapping;

  public ForceStartItem(Plugin plugin, LobbyItemCache cache, LobbyItemHandler wrapping) {
    super("private_games:force_start", plugin);

    this.cache = cache;
    this.wrapping = wrapping;
  }

  @Override
  public void handleUse(Player player, Arena arena, LobbyItem lobbyItem) {
    if (arena.getPlayers().size() >= 2 && arena.getPlayers().size() < arena.getMinPlayers())
      arena.setStatus(ArenaStatus.RUNNING);
    else
      this.wrapping.handleUse(player, arena, lobbyItem);
  }

  @Override
  public boolean isVisible(Player player, Arena arena, LobbyItem lobbyItem) {
    return arena.getPlayers().size() >= 2 && !this.wrapping.isVisible(player, arena, lobbyItem) && this.cache.isLeader(player, arena);
  }
}
