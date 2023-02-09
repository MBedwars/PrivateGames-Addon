package me.harsh.privategamesaddon.commands;

import me.harsh.privategamesaddon.settings.Settings;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.plugin.SimplePlugin;

public class PrivateGameReloadCommand extends SimpleSubCommand {

    protected PrivateGameReloadCommand(SimpleCommandGroup parent) {
        super(parent, "rl|reload");
        setPermission(null);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        final Player player = getPlayer();
        if (player.hasPermission(Settings.RELOAD_PERM)){
            SimplePlugin.getInstance().reload();
            Common.tell(player,  " " +  Settings.SUCCESSFUL_RELOAD);
        }else {
            Common.tell(player,  " " + Settings.RELOAD_NO_PERM_EROR);
        }
    }
}
