package me.harsh.privategamesaddon.api;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.player.PlayerDataAPI;
import java.util.function.Consumer;
import lombok.experimental.UtilityClass;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@UtilityClass
public class PrivateGameAPI {
    public Boolean hasPermision(@NotNull Player player){
        return player.hasPermission(Settings.GLOBAL_PERM) || player.hasPermission(Settings.CREATE_PERM) || player.hasPermission("*");
    }

    public Boolean isPrivateGame(@NotNull Arena arena){
        return Utility.getManager().privateArenas.contains(arena);
    }

    public List<Arena> getPrivateGames(){
        return Utility.getManager().getPrivateArenas();
    }

    public String getPrivateGamePlaceholder(Arena arena){
        if (isPrivateGame(arena))
            return Settings.IS_PRIVATE_GAME;

        return null;
    }

    public void setPrivateGameMode(Player player, boolean value){
        PlayerDataAPI.get().getProperties(player, props -> {
            Utility.getManager().setPrivateGameMode(props, value);
        });
    }

    public void getPrivateGameMode(Player player, Consumer<Boolean> callback) {
        PlayerDataAPI.get().getProperties(player, props -> {
            callback.accept(Utility.getManager().getPlayerPrivateMode(props));
        });
    }
}
