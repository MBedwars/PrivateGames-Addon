package me.harsh.privategamesaddon.commands;

import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public class PrivateGameCreateCommand extends SimpleSubCommand {

    protected PrivateGameCreateCommand(SimpleCommandGroup parent) {
        super(parent, "private");
        setPermission(null);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        final Player player = getPlayer();
        if (!Utility.hasPermision(player)){
            Common.tell(player,  Common.colorize(Settings.NO_PERM_EROR));
            return;
        }
        final PrivateGameManager manager = Utility.getManager();
        if (manager.getPlayerPrivateMode(player)){

            manager.setPrivateGameMode(player, false);

        }else if (!(manager.getPlayerPrivateMode(player))){

            manager.setPrivateGameMode(player, true);
        }
    }
}
