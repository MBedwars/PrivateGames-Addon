package me.harsh.privategamesaddon.managers;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.libraries.org.jetbrains.annotations.NotNull;
import lombok.Getter;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.party.IParty;
import org.bukkit.entity.Player;
import org.mineacademy.fo.collection.StrictMap;

import java.util.*;

public class PrivateGameManager {

    public final Map<Arena, IParty> partyMembersMangingMap = new HashMap<>();
    public final StrictMap<Arena, ArenaBuff> arenaArenaBuffMap = new StrictMap<>();
    public final StrictMap<UUID,Boolean> privateGameMode = new StrictMap<>();
    public final List<UUID> playerStatsList = new ArrayList<>();
    @Getter
    public final List<Arena> privateArenas = new ArrayList<>();

    public Boolean checkPlayer(@NotNull Player player){
        return privateGameMode.containsKey(player.getUniqueId());
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
