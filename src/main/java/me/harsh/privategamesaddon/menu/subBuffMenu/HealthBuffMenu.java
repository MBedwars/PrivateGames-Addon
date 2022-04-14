package me.harsh.privategamesaddon.menu.subBuffMenu;

import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.menu.PrivateGameMenu;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class HealthBuffMenu extends Menu {

    private final Button twentyHearts;
    private final Button thrityHearts;
    private final Button fourtyHearts;

    public HealthBuffMenu(){
        super(new PrivateGameMenu());
        setTitle(Settings.HEALTH_BUFF_MENU);
        setSize(9*3);
        this.twentyHearts = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                final ArenaBuff buff = Utility.getBuffSafe(player);
                if (getHealthBolean(20, buff)){
                    buff.setHealth(20);
                    restartMenu("Set the Health back to normal");
                    return;
                }
                buff.setHealth(20*2);
                restartMenu("Custom hearts are now 20!");
            }

            @Override
            public ItemStack getItem() {
                final ArenaBuff buff = Utility.getBuffSafe(getViewer());
                return ItemCreator.of(CompMaterial.GOLDEN_APPLE,
                        "&620 hearts",
                        "",
                        "Click to set custom",
                        "Hearts to twenty")
                        .glow(getHealthBolean(20, buff)).build().make();
            }
        };
        this.thrityHearts = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                final ArenaBuff buff = Utility.getBuffSafe(player);
                if (getHealthBolean(30, buff)){
                    buff.setHealth(20);
                    restartMenu("Set the Health back to normal");
                    return;
                }
                buff.setHealth(30*2);
                restartMenu("Custom hearts are not 30!");
            }

            @Override
            public ItemStack getItem() {
                final ArenaBuff buff = Utility.getBuffSafe(getViewer());
                return ItemCreator.of(CompMaterial.GOLDEN_APPLE,
                        "&630 hearts",
                        "",
                        "Click to set custom",
                        "Hearts to thirty")
                        .glow(getHealthBolean( 30, buff)).build().make();
            }
        };
        this.fourtyHearts = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                final ArenaBuff buff = Utility.getBuffSafe(player);
                if (getHealthBolean(40, buff)){
                    buff.setHealth(20);
                    restartMenu("Set the Health back to normal");
                    return;
                }
                buff.setHealth(40*2);
                restartMenu("Custom hearts are not 40!");
            }

            @Override
            public ItemStack getItem() {
                final ArenaBuff buff = Utility.getBuffSafe(getViewer());
                return ItemCreator.of(CompMaterial.GOLDEN_APPLE,
                        "&640 hearts",
                        "",
                        "Click to set custom",
                        "Hearts to forty")
                        .glow(getHealthBolean(40, buff)).build().make();
            }
        };
    }
    @Override
    public ItemStack getItemAt(int slot) {
        if (slot == 10){
            return twentyHearts.getItem();
        }
        if (slot == 13){
            return thrityHearts.getItem();
        }
        if (slot == 16){
            return fourtyHearts.getItem();
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
    
    private Boolean getHealthBolean(Integer youWant, ArenaBuff buff){
        return buff.getHealth() == youWant;
    }
}
