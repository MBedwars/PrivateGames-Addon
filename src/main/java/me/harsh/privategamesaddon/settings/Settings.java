package me.harsh.privategamesaddon.settings;

import de.marcely.bedwars.tools.YamlConfigurationDescriptor;
import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import me.harsh.privategamesaddon.PrivateGamesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Settings {

    private static final byte VERSION = 2;

    public static boolean DISABLE_PRIZES;
    public static boolean PER_BUFF_PERM;
    public static String IS_PRIVATE_GAME;

    private static File getFile(PrivateGamesPlugin plugin) {
        return new File(plugin.getAddon().getDataFolder(), "settings.yml");
    }

    public static void load(PrivateGamesPlugin plugin) {
        synchronized (Settings.class) {
            try {
                loadUnchecked(plugin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadUnchecked(PrivateGamesPlugin plugin) throws Exception {
        final File file = getFile(plugin);

        if (!file.exists())
            save(plugin);

        // load it
        final FileConfiguration config = new YamlConfiguration();

        try (Reader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            config.load(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // read it
        {
            // Legacy support
            DISABLE_PRIZES = config.getBoolean("Features.Disable_Prizes", DISABLE_PRIZES);
            PER_BUFF_PERM = config.getBoolean("Features.Per_buff_perm", PER_BUFF_PERM);
            IS_PRIVATE_GAME = config.getString("Placeholders.Is_private_game", IS_PRIVATE_GAME);

            // New ones
            DISABLE_PRIZES = config.getBoolean("Features_Disable_Prizes", DISABLE_PRIZES);
            PER_BUFF_PERM = config.getBoolean("Features_Per_buff_perm", PER_BUFF_PERM);
            IS_PRIVATE_GAME = config.getString("Placeholders_Is_private_game", IS_PRIVATE_GAME);

            if (config.getStringList("Command_Aliases") != null)
                Bukkit.getPluginCommand("privategames").setAliases(config.getStringList("Command_Aliases"));
        }

        // auto update file if newer version
        {
            final int currentVersion = config.getInt("File_Version", -1);

            if (currentVersion != VERSION)
                save(plugin);
        }
    }

    private static void save(PrivateGamesPlugin plugin) throws Exception {
        final YamlConfigurationDescriptor config = new YamlConfigurationDescriptor();

        config.addComment("============================================");
        config.addComment("");
        config.addComment("    P R I V A T E  G A M E S  A D D O N");
        config.addComment("");
        config.addComment("============================================");

        config.set("File_Version", VERSION);
        config.addEmptyLine();
        config.addEmptyLine();

        config.addComment("If set to true: Players won't receive coins, achievements, stats etc.");
        config.set("Features_Disable_Prizes", DISABLE_PRIZES);

        config.addComment("If set to true the perms set in the Perms section of that buff will be used");
        config.set("Features_Per_buff_perm", PER_BUFF_PERM);

        config.addEmptyLine();

        config.addComment("# This placeholder can be used inside Scoreboard to present if the game is private or not! [P] on hypixel");
        config.set("Placeholders_Is_private_game", IS_PRIVATE_GAME);

        if (config.getStringList("Command_Aliases") != null)
            Bukkit.getPluginCommand("privategames").setAliases(config.getStringList("Command_Aliases"));

        // save
        try (Writer writer = Files.newBufferedWriter(getFile(plugin).toPath(), StandardCharsets.UTF_8)) {
            writer.write(config.saveToString());
        }
    }
}
