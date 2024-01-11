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
    public static Boolean SHOULD_SAVE_STATS;
    public static Boolean AUTO_WARP;

    public static Boolean PER_BUFF_PERM;

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
        SHOULD_SAVE_STATS = config.getBoolean("Features.Save_Stats");
        AUTO_WARP = config.getBoolean("Features.Auto_warp");
        PARTY_PRIORITY = config.getString("Features.Priority");
        PER_BUFF_PERM = config.getBoolean("Features.Per_buff_perm");
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
        Bukkit.getPluginCommand("privategames").setAliases(config.getStringList("Command_Aliases"));
    }
}
