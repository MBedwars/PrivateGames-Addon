package me.harsh.privategamesaddon.buffs;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.arena.ArenaDeleteEvent;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import de.marcely.bedwars.api.event.arena.RoundStartEvent;
import de.marcely.bedwars.api.event.player.PlayerIngameDeathEvent;
import de.marcely.bedwars.api.event.player.PlayerIngameRespawnEvent;
import de.marcely.bedwars.api.event.player.PlayerModifyBlockPermissionEvent;
import de.marcely.bedwars.api.game.spawner.Spawner;
import de.marcely.bedwars.api.game.spawner.SpawnerDurationModifier;
import de.marcely.bedwars.api.game.upgrade.Upgrade;
import de.marcely.bedwars.api.game.upgrade.UpgradeState;
import me.harsh.privategamesaddon.PrivateGamesPlugin;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class PlayerBuffListener implements Listener {

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event){
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player){
            final Player player = (Player) event.getEntity();
            final Arena arena = GameAPI.get().getArenaByPlayer(player);

            if (arena == null)
                return;
            if (arena.getStatus() != ArenaStatus.RUNNING)
                return;

            final ArenaBuff buff = Utility.getManager().getBuffState(arena);

            if (buff == null)
                return;

            if (buff.isOneHitKill())
                player.setHealth(0);
        }
    }

    @EventHandler
    public void onPlayerTakeFallDamage(EntityDamageEvent event){
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL && event.getEntity() instanceof Player){
            final Player player = (Player) event.getEntity();
            final Arena arena = GameAPI.get().getArenaByPlayer(player);

            if (arena == null)
                return;
            if (arena.getStatus() != ArenaStatus.RUNNING)
                return;

            final ArenaBuff buff = Utility.getManager().getBuffState(arena);

            if (buff == null)
                return;

            if (!buff.isFallDamageEnabled())
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerIngameRespawnEvent event){
        final Player player = event.getPlayer();
        final Arena arena = event.getArena();
        final ArenaBuff buff = Utility.getManager().getBuffState(arena);

        if (buff == null)
            return;

        player.setMaxHealth(buff.getHealth());
        player.setHealth(buff.getHealth());

        if (buff.isLowGravity()) {
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.JUMP,
                Integer.MAX_VALUE,
                3));
        }

        if (buff.getSpeedModifier() != 1) {
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.SPEED,
                Integer.MAX_VALUE,
                buff.getSpeedModifier()));
        }
    }

    @EventHandler
    public void onStart(RoundStartEvent event) {
        final Arena arena = event.getArena();

        if (!Utility.getManager().isPrivateArena(arena)) {
            Utility.getManager().removeBuffState(arena);
            return;
        }

        final ArenaBuff buff = Utility.getManager().getBuffState(arena);

        if (buff == null)
            return;

        if (buff.getSpawnRateMultiplier() != 3) {
            for (Spawner spawner: arena.getSpawners()){
                for (Team team: arena.getEnabledTeams()){
                    if (spawner.getLocation().distance(arena.getTeamSpawn(team)) <= 15){
                        spawner.addDropDurationModifier(
                            "private_games:buff_multiplier",
                            PrivateGamesPlugin.getInstance(),
                            SpawnerDurationModifier.Operation.SET,
                            buff.getSpawnRateMultiplier());
                    }
                }
            }
        }

        if (buff.isNoEmeralds()){
            for (Spawner spawner : arena.getSpawners()){
                for (Team team: arena.getEnabledTeams()){
                    if (spawner.getLocation().distance(arena.getTeamSpawn(team)) >= 20) {
                        spawner.addDropDurationModifier(
                            "private_games:no_emeralds",
                            PrivateGamesPlugin.getInstance(),
                            SpawnerDurationModifier.Operation.SET,
                            9999999999999.9);
                    }
                }
            }
        }

        arena.getPlayers().forEach(player -> {
            if (buff.isLowGravity()) {
                player.addPotionEffect(new PotionEffect(
                    PotionEffectType.JUMP,
                    Integer.MAX_VALUE,
                    3));
            }

            if (buff.getSpeedModifier() != 1) {
                player.addPotionEffect(new PotionEffect(
                    PotionEffectType.SPEED,
                    Integer.MAX_VALUE,
                    buff.getSpeedModifier()));
            }
        });

        Bukkit.getScheduler().runTaskLater(PrivateGamesPlugin.getInstance(), () -> {
            if (event.getArena().getStatus() != ArenaStatus.RUNNING)
                return;

            // update health
            event.getArena().getPlayers().forEach(player -> {
                player.setMaxHealth(buff.getHealth());
                player.setHealth(buff.getHealth());
            });

            // autom max upgrade
            if (buff.isMaxUpgrades()) {
                for (Team team: arena.getRemainingTeams()){
                    final UpgradeState state = arena.getUpgradeState(team);
                    for (Upgrade upgrade : GameAPI.get().getUpgrades()) {
                        if (state.isMaxLevel(upgrade)){
                            continue;
                        }else {
                            state.doUpgrade(upgrade.getMaxLevel(), null);
                        }
                    }
                }
            }
        }, 10);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEnd(RoundEndEvent event) {
        Utility.getManager().removeBuffState(event.getArena());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDelete(ArenaDeleteEvent event) {
        Utility.getManager().unsetPrivateArena(event.getArena());
    }


    @EventHandler
    public void onPlayerDeath(PlayerIngameDeathEvent event){
        final Arena arena = event.getArena();
        final ArenaBuff buff = Utility.getManager().getBuffState(arena);

        if (buff == null)
            return;

        event.setDeathSpectateDuration(buff.getRespawnTime());
    }

    @EventHandler
    public void onPlayerBlockBreak(PlayerModifyBlockPermissionEvent event){
        final Arena arena = event.getArena();
        final ArenaBuff buff = Utility.getManager().getBuffState(arena);

        if (buff == null)
            return;

        if (!buff.isBlocksProtected()){
            event.setIssuePresent(PlayerModifyBlockPermissionEvent.Issue.INSIDE_NON_BUILD_RADIUS, false);
            event.setIssuePresent(PlayerModifyBlockPermissionEvent.Issue.NON_PLAYER_PLACED, false);
            event.setIssuePresent(PlayerModifyBlockPermissionEvent.Issue.BLACKLISTED_MATERIAL, false);
        }
    }

    /*@EventHandler(priority = EventPriority.HIGH)
    public void onPlayerBlockPlace(BlockPlaceEvent event){
        final Block block = event.getBlock();
        final Player player = event.getPlayer();
        final Arena arena = GameAPI.get().getArenaByPlayer(player);

        if (arena == null)
            return;

        if (!Utility.getManager().isPrivateArena(arena))
            return;

        final ArenaBuff buff = Utility.getBuff(arena);
    }*/
}
