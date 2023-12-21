package me.harsh.privategamesaddon.api;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.player.PlayerDataAPI;
import de.marcely.bedwars.api.remote.RemoteArena;
import java.util.Collection;
import java.util.function.Consumer;
import lombok.experimental.UtilityClass;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class PrivateGameAPI {

    @Deprecated
    public boolean hasPermision(@NotNull Player player){
        return player.hasPermission(Settings.ADMIN_PERM) || player.hasPermission(Settings.CREATE_PERM) || player.hasPermission("*");
    }

    public boolean isPrivateGame(@NotNull Arena arena){
        return Utility.getManager().isPrivateArena(arena);
    }

    public boolean isPrivateGame(@NotNull RemoteArena arena){
        return Utility.getManager().isPrivateArena(arena);
    }

    public Collection<Arena> getPrivateGames(){
        return Utility.getManager().getPrivateArenas();
    }

    public Collection<RemoteArena> getRemotePrivateGames(){
        return Utility.getManager().getRemotePrivateArenas();
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
