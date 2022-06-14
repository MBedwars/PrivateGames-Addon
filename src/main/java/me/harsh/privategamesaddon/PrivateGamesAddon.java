package me.harsh.privategamesaddon;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.GameAPI;
import me.harsh.privategamesaddon.buffs.PlayerBuffListener;
import me.harsh.privategamesaddon.commands.PrivateCommandGroup;
import me.harsh.privategamesaddon.events.InventoryListener;
import me.harsh.privategamesaddon.events.PlayerListener;
import me.harsh.privategamesaddon.lobbyItems.BuffItem;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
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
        if(Bukkit.getPluginManager().getPlugin("MBedwars") == null) {
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            Common.log("Dependency MBedwars wasn't present!");
        }
        registerEvents(new PlayerListener(Utility.getManager()));
        registerEvents(new PlayerBuffListener());
        registerEvents(new InventoryListener());
        registerCommands("bwp", new PrivateCommandGroup());
        BedwarsAPI.onReady(() -> GameAPI.get().registerLobbyItemHandler(new BuffItem()));
        new PrivateGamePlaceholder().register();
        Common.log("&a----------------------------");
        Common.log("&a    &r");
        Common.log("&aEnabling Private Games Addon");
        Common.log("&a    &r");
        Common.log("&a----------------------------");
        if (Bukkit.getPluginManager().getPlugin("Parties") == null && Bukkit.getPluginManager().getPlugin("PartyAndFriends") != null){
            Utility.isPfa = true;
            Utility.isParty = false;
            Common.log("&a-----------Party and friends found!------------");
        }else if (Bukkit.getPluginManager().getPlugin("Parties") != null && Bukkit.getPluginManager().getPlugin("PartyAndFriends") == null){
            Common.log("&a----------Parties plugin found!---------------");
            Utility.isParty = true;
            Utility.isPfa = false;
        }else if (Bukkit.getPluginManager().getPlugin("Parties") != null && Bukkit.getPluginManager().getPlugin("PartyAndFriends") != null){
            if (Settings.PARTIES_PRIORITY){
                Common.log("&a--------- 2 Party plugins found!------------");
                Common.log("&a--------- Using Parties due to priority------------");
                Utility.isPfa = false;
                Utility.isParty = true;
            }else {
                Common.log("&a--------- 2 Party plugins found!------------");
                Common.log("&a--------- Using Party and friends due to priority------------");
                Utility.isParty = false;
                Utility.isPfa = true;
        }
        }

        else {
            Common.log("&cNO PARTY PLUGIN FOUND! DISABLING PLUGIN!");
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    protected void onReloadablesStart() {
//        registerEvents(new PlayerListener(Utility.getManager()));
//        registerEvents(new PlayerBuffListener());
//        registerEvents(new InventoryListener());
        registerCommands("bwp", new PrivateCommandGroup());

        BedwarsAPI.onReady(() -> GameAPI.get().registerLobbyItemHandler(new BuffItem()));
        new PrivateGamePlaceholder().register();
    }

    @Override
    protected void onPluginReload() {
        final PrivateGameManager manager = Utility.getManager();
        manager.getPrivateArenas().forEach(arena -> {
            arena.endMatch(null);
            manager.getPrivateArenas().remove(arena);
        });
        manager.partyMembersMangingMap.clear();
        manager.playerStatsList.clear();
        manager.privateGameMode.clear();
    }

    @Override
    public List<Class<? extends YamlStaticConfig>> getSettings() {
        return Arrays.asList(Settings.class);
    }
}
