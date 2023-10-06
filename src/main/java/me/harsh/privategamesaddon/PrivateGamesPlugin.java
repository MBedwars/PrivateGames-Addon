package me.harsh.privategamesaddon;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.picker.ArenaPickerAPI;
import de.marcely.bedwars.api.hook.HookAPI;
import de.marcely.bedwars.api.hook.PartiesHook;
import java.io.File;
import me.harsh.privategamesaddon.buffs.PlayerBuffListener;
import me.harsh.privategamesaddon.commands.PrivateCommandGroup;
import me.harsh.privategamesaddon.events.InventoryListener;
import me.harsh.privategamesaddon.events.PlayerListener;
import me.harsh.privategamesaddon.lobbyItems.BuffItem;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.placeholders.PrivateGamePlaceholder;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.Bukkit;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

public final class PrivateGamesPlugin extends SimplePlugin {

    private static final int MBEDWARS_MIN_API = 24;
    private static final String MBEDWARS_MIN_VERSION = "5.3.2";

    @Override
    protected void onPluginLoad() {
        // Hotfix for trash Foundation library
        try {
            new File(getDataFolder().getParentFile().getParentFile(), "server.properties").createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPluginStart() {
        // Check MBedwars
        try {
            Class<?> apiClass = Class.forName("de.marcely.bedwars.api.BedwarsAPI");
            int apiVersion = (int) apiClass.getMethod("getAPIVersion").invoke(null);

            if (apiVersion < MBEDWARS_MIN_API)
                throw new IllegalStateException();
        } catch(Exception e) {
            getLogger().warning("Sorry, your installed version of MBedwars is not supported. Please install at least v" + MBEDWARS_MIN_VERSION);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Common.log("&a----------------------------");
        Common.log("&a    &r");
        Common.log("&aEnabling Private Games Addon");
        Common.log("&a    &r");
        Common.log("&a----------------------------");

        // Wait a second so that other plugins can register their parties hook
        Bukkit.getScheduler().runTaskLater(this, () -> {
            // Detect hooks
            final PartiesHook[] hooks = HookAPI.get().getPartiesHooks();

            if (hooks.length == 0) {
                Common.log("&cNO PARTY PLUGIN FOUND! DISABLING PLUGIN!");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }

            for (PartiesHook hook : hooks) {
                final String name =
                    (hook.getHookedPlugin() != null ? hook.getHookedPlugin() : hook.getManagingPlugin())
                        .getName();

                Common.log("&a----------- " + name + " found!------------");
            }

            // Register everything
            registerEvents(new PlayerListener(Utility.getManager()));
            registerEvents(new PlayerBuffListener());
            registerEvents(new InventoryListener());
            new PrivateGamePlaceholder().register();
            ArenaPickerAPI.get().registerConditionVariable(new PrivateArenaConditionVariable());
            new PrivateGamesAddon(this).register();

            final BuffItem item = new BuffItem();

            GameAPI.get().registerLobbyItemHandler(new BuffItem());
            item.runCacheGCScheduler();
        }, 2);


        // Hotfix for trash Foundation library
        try {
            new File(getDataFolder().getParentFile().getParentFile(), "server.properties").delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onReloadablesStart() {
        final PrivateGameManager manager = Utility.getManager();
        for (Arena privateArena : manager.getPrivateArenas()) {
            privateArena.endMatch(null);
            manager.getPrivateArenas().remove(privateArena);
        }
        manager.partyMembersMangingMap.clear();
        manager.playerStatsList.clear();
        registerCommands( new PrivateCommandGroup());

        BedwarsAPI.onReady(() -> GameAPI.get().registerLobbyItemHandler(new BuffItem()));
        new PrivateGamePlaceholder().register();
    }


}
