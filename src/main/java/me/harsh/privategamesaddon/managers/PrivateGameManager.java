package me.harsh.privategamesaddon.managers;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.hook.HookAPI;
import de.marcely.bedwars.api.hook.PartiesHook;
import de.marcely.bedwars.api.hook.PartiesHook.Member;
import de.marcely.bedwars.api.hook.PartiesHook.Party;
import de.marcely.bedwars.api.player.PlayerDataAPI;
import de.marcely.bedwars.api.player.PlayerProperties;
import de.marcely.bedwars.tools.Helper;
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

    private static final String PROP_PRIVATE = "privategames:is_private";
    private static final String PROP_ENFORCE_JOIN = "privategames:enforce_join";

    public final Map<Arena, Party> partyMembersMangingMap = new HashMap<>();
    public final StrictMap<Arena, ArenaBuff> arenaArenaBuffMap = new StrictMap<>();
    public final List<UUID> playerStatsList = new ArrayList<>();
    @Getter
    public final List<Arena> privateArenas = new ArrayList<>();

    public boolean getPlayerPrivateMode(PlayerProperties props) {
        return props.getBoolean(PROP_PRIVATE).orElse(false);
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
}
