package me.harsh.privategamesaddon.placeholders;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.tools.Helper;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.harsh.privategamesaddon.PrivateGamesPlugin;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PrivateGamePlaceholder extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "privategames";
    }
    @Override
    public @NotNull String getAuthor() {
        return "WhoHarsh";
    }

    @Override
    public @NotNull String getVersion() {
        return PrivateGamesPlugin.getInstance().getDescription().getVersion();
    }

    @Override
    public String getRequiredPlugin() {
        return PrivateGamesPlugin.getInstance().getName();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("private")) {
            if (player == null)
                return "Player parameter is null";

            final Arena arena = GameAPI.get().getArenaByPlayer((Player) player);

            if (arena == null)
                return "";

            if (Utility.getManager().isPrivateArena(arena)){
                return Settings.IS_PRIVATE_GAME;
            } else
                return "";

        }else if (params.equalsIgnoreCase("private_count")) {
            return Helper.get().formatNumber(Utility.getManager().getRemotePrivateArenas().size());
        }

        return null;
    }
}
