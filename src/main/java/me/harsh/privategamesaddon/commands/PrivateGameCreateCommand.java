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
            Common.tell(player, Settings.PREFIX + Common.colorize(Settings.NO_PERM_EROR));
            return;
        }
        final PrivateGameManager manager = Utility.getManager();
        if (manager.checkPlayer(player) && manager.getMode(player)){
            manager.setMode(player, false);
            Common.tell(player, Settings.PREFIX + " " + Settings.NORMAL_MODE);
        }else if (manager.checkPlayer(player) && !(manager.getMode(player))){
            Common.tell(player, Settings.PREFIX + " " + Settings.PRIVATE_GAME_MODE);
            manager.setMode(player, true);
        }else if (!manager.checkPlayer(player)){
            Common.tell(player, Settings.PREFIX + " " + Settings.PRIVATE_GAME_MODE);
            manager.addPlayer(player, true);
        }
    }
}
