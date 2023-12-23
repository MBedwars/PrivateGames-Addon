package me.harsh.privategamesaddon.settings;

import de.marcely.bedwars.tools.Helper;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import me.harsh.privategamesaddon.PrivateGamesAddon;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Settings {

    // Menu title, Prefix, Booleans
    public static String PREFIX;
    public static String MENU_TITLE;
    public static Boolean SHOULD_SAVE_STATS;
    public static Boolean AUTO_WARP;

    public static Boolean PER_BUFF_PERM;

    // Sub menus
    public static String HEALTH_BUFF_MENU;
    public static String SPEED_BUFF_MENU;
    public static String RESPAWN_BUFF_MENU;
    public static String SPAWN_RATE_BUFF_MENU;
    public static String ADMIN_CONTROL_PANEL;

    // Buttons
    public static String ONE_HIT_BUFF;
    public static String MAX_UPGRADES_BUFF;
    public static String HEALTH_BUFF;
    public static String LOW_GRAVITY_BUFF;
    public static String SPEED_BUFF;
    public static String DISABLE_BLOCK_PROTECTION_BUFF;
    public static String RESPAWN_TIME_BUFF;
    public static String SPAWN_RATE_MUTIPLIER_BUFF;
    public static String FALL_DAMAGE_BUFF;
    public static String NO_SPAWNERS_BUFF;

    // Permisions
    public static String CREATE_PERM;
    public static String AUTO_WARP_PERM;
    public static String RELOAD_PERM;
    public static String PARTY_BYPASS_PERM;
    public static String ADMIN_PERM;

    // Buff perms
    public static String ONE_HIT_BUFF_PERM;
    public static String CUSTOM_HEALTH_BUFF_PERM;
    public static String GRAVITY_BUFF_PERM;
    public static String RESPAWN_BUFF_PERM;
    public static String NO_SPECIAL_SPAWNER_BUFF_PERM;
    public static String SPEED_BUFF_PERM;
    public static String NO_FALL_DAMAGE_BUFF_PERM;
    public static String MAX_UPGRADE_BUFF_PERM;
    public static String BLOCK_PROT_BUFF_PERM;
    public static String SPAWN_RATE_MUTLIPLER_BUFF_PERM;


    public static String ADMIN_JOIN_PGA;

    // Placeholders
    public static String IS_PRIVATE_GAME;

    // Messages

    public static String SUCCESSFUL_RELOAD;
    public static String ILLEGAL_JOIN_MESSAGE;
    public static String NO_PERM_EROR;
    public static String NO_AUTO_WARP_PERM_EROR;
    public static String NO_PLAYER_FOUND_IN_PARTY;
    public static String ARENA_IS_PRIVATE;
    public static String NOT_IN_ARENA;
    public static String NOT_IN_PRIVATE_GAME_MODE;
    public static String PLAYER_JOIN_PRIVATE_GAME;
    public static String NOT_PRIVATE_ROOM_WARP;
    public static String NOT_IN_PARTY;
    public static String PRIVATE_GAME_MODE;
    public static String NORMAL_MODE;
    public static String NO_PARTY_ON_CREATE;
    public static String ONLY_LEADER_IN_PARTY;

    public static String PARTY_PRIORITY;

    public static void read(PrivateGamesAddon addon) {
        try {
            final File file = new File(addon.getDataFolder(), "settings.yml");

            if (!file.exists()) {
                file.getParentFile().mkdirs();

                try (InputStream is = addon.getPlugin().getResource("settings.yml")) {
                    try (OutputStream os = Files.newOutputStream(file.toPath())) {
                        Helper.get().copy(is, os);
                    }
                }
            }

            read(YamlConfiguration.loadConfiguration(file));
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private static void read(Configuration config) {
        PREFIX = config.getString("Prefix");
        SHOULD_SAVE_STATS = config.getBoolean("Features.Save_Stats");
        AUTO_WARP = config.getBoolean("Features.Auto_warp");
        PARTY_PRIORITY = config.getString("Features.Priority");
        PER_BUFF_PERM = config.getBoolean("Features.Per_buff_perm");
        MENU_TITLE = config.getString("Menu.Title");
        ADMIN_CONTROL_PANEL = config.getString("Menu.Control_panel");
        HEALTH_BUFF_MENU = config.getString("Menu.Sub_Menu.Health");
        SPEED_BUFF_MENU = config.getString("Menu.Sub_Menu.Speed");
        RESPAWN_BUFF_MENU = config.getString("Menu.Sub_Menu.Respawn");
        SPAWN_RATE_BUFF_MENU = config.getString("Menu.Sub_Menu.Spawn_rate");
        NO_SPAWNERS_BUFF = config.getString("Menu.Buffs.No_emeralds_and_diamonds");
        MAX_UPGRADES_BUFF = config.getString("Menu.Buffs.Max_upgrades_buff");
        ONE_HIT_BUFF = config.getString("Menu.Buffs.One_Hit_Buff");
        HEALTH_BUFF = config.getString("Menu.Buffs.Health_Buff");
        LOW_GRAVITY_BUFF = config.getString("Menu.Buffs.Low_Gravity_Buff");
        SPEED_BUFF = config.getString("Menu.Buffs.Speed_Buff");
        DISABLE_BLOCK_PROTECTION_BUFF = config.getString("Menu.Buffs.Disable_Block_Protection_Buff");
        RESPAWN_TIME_BUFF = config.getString("Menu.Buffs.Respawn_Time_Buff");
        SPAWN_RATE_MUTIPLIER_BUFF = config.getString("Menu.Buffs.Spawn_Rate_Mutiplier_Buff");
        FALL_DAMAGE_BUFF = config.getString("Menu.Buffs.Fall_damage_buff");
        CREATE_PERM = config.getString("Perms.create_perm");
        AUTO_WARP_PERM = config.getString("Perms.auto_warp");
        RELOAD_PERM = config.getString("Perms.reload_perm");
        PARTY_BYPASS_PERM = config.getString("Perms.party_requirement");
        ADMIN_PERM = config.getString("Perms.admin_perm");
        ONE_HIT_BUFF_PERM = config.getString("Perms.Buffs.One_hit_buff_perm");
        CUSTOM_HEALTH_BUFF_PERM = config.getString("Perms.Buffs.Custom_health_buff_perm");
        GRAVITY_BUFF_PERM = config.getString("Perms.Buffs.Gravity_buff_perm");
        RESPAWN_BUFF_PERM = config.getString("Perms.Buffs.Respawn_time_buff_perm");
        NO_SPECIAL_SPAWNER_BUFF_PERM = config.getString("Perms.Buffs.No_special_spawner_buff_perm");
        SPEED_BUFF_PERM = config.getString("Perms.Buffs.Speed_buff_perm");
        NO_FALL_DAMAGE_BUFF_PERM = config.getString("Perms.Buffs.No_fall_damage_buff_perm");
        MAX_UPGRADE_BUFF_PERM = config.getString("Perms.Buffs.Max_upgrades_buff_perm");
        BLOCK_PROT_BUFF_PERM = config.getString("Perms.Buffs.Block_protection_buff_perm");
        SPAWN_RATE_MUTLIPLER_BUFF_PERM = config.getString("Perms.Buffs.Spawn_rate_multiplier");
        IS_PRIVATE_GAME = config.getString("Placeholders.Is_private_game");
        ADMIN_JOIN_PGA = config.getString("Messages.Admin_join");
        ILLEGAL_JOIN_MESSAGE = config.getString("Messages.Illegal_join_try");
        SUCCESSFUL_RELOAD = config.getString("Messages.Successfully_reload");
        NO_PLAYER_FOUND_IN_PARTY = config.getString("Messages.No_players_found_in_party");
        NO_PERM_EROR = config.getString("Messages.No_perm");
        NO_PARTY_ON_CREATE = config.getString("Messages.No_party_found");
        ARENA_IS_PRIVATE = config.getString("Messages.Arena_private");
        NO_AUTO_WARP_PERM_EROR = config.getString("Messages.Auto_warp_noperm");
        NOT_IN_ARENA = config.getString("Messages.Arena_not_found");
        NOT_IN_PRIVATE_GAME_MODE = config.getString("Messages.Private_game_wrong_mode");
        PLAYER_JOIN_PRIVATE_GAME = config.getString("Messages.Private_game_join");
        NOT_PRIVATE_ROOM_WARP = config.getString("Messages.Private_game_illegal_warp");
        NOT_IN_PARTY = config.getString("Messages.Not_in_a_party_to_warp");
        PRIVATE_GAME_MODE = config.getString("Messages.Private_game_creation_mode");
        NORMAL_MODE = config.getString("Messages.Normal_game_mode");
        ONLY_LEADER_IN_PARTY = config.getString("Messages.Only_leader_in_party");
        Bukkit.getPluginCommand("privategames").setAliases(config.getStringList("Command_Aliases"));
    }
}
