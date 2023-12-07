package me.harsh.privategamesaddon.menu;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.player.PlayerDataAPI;
import de.marcely.bedwars.api.remote.RemoteAPI;
import de.marcely.bedwars.api.remote.RemoteArena;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class AllPrivateGameControlPanelMenu extends MenuPagged<RemoteArena> {
    public AllPrivateGameControlPanelMenu() {
        super(Utility.getManager().getRemotePrivateArenas());

        setTitle(Settings.ADMIN_CONTROL_PANEL);
    }

    @Override
    protected ItemStack convertToItemStack(RemoteArena arena) {
        return ItemCreator.of(CompMaterial.PAPER,
                arena.getDisplayName(),
                "",
                "&b[LEFT] click to stop the private game", "&b[RIGHT] click to enter the private game")
                .glow(arena.getStatus() == ArenaStatus.RUNNING).build().make();
    }

    @Override
    protected void onPageClick(Player player, RemoteArena arena, ClickType clickType) {
        if (arena.getStatus() == ArenaStatus.RUNNING) {
            tell( " " + Settings.ILLEGAL_JOIN_MESSAGE);
            arena.addSpectator(RemoteAPI.get().getOnlinePlayer(player));
        }

        if (!arena.isLocal()) {
            player.sendMessage(ChatColor.RED + "This only works if you are on the same server (" + arena.getRemoteServer().getBungeeChannelName() + ")");
            return;
        }

        final PrivateGameManager manager = Utility.getManager();
        final Arena localArena = arena.getLocal();

        if (clickType.isLeftClick()) {
            localArena.broadcast(Common.colorize( " &cTHE ARENA IS FORCED STOPPED BY ADMIN.."));

            manager.setPrivateArena(localArena, false);

            localArena.getPlayers().forEach(player1 -> manager.playerStatsList.remove(player1.getUniqueId()));
            localArena.kickAllPlayers();

        }else if (clickType.isRightClick()){
            rightClick(player, localArena);
            final String message = Settings.ADMIN_JOIN_PGA.replace("{p}", player.getDisplayName());
            tell(message);
        }

    }
    private void rightClick(Player player, Arena arena) {
        if (arena.getStatus() != ArenaStatus.LOBBY)
            return;

        PlayerDataAPI.get().getProperties(player, props -> {
            final PrivateGameManager manager = Utility.getManager();

            manager.playerStatsList.add(player.getUniqueId());

            manager.setJoinEnforced(props, true);
            arena.addPlayer(player);
        });
    }
}
