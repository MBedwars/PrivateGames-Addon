package me.harsh.privategamesaddon.events;

import de.marcely.bedwars.api.event.player.SpectatorItemUseNextRoundEvent;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SpectatorListener implements Listener {

  // filter out private arenas from "next round" item
  @EventHandler
  public void onSpectatorItemUseNextRound(SpectatorItemUseNextRoundEvent event) {
    final PrivateGameManager manager = Utility.getManager();

    event.getConsiderations().removeIf(manager::isPrivateArena);
  }
}