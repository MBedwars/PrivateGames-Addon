package me.harsh.privategamesaddon.utils;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import lombok.experimental.UtilityClass;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.settings.Settings;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.remain.Remain;

import java.util.UUID;

@UtilityClass
public class Utility {

    public boolean isPfa = false;
    public boolean isParty = false;
    public boolean isBedwarParty = false;
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

    public ArenaBuff getBuff( Player player){
        final Arena arena = GameAPI.get().getArenaByPlayer(player);
        if (arena == null) return null;
        return Utility.getManager().arenaArenaBuffMap.get(arena);
    }
    public ArenaBuff getBuff( Arena arena){
        if (!getManager().arenaArenaBuffMap.containsKey(arena)) return null;
        return Utility.getManager().arenaArenaBuffMap.get(arena);
    }
    public ArenaBuff getBuffSafe( Player player){
        final Arena arena = GameAPI.get().getArenaByPlayer(player);
        Valid.checkNotNull(arena, "Arena not found!");
        if (getManager().arenaArenaBuffMap.containsKey(arena)){
            return getManager().arenaArenaBuffMap.get(arena);
        }
        return null;
    }

    public Boolean hasPermision( Player player){
        if (player.hasPermission(Settings.ADMIN_PERM)) return true;
        return player.hasPermission(Settings.GLOBAL_PERM) || player.hasPermission(Settings.CREATE_PERM) || player.hasPermission("*");
    }

    public void doStatsThing(UUID uuid){
        if (Settings.SHOULD_SAVE_STATS){
            Utility.getManager().playerStatsList.add(uuid);
        }
    }

}
