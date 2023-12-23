package me.harsh.privategamesaddon.events;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.arena.QuitPlayerMemory;
import de.marcely.bedwars.api.event.ConfigsLoadEvent;
import de.marcely.bedwars.api.event.arena.ArenaStatusChangeEvent;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import de.marcely.bedwars.api.event.arena.RoundStartEvent;
import de.marcely.bedwars.api.player.PlayerDataAPI;
import java.util.UUID;
import java.util.function.Consumer;
import me.harsh.privategamesaddon.api.events.PrivateGameEndEvent;
import me.harsh.privategamesaddon.api.events.PrivateGameStartEvent;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ArenaListener implements Listener {

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onArenaStatusChangeEvent(ArenaStatusChangeEvent event) {
    final ArenaStatus status = event.getNewStatus();

    if (event.getOldStatus() == status)
      return;
    if (status != ArenaStatus.RESETTING && status != ArenaStatus.LOBBY)
      return;

    final Arena arena = event.getArena();

    Utility.getManager().unsetPrivateArena(arena);
  }

  @EventHandler
  public void onConfigsLoadEvent(ConfigsLoadEvent event) {
    for (Arena arena : GameAPI.get().getArenas())
      Utility.getManager().unsetPrivateArena(arena);
  }

  @EventHandler
  public void onRoundStart(RoundStartEvent event){
    final Arena arena = event.getArena();

    if (Utility.getManager().isPrivateArena(arena))
      Bukkit.getServer().getPluginManager().callEvent(new PrivateGameStartEvent(arena));
  }

  @EventHandler
  public void onArenaEnd(RoundEndEvent event){
    final Arena arena = event.getArena();
    final PrivateGameManager manager = Utility.getManager();

    if (manager.isPrivateArena(arena))
      Bukkit.getServer().getPluginManager().callEvent(new PrivateGameEndEvent(arena, event.getWinners(), event.getWinnerTeam()));

    final Consumer<UUID> applyPlayer = uuid -> {
      PlayerDataAPI.get().getProperties(uuid, props -> {
        manager.setPrivateGameMode(props, false);
      });
    };

    for (Player player : arena.getPlayers())
      applyPlayer.accept(player.getUniqueId());

    for (QuitPlayerMemory memory : arena.getQuitPlayerMemories())
      applyPlayer.accept(memory.getUniqueId());
  }
}
