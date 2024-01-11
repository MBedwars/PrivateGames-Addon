package me.harsh.privategamesaddon;

import de.marcely.bedwars.api.BedwarsAddon;
import de.marcely.bedwars.api.message.DefaultMessageMappings;
import de.marcely.bedwars.api.message.MessageAPI;
import org.bukkit.plugin.Plugin;

public class PrivateGamesAddon extends BedwarsAddon {

  private final PrivateGamesPlugin plugin;

  PrivateGamesAddon(PrivateGamesPlugin plugin) {
    super(plugin);

    this.plugin = plugin;
  }

  void registerMessageMappings() {
    try {
      MessageAPI.get().registerDefaultMappings(
          DefaultMessageMappings.loadInternalYAML(this.plugin, this.plugin.getResource("messages.yml")));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
