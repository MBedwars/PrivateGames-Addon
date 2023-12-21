package me.harsh.privategamesaddon.commands;

import de.marcely.bedwars.api.message.Message;
import java.util.Collections;
import java.util.List;
import me.harsh.privategamesaddon.PrivateGamesPlugin;
import me.harsh.privategamesaddon.settings.Settings;
import org.bukkit.command.CommandSender;

public class PrivateGameReloadCommand extends Command.Executor {

    public PrivateGameReloadCommand(PrivateGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        PrivateGamesPlugin.getInstance().reload();
        Message.buildByKey(Settings.SUCCESSFUL_RELOAD).send(sender);
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
