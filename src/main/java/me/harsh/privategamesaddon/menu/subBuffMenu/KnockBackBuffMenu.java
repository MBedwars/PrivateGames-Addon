package me.harsh.privategamesaddon.menu.subBuffMenu;

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


public class KnockBackBuffMenu extends Menu {
    private final Button one; // normal
    private final Button two; // super fast
    private final Button three; // super slow
    public KnockBackBuffMenu(){
        super(new PrivateGameMenu());
        setTitle(Settings.KNOCK_BACK_BUFF_MENU);
        setSize(9*3);
        this.three = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                final Arena arena = GameAPI.get().getArenaByPlayer(player);
                if (arena == null || !(Utility.getManager().privateArenas.contains(arena))) {
                    Common.tell(player, Settings.PREFIX + " Sorry, Something went wrong!");
                }
                final ArenaBuff buff = Utility.getBuff(arena);
                if (buff == null) return;
                if (buff.getKnockBackMultiper() == 0.3){
                    buff.setSpawnRateMultiplier(1);
                    restartMenu("&cSet Knockback multiplier to normal");
                    return;
                }
                buff.setSpawnRateMultiplier(0.3);
                restartMenu("&aSet knockback multiplier to SUPER LOW!");
            }

            @Override
            public ItemStack getItem() {
                final Player player = getViewer();
                final Arena arena = GameAPI.get().getArenaByPlayer(player);
                final ArenaBuff buff = Utility.getBuff(arena);
                return ItemCreator.of(CompMaterial.STICK,
                        "&aSUPER LOW",
                        "",
                        "Set Knockback multiplier rate ",
                        "to super low")
                        .glow(buff.getKnockBackMultiper() == 0.3).build().make();
            }
        };
        this.two = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                final Arena arena = GameAPI.get().getArenaByPlayer(player);
                if (arena == null || !(Utility.getManager().privateArenas.contains(arena))) {
                    Common.tell(player, Settings.PREFIX + " Sorry, Something went wrong!");
                }
                final ArenaBuff buff = Utility.getBuff(arena);
                if (buff == null) return;
                if (buff.getKnockBackMultiper() == 5){
                    buff.setSpawnRateMultiplier(1);
                    restartMenu("&cSet Knockback multiplier to normal");
                    return;
                }
                buff.setSpawnRateMultiplier(5);
                restartMenu("&aSet Knockback multiplier to SUPER HIGH!");
            }

            @Override
            public ItemStack getItem() {
                final Player player = getViewer();
                final Arena arena = GameAPI.get().getArenaByPlayer(player);
                final ArenaBuff buff = Utility.getBuff(arena);
                return ItemCreator.of(CompMaterial.STICK,
                                "&aSUPER HIGH",
                                "",
                                "Set Knockback multiplier rate ",
                                "to super high")
                        .glow(buff.getKnockBackMultiper() == 5).build().make();
            }
        };
        this.one = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                final Arena arena = GameAPI.get().getArenaByPlayer(player);
                if (arena == null || !(Utility.getManager().privateArenas.contains(arena))) {
                    Common.tell(player, Settings.PREFIX + " Sorry, Something went wrong!");
                }
                final ArenaBuff buff = Utility.getBuff(arena);
                if (buff == null) return;
                buff.setSpawnRateMultiplier(1);
                restartMenu("&aSet Knockback multiplier to normal");
            }

            @Override
            public ItemStack getItem() {
                final Player player = getViewer();
                final Arena arena = GameAPI.get().getArenaByPlayer(player);
                final ArenaBuff buff = Utility.getBuff(arena);
                return ItemCreator.of(CompMaterial.STICK,
                                "&aNormal",
                                "",
                                "Set Knockback multiplier rate ",
                                "to normal")
                        .glow(buff.getKnockBackMultiper() == 1).build().make();
            }
        };
    }

    @Override
    public ItemStack getItemAt(int slot) {
        if (slot == 10){
            return three.getItem();
        }else if (slot == 13){
            return one.getItem();
        }else if (slot == 16){
            return two.getItem();
        }
        return super.getItemAt(slot);
    }
}
