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
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class SpeedBuffMenu extends Menu {
    private final Button one;
    private final Button two;
    private final Button three;

    public SpeedBuffMenu(){
        super(new PrivateGameMenu());
        setTitle(Settings.SPEED_BUFF_MENU);
        setSize(9*3);
        this.one = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                final ArenaBuff buff = Utility.getBuffSafe(player);
                buff.setSpeedModifier(1);
                restartMenu("Speed is now 1");
            }

            @Override
            public ItemStack getItem() {
                final ArenaBuff buff = Utility.getBuffSafe(getViewer());
                return ItemCreator.of(CompMaterial.SUGAR,
                        "&6Normal Speed",
                        "",
                        "Click to set normal",
                        "speed in bedwars game")
                        .glow(buff.getSpeedModifier() == 1).build().make();
            }
        };
        this.two = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                final ArenaBuff buff = Utility.getBuffSafe(player);
                if (buff.getSpeedModifier() == 2){
                    buff.setSpeedModifier(1);
                    restartMenu("Set the Speed to 1 Again!");
                    return;
                }
                buff.setSpeedModifier(2);
                restartMenu("Speed is now 2");
            }

            @Override
            public ItemStack getItem() {
                final ArenaBuff buff = Utility.getBuffSafe(getViewer());
                return ItemCreator.of(CompMaterial.SUGAR,
                        "&6Speed 2",
                        "",
                        "Click to set speed 2",
                        "speed in bedwars game")
                        .glow(buff.getSpeedModifier() == 2).build().make();
            }
        };
        this.three = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                final ArenaBuff buff = Utility.getBuffSafe(player);
                if (buff.getSpeedModifier() == 3){
                    buff.setSpeedModifier(1);
                    restartMenu("Set the Speed to 1 Again!");
                    return;
                }
                buff.setSpeedModifier(3);
                restartMenu("Speed is now 3");
            }

            @Override
            public ItemStack getItem() {
                final ArenaBuff buff = Utility.getBuffSafe(getViewer());
                return ItemCreator.of(CompMaterial.SUGAR,
                        "&6Speed 3",
                        "",
                        "Click to set speed 3",
                        "speed in bedwars game")
                        .glow(buff.getSpeedModifier() == 3).build().make();
            }
        };
    }
    @Override
    public ItemStack getItemAt(int slot) {
        if (slot == 10){
            return one.getItem();
        }
        if (slot == 13){
            return two.getItem();
        }
        if (slot == 16){
            return three.getItem();
        }
        return super.getItemAt(slot);
    }

    @Override
    protected String[] getInfo() {
        return new String[]{
                "Allows to set Health",
                "Buffs in private games."
        };
    }
}
