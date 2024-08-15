package me.harsh.privategamesaddon.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.harsh.privategamesaddon.PrivateGamesPlugin;
import me.harsh.privategamesaddon.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Data
@AllArgsConstructor
public class Command {

  private final String name;
  private final Executor executor;
  private final String consoleUsage, playerUsage;
  private final int minArgsConsole, minArgsPlayer;
  private final boolean isPlayerOnly;
  private final String permission;

  public String getUsage(String label, boolean isPlayer) {
    return "/" + label+ " " +
        this.name + " " +
        (isPlayer ? this.playerUsage : this.consoleUsage);
  }

  public boolean hasPermission(CommandSender sender) {
    return this.permission == null || sender.hasPermission("privategame.admin") || sender.hasPermission(this.permission);
  }


  public static abstract class Executor {

    protected final PrivateGamesPlugin plugin;

    public Executor(PrivateGamesPlugin plugin) {
      this.plugin = plugin;
    }

    public abstract void onExecute(CommandSender sender, String[] args, String usedCommand);

    public abstract List<String> onTab(CommandSender sender, String[] args);


    // helpers for tab
    public static List<String> getCommands(Collection<Command> cmds, String written, CommandSender sender) {
      return cmds.stream()
          .filter(cmd -> cmd.hasPermission(sender))
          .map(Command::getName)
          .filter(cmd -> cmd.toLowerCase().startsWith(written.toLowerCase()))
          .collect(Collectors.toList());
    }

    protected List<String> getPlayers(String written) {
      return Bukkit.getOnlinePlayers().stream()
          .map(Player::getName)
          .filter(p -> p.toLowerCase().startsWith(written.toLowerCase()))
          .collect(Collectors.toList());
    }

    protected List<String> getArray(String written, String... entries) {
      return Arrays.stream(entries)
          .filter(k -> k.toLowerCase().startsWith(written.toLowerCase()))
          .collect(Collectors.toList());
    }
  }
}