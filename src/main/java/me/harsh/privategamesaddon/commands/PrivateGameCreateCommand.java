package me.harsh.privategamesaddon.commands;

import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.api.player.PlayerDataAPI;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import me.harsh.privategamesaddon.PrivateGamesPlugin;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrivateGameCreateCommand extends Command.Executor {

    public PrivateGameCreateCommand(PrivateGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args, String fullUsage) {
        final Player player = (Player) sender;

        // is the player enforcing a certain mode?
        Boolean specificMode = null;

        if (args.length >= 1) {
            switch (args[0].toLowerCase(Locale.ENGLISH)) {
                case "on":
                case "true":
                    specificMode = true;
                    break;
                case "off":
                case "false":
                    specificMode = false;
                    break;
                default:
                    Message.buildByKey("Usage")
                        .placeholder("usage", fullUsage)
                        .send(sender);
                    return;
            }
        }

        // toggle mode
        final Boolean fSpecificMode = specificMode;

        PlayerDataAPI.get().getProperties(player, props -> {
            final PrivateGameManager manager = Utility.getManager();
            final boolean newMode = fSpecificMode != null ? fSpecificMode : !manager.getPlayerPrivateMode(props, player);

            manager.setPrivateGameMode(props, newMode);

            if (newMode)
                Message.buildByKey("PrivateGames_SetModeCreation").send(player);
            else
                Message.buildByKey("PrivateGames_SetModeNormal").send(player);
        });
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
