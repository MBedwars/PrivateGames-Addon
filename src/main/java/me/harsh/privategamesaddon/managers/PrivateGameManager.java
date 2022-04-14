package me.harsh.privategamesaddon.managers;

import com.alessiodp.parties.api.interfaces.Party;
import de.marcely.bedwars.api.arena.Arena;
import lombok.Getter;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.collection.StrictMap;

import java.util.*;

public class PrivateGameManager {

    public final Map<Arena,Party> partyMembersMangingMap = new HashMap<>();
    public final StrictMap<Arena, ArenaBuff> arenaArenaBuffMap = new StrictMap<>();
    public final StrictMap<UUID,Boolean> privateGameMode = new StrictMap<>();
    @Getter
    public final List<Arena> privateArenas = new ArrayList<>();

    public Boolean checkPlayer(@NotNull Player player){
        return privateGameMode.contains(player.getUniqueId());
    }

    public Boolean getMode(@NotNull Player player){
        return privateGameMode.get(player.getUniqueId());
    }

    public void setMode(@NotNull Player player, Boolean bol){
        privateGameMode.remove(player.getUniqueId());
        addPlayer(player, bol);
    }



    public void addPlayer(@NotNull Player player, Boolean bol){
        privateGameMode.put(player.getUniqueId(), bol);
    }

    public void removePlayer(@NotNull Player player){
        privateGameMode.remove(player.getUniqueId());
    }

}
