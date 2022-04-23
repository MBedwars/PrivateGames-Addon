package me.harsh.privategamesaddon;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.GameAPI;
import me.harsh.privategamesaddon.buffs.PlayerBuffListener;
import me.harsh.privategamesaddon.commands.PrivateCommandGroup;
import me.harsh.privategamesaddon.events.InventoryListener;
import me.harsh.privategamesaddon.events.PlayerListener;
import me.harsh.privategamesaddon.placeholders.PrivateGamePlaceholder;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.Bukkit;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.Arrays;
import java.util.List;

public final class PrivateGamesAddon extends SimplePlugin {

    @Override
    protected void onPluginStart() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        }
        Common.log("&a----------------------------");
        Common.log("&a    &r");
        Common.log("&aEnabling Private Games Addon");
        Common.log("&a    &r");
        Common.log("&a----------------------------");
        registerEvents(new PlayerListener(Utility.getManager()));
        registerEvents(new PlayerBuffListener());
        registerEvents(new InventoryListener());
        registerCommands("bwp", new PrivateCommandGroup());
        BedwarsAPI.onReady(() -> {
            GameAPI.get().registerLobbyItemHandler(new LobbyItems());
        });
        new PrivateGamePlaceholder().register();
    }

    @Override
    public List<Class<? extends YamlStaticConfig>> getSettings() {
        return Arrays.asList(Settings.class);
    }
}
