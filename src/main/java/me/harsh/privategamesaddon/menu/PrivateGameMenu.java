package me.harsh.privategamesaddon.menu;
import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.menu.subBuffMenu.*;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class PrivateGameMenu extends Menu {
    private final Button healthBuff;
    private final Button oneHitBuff;
    private final Button gravityBuff;
    private final Button speedBuff;
    private final Button blockProtBuff;
    private final Button respawnTimeBuff;
    private final Button baseSpawnerBuff;
    private final Button fallDamageBuff;
    private final Button craftingBuff;
    private final Button noSpawnerBuff;

    public PrivateGameMenu(){
        setTitle(Settings.MENU_TITLE);
        setSize(9*5);
        this.noSpawnerBuff = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                if (!Utility.hasBuffPerm("SPAWNERS", player)) return;
                final ArenaBuff buff = Utility.getBuffSafe(player);
                if (buff.isNoEmeralds()){
                    buff.setNoEmeralds(false);
                    restartMenu("&aDisabled No Special Spawners!");
                    return;
                }
                buff.setNoEmeralds(true);
                restartMenu("&aEnabled Special Spawner!");
            }

            @Override
            public ItemStack getItem() {
                final ArenaBuff buff = Utility.getBuffSafe(getViewer());
                return ItemCreator.of(CompMaterial.EMERALD,
                        Settings.NO_SPAWNERS_BUFF,
                        "",
                        "Enabled you to disable",
                        "All the special spawners like",
                        "Emeralds and diamonds!")
                        .glow(buff.isNoEmeralds()).build().make();
            }
        };
        this.craftingBuff = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                if (!Utility.hasBuffPerm("CRAFT", player)) return;
                final ArenaBuff buff = Utility.getBuffSafe(player);
                if (buff.isCraftingAllowed()){
                    buff.setCraftingAllowed(false);
                    restartMenu("&aDisabled Crafting!");
                    return;
                }
                buff.setCraftingAllowed(true);
                restartMenu("&aEnabled Crafting!");
            }

            @Override
            public ItemStack getItem() {
                final ArenaBuff buff = Utility.getBuffSafe(getViewer());
                return ItemCreator.of(CompMaterial.CRAFTING_TABLE,
                                Settings.CRAFTING_BUFF,
                        "",
                        "Enables you to craft items",
                        "Inside Private game")
                        .glow(buff.isCraftingAllowed()).build().make();
            }
        };
        this.fallDamageBuff = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                if (!Utility.hasBuffPerm("FALL", player)) return;
                final ArenaBuff buff = Utility.getBuffSafe(player);
                if (buff.isFallDamageEnabled()){
                    buff.setFallDamageEnabled(false);
                    restartMenu("&aDisabled Fall Damage!");
                    return;
                }
                buff.setFallDamageEnabled(true);
                restartMenu("&aEnabled Fall Damage!");
            }

            @Override
            public ItemStack getItem() {
                final Player player = getViewer();
                final Arena arena = GameAPI.get().getArenaByPlayer(player);
                final ArenaBuff buff = Utility.getBuff(arena);
                return ItemCreator.of(CompMaterial.ENDER_PEARL,
                        Settings.FALL_DAMAGE_BUFF,
                        "",
                        "Enables you to disable",
                        "or enable fall damage")
                        .glow(buff.isFallDamageEnabled()).build().make();
            }
        };
        this.respawnTimeBuff = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                if (!Utility.hasBuffPerm("RESPAWN", player)) return;
                new RespawnBuffMenu().displayTo(player);
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.PAPER,
                        Settings.RESPAWN_TIME_BUFF,
                        "",
                        "Enables you to ",
                        "Change respawn time").build().make();
            }
        };
        this.blockProtBuff = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                if (!Utility.hasBuffPerm("BLOCK", player)) return;
                final ArenaBuff buff = Utility.getBuffSafe(player);
                if (!buff.isBlocksProtected()){
                    buff.setBlocksProtection(true);
                    restartMenu("&aEnabled Block Protection!");
                    return;
                }
                buff.setBlocksProtection(false);
                restartMenu("&cDisabled Block Protection!");
            }

            @Override
            public ItemStack getItem() {
                final ArenaBuff buff = Utility.getBuffSafe(getViewer());
                return ItemCreator.of(CompMaterial.GRASS_BLOCK,
                        Settings.DISABLE_BLOCK_PROTECTION_BUFF,
                        "",
                        "Enables you to stop the",
                        "Block protection in arenas")
                        .glow(buff.isBlocksProtected())
                        .build().make();
            }
        };
        this.baseSpawnerBuff = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                if (!Utility.hasBuffPerm("SPAWNRATE", player)) return;
                new SpawnRateBuffMenu().displayTo(player);
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.IRON_INGOT,
                                Settings.SPAWN_RATE_MUTIPLIER_BUFF,
                                "",
                                "Enables people multiply",
                                "spawn rate of base spawners!")
                        .build().make();
            }
        };
        this.speedBuff = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                final SpeedBuffMenu speedBuffMenu = new SpeedBuffMenu();
                speedBuffMenu.displayTo(player);
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.SUGAR,
                        Settings.SPEED_BUFF,
                        "",
                        "Enables you to play",
                        "in more speed than normal").build().make();
            }
        };
        this.gravityBuff = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                if (!Utility.hasBuffPerm("GRAVITY", player)) return;
                final ArenaBuff buff = Utility.getBuffSafe(player);
                if (buff.isLowGravity()){
                    buff.setLowGravity(false);
                    restartMenu("&cLow Gravity Disabled");
                    return;
                }
                buff.setLowGravity(true);
                restartMenu("&aLow Gravity Enabled");
            }

            @Override
            public ItemStack getItem() {
                final ArenaBuff buff = Utility.getBuffSafe(getViewer());
                return ItemCreator.of(CompMaterial.RABBIT_FOOT,
                        Settings.LOW_GRAVITY_BUFF,
                        "",
                        "Enables you to play in",
                        "low gravity in bedwars")
                        .glow(buff.isLowGravity())
                        .build().make();
            }
        };
        this.oneHitBuff = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                if (!Utility.hasBuffPerm("ONE", player)) return;
                final ArenaBuff buff = Utility.getBuffSafe(player);
                if (buff.isOneHitKill()){
                    buff.setOneHitKill(false);
                    restartMenu("&cOne Hit Buff Disabled!");
                    return;
                }
                buff.setOneHitKill(true);
                restartMenu("&aOne Hit Buff Enabled!");
            }

            @Override
            public ItemStack getItem() {
                final ArenaBuff buff = Utility.getBuffSafe(getViewer());
                return ItemCreator.of(CompMaterial.DIAMOND_SWORD,
                        Settings.ONE_HIT_BUFF,
                        "",
                        "Enables to one hit anyone",
                        "in the bedwars game")
                        .glow(buff.isOneHitKill())
                        .build().make();
            }
        };
        this.healthBuff = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                if (!Utility.hasBuffPerm("HEALTH", player)) return;
                final HealthBuffMenu menu1 = new HealthBuffMenu();
                menu1.displayTo(player);
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.GOLDEN_APPLE, Settings.HEALTH_BUFF,
                        "",
                        "Enables you to",
                        "increase custom health").build().make();

            }
        };

    }

    @Override
    public ItemStack getItemAt(int slot) {
        if (slot == 10){
            return oneHitBuff.getItem();
        }else if (slot == 12){
            return healthBuff.getItem();
        }else if (slot == 14){
            return gravityBuff.getItem();
        }else if (slot == 16) {
            return speedBuff.getItem();
        }else if (slot == 22){
            return this.craftingBuff.getItem();
        }else if (slot == 37) {
            return this.respawnTimeBuff.getItem();
        }else if (slot == 43){
            return this.fallDamageBuff.getItem();
        }else if (slot == 29){
            return baseSpawnerBuff.getItem();
        }else if (slot == 33){
            return blockProtBuff.getItem();
        }else if (slot == 40){
            return this.noSpawnerBuff.getItem();
        }
        return ItemCreator.of(CompMaterial.CYAN_STAINED_GLASS_PANE).build().make();
    }
}
