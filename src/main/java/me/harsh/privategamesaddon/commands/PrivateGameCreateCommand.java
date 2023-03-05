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
        Common.log("Current mode found -> " + manager.getPlayerPrivateMode(player));
        if (manager.getPlayerPrivateMode(player) ){
            Common.log("Setting value to false [we found player in pga mode]");
            manager.setPrivateGameMode(player, false);
            Common.log("Done setting value. found value from manager :-  " + manager.getPlayerPrivateMode(player));
        }else if (!(manager.getPlayerPrivateMode(player))){
            Common.log("Setting value to true [ we found player in normal mode] ");
            manager.setPrivateGameMode(player, true);
            Common.log("Done setting value. found value from manager :-  " + manager.getPlayerPrivateMode(player));
        }
    }
}
