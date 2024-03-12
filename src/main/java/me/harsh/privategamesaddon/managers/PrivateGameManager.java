package me.harsh.privategamesaddon.managers;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaPersistentStorage;
import de.marcely.bedwars.api.hook.HookAPI;
import de.marcely.bedwars.api.hook.PartiesHook;
import de.marcely.bedwars.api.hook.PartiesHook.Member;
import de.marcely.bedwars.api.hook.PartiesHook.Party;
import de.marcely.bedwars.api.player.PlayerProperties;
import de.marcely.bedwars.api.remote.RemoteAPI;
import de.marcely.bedwars.api.remote.RemoteArena;
import de.marcely.bedwars.tools.Helper;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.settings.Settings;
import org.bukkit.entity.Player;

import java.util.*;
import org.jetbrains.annotations.Nullable;

public class PrivateGameManager {

    private static final String PROP_PRIVATE = "privategames:is_private";
    private static final String PROP_ENFORCE_JOIN = "privategames:enforce_join";
    private static final String ARENA_KEY_PRIVATE = "privategames:is_private";

    private final Map<Arena, Party> partyMembersMangingMap = new HashMap<>();
    private final Map<Arena, ArenaBuff> arenaArenaBuffMap = new HashMap<>();

    public boolean getPlayerPrivateMode(PlayerProperties props, Player player) {
        return props.getBoolean(PROP_PRIVATE).orElse(false) && player.hasPermission(Settings.CREATE_PERM) /* Otherwise he may get stuck */;
    }

    public void setPrivateGameMode(PlayerProperties props, boolean mode) {
        if (mode)
            props.set(PROP_PRIVATE, true);
        else
            props.remove(PROP_PRIVATE);
    }

    public boolean isJoinEnforced(PlayerProperties props) {
        return props.getBoolean(PROP_ENFORCE_JOIN).orElse(false);
    }

    public void setJoinEnforced(PlayerProperties props, boolean value) {
        if (value)
            props.set(PROP_ENFORCE_JOIN, true);
        else
            props.remove(PROP_ENFORCE_JOIN);
    }

    public void getParty(Player player, Consumer<Optional<Member>> callback) {
        getParty(player.getUniqueId(), callback);
    }

    public void getParty(UUID uuid, Consumer<Optional<Member>> callback) {
        final PartiesHook[] hooks = HookAPI.get().getPartiesHooks();
        final AtomicInteger remaining = new AtomicInteger(hooks.length);

        if (hooks.length == 0) {
            callback.accept(Optional.empty());
            return;
        }

        for (PartiesHook hook : hooks) {
            hook.getMember(uuid, member -> {
                final int index = remaining.decrementAndGet();

                if (index < 0 || !member.isPresent()) {
                    if (index == 0) // We went through all parties, and found none
                        Helper.get().synchronize(() -> callback.accept(Optional.empty()));

                    return;
                }

                remaining.set(-1); // avoid it getting called again
                Helper.get().synchronize(() -> callback.accept(member));
            });
        }
    }

    public boolean match(Party party1, Party party2) {
        for (Member member : party1.getLeaders()) {
            final Member matchingMember = party2.getMember(member.getUniqueId());

            if (matchingMember != null && matchingMember.isLeader())
                return true;
        }

        return false;
    }

    public Collection<Arena> getPrivateArenas() {
        return GameAPI.get().getArenas().stream()
            .filter(arena -> isPrivateArena(arena.getPersistentStorage()))
            .collect(Collectors.toList());
    }

    public Collection<RemoteArena> getRemotePrivateArenas() {
        return RemoteAPI.get().getArenas().stream()
            .filter(arena -> isPrivateArena(arena.getPersistentStorage()))
            .collect(Collectors.toList());
    }

    public boolean isPrivateArena(RemoteArena arena) {
        return isPrivateArena(arena.getPersistentStorage());
    }

    public boolean isPrivateArena(Arena arena) {
        return isPrivateArena(arena.getPersistentStorage());
    }

    public void setPrivateArena(Arena arena, Party party) {
        arena.getPersistentStorage().set(ARENA_KEY_PRIVATE, true);
        this.partyMembersMangingMap.put(arena, party);
        arena.broadcastCustomPropertyChange(); // Fixes ArenasGUI using arena picker variable not dynamically updating
    }

    public void updatePrivateArena(Arena arena, Party party) {
        this.partyMembersMangingMap.replace(arena, party);
    }

    public void unsetPrivateArena(Arena arena) {
        arena.getPersistentStorage().remove(ARENA_KEY_PRIVATE);
        this.partyMembersMangingMap.remove(arena);
        this.arenaArenaBuffMap.remove(arena);
    }

    public void clearPrivateArenas() {
        for (Arena arena : new ArrayList<>(this.partyMembersMangingMap.keySet()))
            unsetPrivateArena(arena);
    }

    private static boolean isPrivateArena(ArenaPersistentStorage storage) {
        return storage.getBoolean(ARENA_KEY_PRIVATE).orElse(false);
    }

    @Nullable
    public ArenaBuff getBuffState(Arena arena) {
        return this.arenaArenaBuffMap.get(arena);
    }

    public void removeBuffState(Arena arena) {
        this.arenaArenaBuffMap.remove(arena);
    }

    public void setBuffState(Arena arena, ArenaBuff buff) {
        this.arenaArenaBuffMap.put(arena, buff);
    }

    @Nullable
    public Party getManagingParty(Arena arena) {
        return this.partyMembersMangingMap.get(arena);
    }
}
