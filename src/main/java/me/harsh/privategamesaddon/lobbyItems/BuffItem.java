package me.harsh.privategamesaddon.lobbyItems;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.game.lobby.LobbyItem;
import de.marcely.bedwars.api.game.lobby.LobbyItemHandler;
import de.marcely.bedwars.api.hook.PartiesHook.Member;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;
import me.harsh.privategamesaddon.PrivateGamesPlugin;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.menu.PrivateGameMenu;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicReference;


public class BuffItem extends LobbyItemHandler {

    private Map<Player, CacheState> cachedParties = new HashMap<>();

    public BuffItem() {
        super("private", PrivateGamesPlugin.getInstance());
    }

    @Override
    public void handleUse(Player player, Arena arena, LobbyItem lobbyItem) {
        if (Utility.getManager().isPrivateArena(arena)){
            final PrivateGameMenu menu = new PrivateGameMenu();
            if (Utility.getManager().arenaArenaBuffMap.containsKey(arena)){
                menu.displayTo(player);
            }
            if (!Utility.getManager().arenaArenaBuffMap.containsKey(arena)){
                Utility.getManager().arenaArenaBuffMap.put(arena, new ArenaBuff());
            }
        }
    }

    @Override
    public boolean isVisible(Player player, Arena arena, LobbyItem lobbyItem) {
        if (!Utility.getManager().isPrivateArena(arena))
            return false;

        final AtomicReference<Boolean> ref = new AtomicReference<>(false);
        final Consumer<Optional<Member>> handle = member -> {
            // he is not a leader
            if (!member.isPresent() || !member.get().isLeader())
                return;

            ref.set(true);
        };

        final CacheState cached = this.cachedParties.get(player);

        if (cached != null) {
            cached.lastRequest = System.currentTimeMillis();

            handle.accept(cached.member);
        } else {
            Utility.getManager().getParty(player, member -> {
                this.cachedParties.put(player, new CacheState(member));
            });
        }

        return ref.get();
    }

    public void runCacheGCScheduler() {
        Bukkit.getScheduler().runTaskTimer(PrivateGamesPlugin.getInstance(), () -> {
            if (this.cachedParties.isEmpty())
                return;

            final Iterator<Entry<Player, CacheState>> it = this.cachedParties.entrySet().iterator();
            final long maxAge = System.currentTimeMillis() - 5_000;

            while (it.hasNext()) {
                final Entry<Player, CacheState> e = it.next();

                if (!e.getKey().isOnline() || e.getValue().lastRequest < maxAge)
                    it.remove();
            }
        }, 20*5, 20*5);
    }


    private static class CacheState {

        final Optional<Member> member;
        long lastRequest = System.currentTimeMillis();

        CacheState(Optional<Member> member) {
            this.member = member;
        }
    }
}
