package me.harsh.privategamesaddon.menu;

import com.alessiodp.parties.api.Parties;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.party.PafParty;
import me.harsh.privategamesaddon.party.PartiesIParty;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class AllPrivateGameControlPanelMenu extends MenuPagged<Arena> {
    public AllPrivateGameControlPanelMenu() {
        super(Utility.getManager().getPrivateArenas());
    }

    @Override
    protected ItemStack convertToItemStack(Arena arena) {
        return ItemCreator.of(CompMaterial.PAPER,
                arena.getDisplayName(),
                "",
                "&b[LEFT] click to stop the private game", "&b[RIGHT] click to enter the private game")
                .glow(arena.getStatus() == ArenaStatus.RUNNING).build().make();
    }

    @Override
    protected void onPageClick(Player player, Arena arena, ClickType clickType) {
        final PrivateGameManager manager = Utility.getManager();
        if (clickType.isLeftClick()){
            arena.broadcast(Settings.PREFIX + " &cTHE ARENA IS FORCED STOPPED BY ADMIN..");
            manager.getPrivateArenas().remove(arena);
            manager.partyMembersMangingMap.remove(arena);
            arena.getPlayers().forEach(player1 -> manager.playerStatsList.remove(player1.getUniqueId()));
            arena.kickAllPlayers();
        }else if (clickType.isRightClick()){
            rightClick(player, arena);
        }

    }
    private void rightClick(Player player, Arena arena){
        final PrivateGameManager manager = Utility.getManager();
        if (Utility.isParty){
            PartiesIParty party = (PartiesIParty) manager.partyMembersMangingMap.get(arena);
            party.getParty().addMember(Parties.getApi().getPartyPlayer(player.getUniqueId()));
        }else {
            PafParty party = (PafParty) manager.partyMembersMangingMap.get(arena);
            party.getParty().addPlayer(PAFPlayerManager.getInstance().getPlayer(player));
        }
        manager.playerStatsList.add(player.getUniqueId());
        arena.addPlayer(player);
    }
}
