package me.harsh.privategamesaddon.utils;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.settings.Settings;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.remain.Remain;

import java.util.UUID;

@UtilityClass
public class Utility {
    public PrivateGameManager manager = new PrivateGameManager();

    public PrivateGameManager getManager() {
        return manager;
    }

    @Getter
    public PartiesAPI api = Parties.getApi();

    public PartyPlayer getPlayer(@NotNull Player player) {
        return api.getPartyPlayer(player.getUniqueId());
    }


    public Party getParty(@NotNull Player player) {
        Valid.checkNotNull(getPlayer(player).getPartyId());
        return api.getParty(getPlayer(player).getPartyId());
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

    public Boolean isPrivateGame(@NotNull Arena arena){
        return getManager().privateArenas.contains(arena);
    }

}
