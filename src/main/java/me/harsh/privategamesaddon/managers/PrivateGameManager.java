package me.harsh.privategamesaddon.managers;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.hook.HookAPI;
import de.marcely.bedwars.api.hook.PartiesHook;
import de.marcely.bedwars.api.hook.PartiesHook.Member;
import de.marcely.bedwars.api.hook.PartiesHook.Party;
import de.marcely.bedwars.api.player.PlayerDataAPI;
import de.marcely.bedwars.api.player.PlayerProperties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import lombok.Getter;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.settings.Settings;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.StrictMap;

import java.util.*;

public class PrivateGameManager {

    private final String PRIVATE = "privategames:is_private";
    public final Map<Arena, Party> partyMembersMangingMap = new HashMap<>();
    public final StrictMap<Arena, ArenaBuff> arenaArenaBuffMap = new StrictMap<>();
    public final List<UUID> playerStatsList = new ArrayList<>();
    @Getter
    public final List<Arena> privateArenas = new ArrayList<>();

    public void getPlayerPrivateMode(Player player, Consumer<Boolean> callback) {
        PlayerDataAPI.get().getProperties(player, props -> {
            final Optional<Boolean> isPrivate = props.getBoolean(PRIVATE);

            callback.accept(isPrivate.orElse(false));
        });
    }

    public void setPrivateGameMode(Player player, boolean mode) {
        PlayerDataAPI.get().getProperties(player, props -> {
            if (mode)
                props.set(PRIVATE, true);
            else
                props.remove(PRIVATE);
        });
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
                        callback.accept(Optional.empty());

                    return;
                }

                remaining.set(-1); // avoid it getting called again
                callback.accept(member);
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
}
