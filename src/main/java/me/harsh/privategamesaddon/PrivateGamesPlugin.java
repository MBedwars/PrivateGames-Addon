package me.harsh.privategamesaddon;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.picker.ArenaPickerAPI;
import de.marcely.bedwars.api.game.lobby.LobbyItemHandler;
import de.marcely.bedwars.api.game.lobby.LobbyItemHandlerType;
import de.marcely.bedwars.api.hook.HookAPI;
import de.marcely.bedwars.api.hook.PartiesHook;
import lombok.Getter;
import me.harsh.privategamesaddon.buffs.PlayerBuffListener;
import me.harsh.privategamesaddon.commands.CommandHandler;
import me.harsh.privategamesaddon.events.ArenaListener;
import me.harsh.privategamesaddon.events.PlayerListener;
import me.harsh.privategamesaddon.lobbyitems.BuffItem;
import me.harsh.privategamesaddon.lobbyitems.ForceStartItem;
import me.harsh.privategamesaddon.lobbyitems.LobbyItemCache;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.placeholders.PrivateGamePlaceholder;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class PrivateGamesPlugin extends JavaPlugin {

    private static final int MBEDWARS_MIN_API = 113;
    private static final String MBEDWARS_MIN_VERSION = "5.4.14";

    @Getter
    private static PrivateGamesPlugin instance;

    @Getter
    private PrivateGamesAddon addon;

    @Override
    public void onEnable() {
        instance = this;

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

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "----------------------------");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "    ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Enabling Private Games Addon");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "    ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "----------------------------");

        // Wait a second so that other plugins can register their parties hook
        Bukkit.getScheduler().runTaskLater(this, () -> {
            // Detect hooks
            final PartiesHook[] hooks = HookAPI.get().getPartiesHooks();

            if (hooks.length == 0) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "NO PARTY PLUGIN FOUND! DISABLING PLUGIN!");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }

            for (PartiesHook hook : hooks) {
                final String name =
                    (hook.getHookedPlugin() != null ? hook.getHookedPlugin() : hook.getManagingPlugin())
                        .getName();

                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "----------- " + name + " found!------------");
            }

            // Register everything
            Bukkit.getPluginManager().registerEvents(new PlayerListener(Utility.getManager()), this);
            Bukkit.getPluginManager().registerEvents(new PlayerBuffListener(), this);
            Bukkit.getPluginManager().registerEvents(new ArenaListener(), this);

            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
                new PrivateGamePlaceholder().register();

            (this.addon = new PrivateGamesAddon(this)).register();
            this.addon.registerMessageMappings();

            ArenaPickerAPI.get().registerConditionVariable(new PrivateArenaConditionVariable());

            {
                final LobbyItemCache cache = new LobbyItemCache();
                final LobbyItemHandler forceStartItem = GameAPI.get().getLobbyItemHandler(LobbyItemHandlerType.FORCE_START.getId());

                GameAPI.get().registerLobbyItemHandler(new BuffItem(this, cache));

                if (forceStartItem != null)
                    GameAPI.get().registerLobbyItemHandler(new ForceStartItem(this, cache, forceStartItem));
                else
                    getLogger().warning("Unable to wrap force-start item as it has been removed");

                cache.runCacheGCScheduler();
            }

            Settings.load(this);

            {
                final CommandHandler cmdHandler = new CommandHandler();
                final PluginCommand cmd = getCommand("privategames");

                cmdHandler.registerDefaultCommands(this);
                cmd.setExecutor(cmdHandler);
                cmd.setTabCompleter(cmdHandler);
            }
        }, 2);

    }

    public void reload() {
        final PrivateGameManager manager = Utility.getManager();

        for (Arena privateArena : manager.getPrivateArenas())
            privateArena.endMatch(null);

        manager.clearPrivateArenas();

        reloadConfig();
        Settings.load(this);
    }
}
