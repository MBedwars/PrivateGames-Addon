package me.harsh.privategamesaddon;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.GameAPI;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.buffs.PlayerBuffListener;
import me.harsh.privategamesaddon.commands.PrivateCommandGroup;
import me.harsh.privategamesaddon.events.PlayerListener;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.Arrays;
import java.util.List;

public final class PrivateGamesAddon extends SimplePlugin {

    @Override
    protected void onPluginStart() {
        Common.log("&aEnabling Private Games Addon");
        registerEvents(new PlayerListener(Utility.getManager()));
        registerEvents(new PlayerBuffListener());
        registerCommands("bwp", new PrivateCommandGroup());
        BedwarsAPI.onReady(() -> {
            GameAPI.get().registerLobbyItemHandler(new LobbyItems());
        });
    }

    @Override
    public List<Class<? extends YamlStaticConfig>> getSettings() {
        return Arrays.asList(Settings.class);
    }
}
