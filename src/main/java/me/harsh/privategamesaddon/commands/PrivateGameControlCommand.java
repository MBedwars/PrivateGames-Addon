package me.harsh.privategamesaddon.commands;

import java.util.Collections;
import java.util.List;
import me.harsh.privategamesaddon.PrivateGamesPlugin;
import me.harsh.privategamesaddon.menu.AllPrivateGameControlPanelMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrivateGameControlCommand extends Command.Executor {

    public PrivateGameControlCommand(PrivateGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args, String fullUsage) {
        final Player player = (Player) sender;

        new AllPrivateGameControlPanelMenu().open(player);
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
