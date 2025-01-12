package me.harsh.privategamesaddon.commands;

import de.marcely.bedwars.api.message.Message;
import java.util.Collections;
import java.util.List;
import me.harsh.privategamesaddon.PrivateGamesPlugin;
import org.bukkit.command.CommandSender;

public class PrivateGameReloadCommand extends Command.Executor {

  public PrivateGameReloadCommand(PrivateGamesPlugin plugin) {
    super(plugin);
  }

  @Override
  public void onExecute(CommandSender sender, String[] args, String fullUsage) {
    PrivateGamesPlugin.getInstance().reload();
    Message.buildByKey("PrivateGames_Reload").send(sender);
  }

  @Override
  public List<String> onTab(CommandSender sender, String[] args) {
    return Collections.emptyList();
  }
}
