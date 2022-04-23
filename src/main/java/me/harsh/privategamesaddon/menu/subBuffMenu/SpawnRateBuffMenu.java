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


public class SpawnRateBuffMenu extends Menu {
    private final Button one;
    private final Button two;
    private final Button three;
    public SpawnRateBuffMenu(){
        super(new PrivateGameMenu());
        setTitle(Settings.RESPAWN_BUFF_MENU);
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
                if (buff.getSpawnRateMultiplier() == 10){
                    buff.setSpawnRateMultiplier(1);
                    restartMenu("&cSet Spawn rate multiplier to 1 time again!");
                    return;
                }
                buff.setRespawnTime(10);
                restartMenu("&aSet Spawn rate multiplier to 3 times!");
            }

            @Override
            public ItemStack getItem() {
                final Player player = getViewer();
                final Arena arena = GameAPI.get().getArenaByPlayer(player);
                final ArenaBuff buff = Utility.getBuff(arena);
                return ItemCreator.of(CompMaterial.GOLD_INGOT,
                        "&a3 Times",
                        "",
                        "Set Spawner multiplier rate ",
                        "to three times")
                        .glow(buff.getRespawnTime() == 10).build().make();
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
                if (buff.getSpawnRateMultiplier() == 5){
                    buff.setSpawnRateMultiplier(1);
                    restartMenu("&cSet Spawn rate multiplier to 1 time again!");
                    return;
                }
                buff.setRespawnTime(5);
                restartMenu("&aSet Spawn rate multiplier to 2 times!");
            }

            @Override
            public ItemStack getItem() {
                final Player player = getViewer();
                final Arena arena = GameAPI.get().getArenaByPlayer(player);
                final ArenaBuff buff = Utility.getBuff(arena);
                return ItemCreator.of(CompMaterial.GOLD_INGOT,
                                "&a2 Times",
                                "",
                                "Set Spawner multiplier rate ",
                                "to two times")
                        .glow(buff.getRespawnTime() == 5).build().make();
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
                buff.setRespawnTime(1);
                restartMenu("&aSet Spawn rate multiplier to normal");
            }

            @Override
            public ItemStack getItem() {
                final Player player = getViewer();
                final Arena arena = GameAPI.get().getArenaByPlayer(player);
                final ArenaBuff buff = Utility.getBuff(arena);
                return ItemCreator.of(CompMaterial.GOLD_INGOT,
                                "&aNormal",
                                "",
                                "Set Spawner multiplier rate ",
                                "to one time (normal)")
                        .glow(buff.getRespawnTime() == 1).build().make();
            }
        };
    }

    @Override
    public ItemStack getItemAt(int slot) {
        if (slot == 10){
            return one.getItem();
        }else if (slot == 13){
            return two.getItem();
        }else if (slot == 16){
            return three.getItem();
        }
        return super.getItemAt(slot);
    }
}
