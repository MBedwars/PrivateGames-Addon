package me.harsh.privategamesaddon.settings;

import de.marcely.bedwars.tools.Helper;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import me.harsh.privategamesaddon.PrivateGamesAddon;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Settings {

    // Menu title, Prefix, Booleans
    public static boolean SHOULD_SAVE_STATS;
    public static boolean AUTO_WARP;

    public static boolean PER_BUFF_PERM;

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

    // Placeholders
    public static String IS_PRIVATE_GAME;

    public static String PARTY_PRIORITY;

    public static void read(PrivateGamesAddon addon) {
        try {
            final File file = new File(addon.getDataFolder(), "settings.yml");

            // first try to read the default values, in case new ones were added
            try (InputStream is = addon.getPlugin().getResource("settings.yml")) {
                read(YamlConfiguration.loadConfiguration(new InputStreamReader(is)));
            }

            // create the default file if it doesn't exist
            if (!file.exists()) {
                file.getParentFile().mkdirs();

                try (InputStream is = addon.getPlugin().getResource("settings.yml")) {
                    try (OutputStream os = Files.newOutputStream(file.toPath())) {
                        Helper.get().copy(is, os);
                    }
                }
            }

            // read their values
            read(YamlConfiguration.loadConfiguration(file));
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private static void read(Configuration config) {
        SHOULD_SAVE_STATS = config.getBoolean("Features.Save_Stats", SHOULD_SAVE_STATS);
        AUTO_WARP = config.getBoolean("Features.Auto_warp", AUTO_WARP);
        PARTY_PRIORITY = config.getString("Features.Priority", PARTY_PRIORITY);
        PER_BUFF_PERM = config.getBoolean("Features.Per_buff_perm", PER_BUFF_PERM);
        CREATE_PERM = config.getString("Perms.create_perm", CREATE_PERM);
        AUTO_WARP_PERM = config.getString("Perms.auto_warp", AUTO_WARP_PERM);
        RELOAD_PERM = config.getString("Perms.reload_perm", RELOAD_PERM);
        PARTY_BYPASS_PERM = config.getString("Perms.party_requirement", PARTY_BYPASS_PERM);
        ADMIN_PERM = config.getString("Perms.admin_perm", ADMIN_PERM);
        ONE_HIT_BUFF_PERM = config.getString("Perms.Buffs.One_hit_buff_perm", ONE_HIT_BUFF_PERM);
        CUSTOM_HEALTH_BUFF_PERM = config.getString("Perms.Buffs.Custom_health_buff_perm", CUSTOM_HEALTH_BUFF_PERM);
        GRAVITY_BUFF_PERM = config.getString("Perms.Buffs.Gravity_buff_perm", GRAVITY_BUFF_PERM);
        RESPAWN_BUFF_PERM = config.getString("Perms.Buffs.Respawn_time_buff_perm", RESPAWN_BUFF_PERM);
        NO_SPECIAL_SPAWNER_BUFF_PERM = config.getString("Perms.Buffs.No_special_spawner_buff_perm", NO_SPECIAL_SPAWNER_BUFF_PERM);
        SPEED_BUFF_PERM = config.getString("Perms.Buffs.Speed_buff_perm", SPEED_BUFF_PERM);
        NO_FALL_DAMAGE_BUFF_PERM = config.getString("Perms.Buffs.No_fall_damage_buff_perm", NO_FALL_DAMAGE_BUFF_PERM);
        MAX_UPGRADE_BUFF_PERM = config.getString("Perms.Buffs.Max_upgrades_buff_perm", MAX_UPGRADE_BUFF_PERM);
        BLOCK_PROT_BUFF_PERM = config.getString("Perms.Buffs.Block_protection_buff_perm", BLOCK_PROT_BUFF_PERM);
        SPAWN_RATE_MUTLIPLER_BUFF_PERM = config.getString("Perms.Buffs.Spawn_rate_multiplier", SPAWN_RATE_MUTLIPLER_BUFF_PERM);
        IS_PRIVATE_GAME = config.getString("Placeholders.Is_private_game", IS_PRIVATE_GAME);

        if (config.getStringList("Command_Aliases") != null)
            Bukkit.getPluginCommand("privategames").setAliases(config.getStringList("Command_Aliases"));
    }
}
