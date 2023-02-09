package me.harsh.privategamesaddon.commands;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import me.harsh.privategamesaddon.menu.AllPrivateGameControlPanelMenu;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.plugin.SimplePlugin;

public class PrivateGameControlCommand extends SimpleSubCommand {

    protected PrivateGameControlCommand(SimpleCommandGroup parent) {
        super(parent, "control");
        setPermission(null);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        final Player player = getPlayer();
        if (Utility.hasPermision(player)){
            final Arena arena = GameAPI.get().getArenaByPlayer(player);
            if (arena == null || arena.getStatus() == ArenaStatus.LOBBY){
                new AllPrivateGameControlPanelMenu().displayTo(player);
            }
        } else  tell( " &cSorry you're not allowed to use that!");
    }
}
