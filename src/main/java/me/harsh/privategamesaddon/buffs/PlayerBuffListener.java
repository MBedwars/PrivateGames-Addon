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
import de.marcely.bedwars.api.game.spawner.SpawnerDurationModifier.Operation;
import de.marcely.bedwars.api.game.upgrade.Upgrade;
import de.marcely.bedwars.api.game.upgrade.UpgradeState;
import java.util.Map;
import me.harsh.privategamesaddon.PrivateGamesPlugin;
import me.harsh.privategamesaddon.utils.Utility;
import me.metallicgoat.tweaksaddon.config.GenTiersConfig;
import me.metallicgoat.tweaksaddon.gentiers.GenTierLevel;
import me.metallicgoat.tweaksaddon.gentiers.GenTiers;
import me.metallicgoat.tweaksaddon.gentiers.GenTiers.GenTierState;
import me.metallicgoat.tweaksaddon.gentiers.TierAction;
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
                event.setDamage(1000);
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

    @EventHandler(priority = EventPriority.HIGH /* Must be HIGH, as tweaks' GenTiers is on NORMAL */)
    public void onStart(RoundStartEvent event) {
        final Arena arena = event.getArena();

        if (!Utility.getManager().isPrivateArena(arena)) {
            Utility.getManager().removeBuffState(arena);
            return;
        }

        final ArenaBuff buff = Utility.getManager().getBuffState(arena);

        if (buff == null)
            return;

        for (Spawner spawner : arena.getSpawners()) {
            final Team team = arena.getTeamByBaseLocation(spawner.getLocation());

            if (team == null)
                continue;

            spawner.addDropDurationModifier(
                "private_games:buff_multiplier",
                PrivateGamesPlugin.getInstance(),
                Operation.MULTIPLY,
                1D/buff.getSpawnRateMultiplier());
        }

        if (buff.isBaseSpawnersOnly()) {
            for (Spawner spawner : arena.getSpawners()){
                final Team team = arena.getTeamByBaseLocation(spawner.getLocation());

                if (team != null)
                    continue;

                spawner.addDropDurationModifier(
                    "private_games:buff_only_team_spawners",
                    PrivateGamesPlugin.getInstance(),
                    SpawnerDurationModifier.Operation.SET,
                    31622400);

                if (spawner.getHologram() != null) // hide the hologram
                    spawner.getHologram().setMinVisibilityRadius(Integer.MAX_VALUE);

                // tweaks: remove gen tiers
                if (Bukkit.getPluginManager().getPlugin("MBedwarsTweaks") != null) {
                    final Map<Integer, GenTierLevel> levels = GenTiersConfig.gen_tier_levels;
                    final GenTierState state = GenTiers.getState(arena);

                    if (state != null) {
                        // can we just skip to a certain level?
                        int maxLevel = 0;

                        while (levels.containsKey(maxLevel+1))
                            maxLevel++;

                        GenTierLevel level = null;

                        while (maxLevel >= 1 && levels.get(maxLevel).getAction() != TierAction.GEN_UPGRADE)
                            level = levels.get(maxLevel--);

                        // handle calculations
                        if (level == null)
                            GenTiers.removeArena(arena);
                        else {
                            double addTime = 0;

                            for (int l=1; l<=maxLevel+1; l++)
                                addTime += levels.get(l).getTime();

                            GenTiers.scheduleNextTier(arena, level, addTime);
                        }
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
                for (Team team: arena.getTeamsWithPlayers()) {
                    final UpgradeState state = arena.getUpgradeState(team);

                    for (Upgrade upgrade : GameAPI.get().getUpgrades()) {
                        if (!state.isMaxLevel(upgrade))
                            state.doUpgrade(upgrade.getMaxLevel(), null);
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
}
