package me.harsh.privategamesaddon.lobbyitems;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.hook.PartiesHook.Member;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import me.harsh.privategamesaddon.PrivateGamesPlugin;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LobbyItemCache {

  private Map<Player, CacheState> cachedParties = new HashMap<>();

  public boolean isLeader(Player player, Arena arena) {
    if (!Utility.getManager().isPrivateArena(arena))
      return false;

    final AtomicReference<Boolean> ref = new AtomicReference<>(false);
    final Consumer<Optional<Member>> handle = member -> {
      // he is not a leader
      if (!member.isPresent() || !member.get().isLeader())
        return;

      ref.set(true);
      Utility.getManager().updatePrivateArena(arena, member.get().getParty()); // keep its state up-to-date
    };

    final CacheState cached = this.cachedParties.get(player);
    final boolean cacheLegit = cached != null && cached.arena == arena;

    if (cacheLegit)
      handle.accept(cached.member);

    if (!cacheLegit || System.currentTimeMillis() - cached.creationTime > 5_000) {
      this.cachedParties.putIfAbsent(player, new CacheState(Optional.empty(), arena)); // no need to request a million times

      Utility.getManager().getParty(player, member -> {
        handle.accept(member);
        this.cachedParties.put(player, new CacheState(member, arena));
      });
    }

    return ref.get();
  }

  public void runCacheGCScheduler() {
    Bukkit.getScheduler().runTaskTimer(PrivateGamesPlugin.getInstance(), () -> {
      if (this.cachedParties.isEmpty())
        return;

      final Iterator<Entry<Player, CacheState>> it = this.cachedParties.entrySet().iterator();
      final long maxAge = System.currentTimeMillis() - 10_000;

      while (it.hasNext()) {
        final Entry<Player, CacheState> e = it.next();

        if (!e.getKey().isOnline() || e.getValue().creationTime < maxAge)
          it.remove();
      }
    }, 20 * 10, 20 * 10);
  }


  private static class CacheState {

    final Optional<Member> member;
    final long creationTime = System.currentTimeMillis();
    final Arena arena;

    CacheState(Optional<Member> member, Arena arena) {
      this.member = member;
      this.arena = arena;
    }
  }
}
