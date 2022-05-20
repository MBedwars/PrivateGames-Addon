package me.harsh.privategamesaddon.utils;

import de.marcely.bedwars.R;
import de.marcely.bedwars.S;
import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.libraries.org.jetbrains.annotations.NotNull;
import jdk.internal.foreign.CABI;
import lombok.experimental.UtilityClass;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.settings.Settings;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.remain.Remain;

import java.util.Set;
import java.util.UUID;

@UtilityClass
public class Utility {

    public boolean isPfa;
    public boolean isParty;
    public PrivateGameManager manager = new PrivateGameManager();

    public PrivateGameManager getManager() {
        return manager;
    }


    public Player getPlayerByUuid(UUID uuid) {
        for (Player p : Remain.getOnlinePlayers())
            if (p.getUniqueId().equals(uuid)) {
                return p;
            }
        throw new IllegalArgumentException();

    }

    public ArenaBuff getBuff(@NotNull Player player){
        final Arena arena = GameAPI.get().getArenaByPlayer(player);
        if (arena == null) return null;
        return Utility.getManager().arenaArenaBuffMap.get(arena);
    }
    public ArenaBuff getBuff(@NotNull Arena arena){
        if (!getManager().arenaArenaBuffMap.contains(arena)) return null;
        return Utility.getManager().arenaArenaBuffMap.get(arena);
    }
    public ArenaBuff getBuffSafe(@NotNull Player player){
        final Arena arena = GameAPI.get().getArenaByPlayer(player);
        Valid.checkNotNull(arena, "Arena not found!");
        if (getManager().arenaArenaBuffMap.contains(arena)){
            return getManager().arenaArenaBuffMap.get(arena);
        }
        return null;
    }

    public Boolean hasPermision(@NotNull Player player){
        return player.hasPermission(Settings.GLOBAL_PERM) || player.hasPermission(Settings.CREATE_PERM) || player.hasPermission("*");
    }

    public void doStatsThing(UUID uuid){
        if (Settings.SHOULD_SAVE_STATS){
            Utility.getManager().playerStatsList.add(uuid);
        }
    }

    public Boolean hasBuffPerm(String buffName, Player player){
        if (!Settings.PER_BUFF_PERM) return false;
        switch (buffName){
            case "ONE":
                return player.hasPermission(Settings.ONE_HIT_BUFF_PERM);
            case "CRAFT":
                return player.hasPermission(Settings.CRAFTING_BUFF_PERM);
            case "RESPAWN":
                return player.hasPermission(Settings.RESPAWN_BUFF_PERM);
            case "HEALTH":
                return player.hasPermission(Settings.CUSTOM_HEALTH_BUFF_PERM);
            case "SPAWNERS":
                return player.hasPermission(Settings.NO_SPECIAL_SPAWNER_BUFF_PERM);
            case "SPAWNRATE":
                return player.hasPermission(Settings.SPAWN_RATE_MUTLIPLER_BUFF_PERM);
            case "GRAVITY":
                return player.hasPermission(Settings.GRAVITY_BUFF_PERM);
            case "FALL":
                return player.hasPermission(Settings.NO_FALL_DAMAGE_BUFF_PERM);
            case "BLOCK":
                return player.hasPermission(Settings.BLOCK_PROT_BUFF_PERM);
            case "SPEED":
                return player.hasPermission(Settings.SPEED_BUFF_PERM);
        }
        return false;
    }

}
