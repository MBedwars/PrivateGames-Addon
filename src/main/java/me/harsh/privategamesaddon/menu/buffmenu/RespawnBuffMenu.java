package me.harsh.privategamesaddon.menu.buffmenu;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.menu.PrivateGameMenu;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;


public class RespawnBuffMenu extends Menu {
    private final Button oneSecond;
    private final Button fiveSecond;
    private final Button tenSecond;
    public RespawnBuffMenu(){
        super(new PrivateGameMenu());
        setTitle(Settings.RESPAWN_BUFF_MENU);
        setSize(9*3);
        this.tenSecond = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                final Arena arena = GameAPI.get().getArenaByPlayer(player);
                if (arena == null || !(Utility.getManager().isPrivateArena(arena))) {
                    Common.tell(player,  " Sorry, Something went wrong!");
                }
                final ArenaBuff buff = Utility.getBuff(arena);
                if (buff == null) return;
                if (buff.getRespawnTime() == 10){
                    buff.setRespawnTime(5);
                    restartMenu("&cSet Respawn Time to 5 seconds again!");
                    return;
                }
                buff.setRespawnTime(10);
                restartMenu("&aSet Respawn Time to 10 seconds!");
            }

            @Override
            public ItemStack getItem() {
                final Player player = getViewer();
                final Arena arena = GameAPI.get().getArenaByPlayer(player);
                if (arena == null){
                    player.sendMessage("Something went wrong!");
                }
                final ArenaBuff buff = Utility.getBuff(arena);
                return ItemCreator.of(CompMaterial.PAPER,
                        "&a10 Second",
                        "",
                        "Set 10 second ",
                        "respawn time")
                        .glow(buff.getRespawnTime() == 10).build().make();
            }
        };
        this.fiveSecond = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                final Arena arena = GameAPI.get().getArenaByPlayer(player);
                if (arena == null || !(Utility.getManager().isPrivateArena(arena))) {
                    Common.tell(player,  " Sorry, Something went wrong!");
                }
                final ArenaBuff buff = Utility.getBuff(arena);
                if (buff == null) return;
                buff.setRespawnTime(5);
                restartMenu("&aSet Respawn Time to 5 seconds!");
            }

            @Override
            public ItemStack getItem() {
                final Player player = getViewer();
                final Arena arena = GameAPI.get().getArenaByPlayer(player);
                if (arena == null){
                    player.sendMessage("Something went wrong!");
                }
                assert arena != null;
                final ArenaBuff buff = Utility.getBuff(arena);
                return ItemCreator.of(CompMaterial.PAPER,
                        "&a5 Second",
                        "",
                        "Set 5 second ",
                        "respawn time")
                        .glow(buff.getRespawnTime() == 5).build().make();
            }
        };
        this.oneSecond = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                final Arena arena = GameAPI.get().getArenaByPlayer(player);
                if (arena == null || !(Utility.getManager().isPrivateArena(arena))) {
                    Common.tell(player,  " Sorry, Something went wrong!");
                }
                final ArenaBuff buff = Utility.getBuff(arena);
                if (buff == null) return;
                if (buff.getRespawnTime() == 1){
                    buff.setRespawnTime(5);
                    restartMenu("&cSet Respawn Time to 5 seconds again!");
                    return;
                }
                buff.setRespawnTime(1);
                restartMenu("&aSet Respawn Time to 1 seconds!");
            }

            @Override
            public ItemStack getItem() {
                final Player player = getViewer();
                final Arena arena = GameAPI.get().getArenaByPlayer(player);
                if (arena == null){
                    player.sendMessage("Something went wrong!");
                }
                assert arena != null;
                final ArenaBuff buff = Utility.getBuff(arena);
                return ItemCreator.of(CompMaterial.PAPER,
                        "&a1 Second",
                        "",
                        "Set 1 second ",
                        "respawn time")
                        .glow(buff.getRespawnTime() == 1).build().make();
            }
        };
    }

    @Override
    public ItemStack getItemAt(int slot) {
        if (slot == 10){
            return oneSecond.getItem();
        }else if (slot == 13){
            return fiveSecond.getItem();
        }else if (slot == 16){
            return tenSecond.getItem();
        }
        return super.getItemAt(slot);
    }
}
