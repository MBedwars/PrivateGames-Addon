package me.harsh.privategamesaddon.commands;

import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.api.player.PlayerDataAPI;
import java.util.Collections;
import java.util.List;
import me.harsh.privategamesaddon.PrivateGamesPlugin;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrivateGameCreateCommand extends Command.Executor {

    public PrivateGameCreateCommand(PrivateGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        final Player player = (Player) sender;

        PlayerDataAPI.get().getProperties(player, props -> {
            final PrivateGameManager manager = Utility.getManager();
            final boolean mode = manager.getPlayerPrivateMode(props);

            manager.setPrivateGameMode(props, !mode);

            if (!mode)
                Message.buildByKey("PrivateGames_SetModeNormal").send(player);
            else
                Message.buildByKey("PrivateGames_SetModeCreation").send(player);
        });
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
