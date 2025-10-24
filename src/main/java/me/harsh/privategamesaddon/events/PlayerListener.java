package me.harsh.privategamesaddon.events;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.event.player.PlayerJoinArenaEvent;
import de.marcely.bedwars.api.event.player.PlayerQuitArenaEvent;
import de.marcely.bedwars.api.hook.PartiesHook.Member;
import de.marcely.bedwars.api.hook.PartiesHook.Party;
import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.api.player.PlayerDataAPI;
import java.util.stream.Collectors;
import me.harsh.privategamesaddon.api.events.PrivateArenaCreateEvent;
import me.harsh.privategamesaddon.api.events.PrivateArenaJoinEvent;
import me.harsh.privategamesaddon.api.events.PrivateArenaJoinEvent.Result;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.utils.Utility;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

public class PlayerListener implements Listener {

  private final PrivateGameManager manager;

  public PlayerListener(PrivateGameManager manager) {
    this.manager = manager;
  }

  @EventHandler
  public void onArenaJoin(PlayerJoinArenaEvent event) {
    final Arena arena = event.getArena();
    final Player player = event.getPlayer();
    final PrivateGameManager manager = Utility.getManager();
    final MutableBoolean methodFinished = new MutableBoolean(false);

    PlayerDataAPI.get().getProperties(player, props -> {
      manager.getParty(player, member -> {
        if (!player.isOnline())
          return;

        final boolean privateMode = manager.getPlayerPrivateMode(props, player);
        final Party managingParty = this.manager.getManagingParty(arena);
        Result result = onJoinArena(player, member.orElse(null), arena, managingParty, privateMode);

        // arena is public or just turned private
        if (result == null)
          return;

        // admin attempts forceful join
        if (result.isDeny() && manager.isJoinEnforced(props)) {
          manager.setJoinEnforced(props, false);
          result = Result.ALLOW_IS_ADMIN;
        }

        // ask API
        final PrivateArenaJoinEvent apiEvent = new PrivateArenaJoinEvent(player, arena, result);
        Bukkit.getPluginManager().callEvent(apiEvent);

        result = apiEvent.getResult();

        if (result.isDeny()) {
          onArenaJoinForbid(event, result, methodFinished);
          return;
        }

        // hooray
        sendJoinMessage(player, managingParty);
      });
    });

    methodFinished.setValue(true);
  }

  @Nullable
  private Result onJoinArena(Player player, @Nullable Member member, Arena arena, @Nullable Party managingParty, boolean privateMode) {
    if (managingParty == null) {
      // nobody is managing the arena yet, take it over
      if (!privateMode || member == null || !member.isLeader())
        return null;

      for (Player arenaPlayer : arena.getPlayers()) {
        if (member.getParty().getMember(arenaPlayer.getUniqueId()) != null)
          continue;

        Message.buildByKey("PrivateGames_UnableTurnPrivateContainsNonMembers").send(player);
        return null;
      }

      final PrivateArenaCreateEvent apiEvent = new PrivateArenaCreateEvent(arena, member.getParty(), player);
      Bukkit.getPluginManager().callEvent(apiEvent);

      if (apiEvent.isCancelled())
        return null;

      this.manager.setPrivateArena(arena, member.getParty());
      sendJoinMessage(player, member.getParty()); // too lazy to implement a proper create message
      return null;

    } else if (member == null || !this.manager.match(member.getParty(), managingParty))
      return Result.DENY_NOT_IN_PARTY;
    else
      return Result.ALLOW_IS_MEMBER;
  }

  private void onArenaJoinForbid(PlayerJoinArenaEvent event, Result result, MutableBoolean methodFinished) {
    final Player player = event.getPlayer();
    final Arena arena = event.getArena();

    // we may cancel the event
    if (methodFinished.isFalse()) {
      event.addIssue(result.getIssue());
      return;
    }

    // not allowed, send him back where he belongs to
    final Message msg = result.getIssue().getHintMessage(arena);

    if (msg != null)
      msg.send(player);

    arena.kickPlayer(player);
  }

  private void sendJoinMessage(Player player, Party party) {
    Message.buildByKey("PrivateGames_JoinPrivateMatch") // success message
        .placeholder("name", party.getLeaders().stream()
            .map(Member::getUsername)
            .collect(Collectors.joining(", ")))
        .send(player);
  }

  @EventHandler
  public void onArenaLeave(PlayerQuitArenaEvent event) {
    final Player player = event.getPlayer();
    final Arena arena = event.getArena();
    final PrivateGameManager manager = Utility.getManager();

    if (arena.getStatus() != ArenaStatus.LOBBY || !manager.isPrivateArena(arena))
      return;

    if (arena.getPlayers().isEmpty()) {
      manager.unsetPrivateArena(arena);
      return;
    }

    manager.getParty(player, member -> {
      if (!member.isPresent() || !member.get().isLeader())
        return;

      manager.unsetPrivateArena(arena);
    });
  }
}
