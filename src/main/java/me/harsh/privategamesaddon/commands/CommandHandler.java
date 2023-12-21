package me.harsh.privategamesaddon.commands;

import de.marcely.bedwars.api.message.Message;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import me.harsh.privategamesaddon.PrivateGamesPlugin;
import me.harsh.privategamesaddon.settings.Settings;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class CommandHandler implements CommandExecutor, TabExecutor {

  private final Map<String, Command> registeredCommands = new LinkedHashMap<>();

  public void registerDefaultCommands(PrivateGamesPlugin plugin) {
    registerCommand(
        "private",
        new PrivateGameCreateCommand(plugin),
        "",
        0,
        true,
        Settings.CREATE_PERM
    );
    registerCommand(
        "warp",
        new PrivateGamePartyWarpCommand(plugin),
        "",
        0,
        true,
        Settings.ADMIN_PERM
    );
    registerCommand(
        "reload",
        new PrivateGameReloadCommand(plugin),
        "",
        0,
        false,
        Settings.RELOAD_PERM
    );
    registerCommand(
        "control",
        new PrivateGameControlCommand(plugin),
        "",
        0,
        true,
        Settings.ADMIN_PERM
    );
  }

  @Override
  public boolean onCommand(CommandSender sender, org.bukkit.command.Command arg0, String label, String[] args) {
    final boolean isPlayer = sender instanceof Player;

    // help
    if (args.length == 0) {
      sender.sendMessage("" + ChatColor.GOLD + ChatColor.BOLD + "Commands:");

      for (Command cmd : this.registeredCommands.values()) {
        if (!cmd.hasPermission(sender))
          continue;

        sender.sendMessage(" " + cmd.getUsage(label, isPlayer));
      }

      {
        final List<String> message = new ArrayList<>();

        message.add("&f");
        message.add(" " + Settings.PREFIX + "&8\u2122");
        message.add("&f");
        message.add(" &7Made By WhoTech &7\u00a9 ");
        message.add("&7 Type &f/bwp" + " private &7to go into private game creation room!");
        message.add("&f");

        Message.build(message).send(sender);
      }

      return true;
    }

    // try to exectue command
    final Command cmd = getByName(args[0]);

    if (cmd == null) {
      Message.buildByKey("Unknown_Argument")
          .placeholder("arg", args[0])
          .send(sender);
      return true;
    }

    if (!cmd.hasPermission(sender)) {
      Message.build(Settings.NO_PERM_EROR).send(sender);
      return true;
    }

    if (!isPlayer && cmd.isPlayerOnly()) {
      Message.buildByKey("OnlyAs_Player").send(sender);
      return true;
    }

    args = Arrays.copyOfRange(args, 1, args.length);

    if (args.length < (isPlayer ? cmd.getMinArgsPlayer() : cmd.getMinArgsConsole())) {
      Message.buildByKey("Usage")
          .placeholder("usage", cmd.getUsage(label, isPlayer))
          .send(sender);
      return true;
    }

    // yay :)
    cmd.getExecutor().onExecute(sender, args);

    return true;
  }

  @Nullable
  @Override
  public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
    if (args.length == 0)
      return Command.Executor.getCommands(this.registeredCommands.values(), "", sender);

    if (args.length == 1)
      return Command.Executor.getCommands(this.registeredCommands.values(), args[0], sender);

    final Command cmd = getByName(args[0]);

    if (cmd == null || cmd.isPlayerOnly() && !(sender instanceof Player))
      return Collections.emptyList();

    return cmd.getExecutor().onTab(
        sender,
        Arrays.copyOfRange(args, 1, args.length)
    );
  }

  @Nullable
  public Command getByName(String name) {
    return this.registeredCommands.get(name.toLowerCase());
  }

  private void registerCommand(
      String name,
      Command.Executor executor,
      String usageConsole,
      String usagePlayer,
      int minArgsConsole,
      int minArgsPlayer,
      boolean playerOnly,
      @Nullable String permission) {

    final Command command = new Command(
        name,
        executor,
        usageConsole,
        usagePlayer,
        minArgsConsole,
        minArgsPlayer,
        playerOnly,
        permission
    );

    this.registeredCommands.put(name.toLowerCase(), command);
  }

  private void registerCommand(
      String name,
      Command.Executor executor,
      String usage,
      int minArgs,
      boolean playerOnly,
      @Nullable String permission) {

    registerCommand(name, executor, usage, usage, minArgs, minArgs, playerOnly, permission);
  }
}
