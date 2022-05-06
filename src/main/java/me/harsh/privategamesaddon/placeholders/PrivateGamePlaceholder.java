package me.harsh.privategamesaddon.placeholders;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PrivateGamePlaceholder extends PlaceholderExpansion {

    // %bw_private%
    @Override
    public @NotNull String getIdentifier() {
        return "bw";
    }
    @Override
    public @NotNull String getAuthor() {
        return "WhoHarsh";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("private")){
            final Arena arena = GameAPI.get().getArenaByPlayer((Player) player);
            if (arena == null) return null;
            if (Utility.getManager().getPrivateArenas().contains(arena)){
                return Settings.IS_PRIVATE_GAME;
            }else return null;
        }
        return null;
    }
}
