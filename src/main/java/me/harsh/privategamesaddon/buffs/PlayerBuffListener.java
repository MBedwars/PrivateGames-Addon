package me.harsh.privategamesaddon.buffs;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import de.marcely.bedwars.api.event.arena.RoundStartEvent;
import de.marcely.bedwars.api.event.player.PlayerIngameDeathEvent;
import de.marcely.bedwars.api.event.player.PlayerIngameRespawnEvent;
import de.marcely.bedwars.api.event.player.PlayerModifyBlockPermissionEvent;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mineacademy.fo.Valid;



public class PlayerBuffListener implements Listener {

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event){
        if (event.getEntity() instanceof Player){
            final Player player = (Player) event.getEntity();
            if (!Utility.getManager().privateArenas.contains(GameAPI.get().getArenaByPlayer(player))){
                return;
            }
            final ArenaBuff buff = Utility.getBuff(player);
            if (buff == null){
                return;
            }
            if (buff.isOneHitKill()){
                player.setHealth(0);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerIngameRespawnEvent event){
        final Player player = event.getPlayer();
        final Arena arena = event.getArena();
        if (!Utility.getManager().privateArenas.contains(arena)){
            return;
        }
        final ArenaBuff buff = Utility.getBuff(arena);

        if (buff.isLowGravity()){
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000, 3));
        }
        if (buff.getSpeedModifier() != 1){
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000000, buff.getSpeedModifier()));
        }
    }

    @EventHandler
    public void onStart(RoundStartEvent event){
        final Arena arena = event.getArena();
        if (!Utility.getManager().privateArenas.contains(arena)){
            return;
        }
        final ArenaBuff buff = Utility.getBuff(arena);
        Valid.checkNotNull(buff);
        event.getArena().getPlayers().forEach(player -> {
            if (buff.isLowGravity()){
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000, 3));
            }
            if (buff.getSpeedModifier() != 1){
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000000, buff.getSpeedModifier()));
            }
        });
    }

    @EventHandler
    public void onEnd(RoundEndEvent event){
        final Arena arena = event.getArena();
        if (!Utility.getManager().privateArenas.contains(arena)){
            return;
        }
       Utility.getManager().arenaArenaBuffMap.remove(arena);

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        final Player player = event.getPlayer();
        final Block block = event.getClickedBlock();
        final Arena arena = GameAPI.get().getArenaByPlayer(player);
        if (arena == null) return;
        if (block == null) return;
        final ArenaBuff buff = Utility.getBuff(arena);
        if (buff == null) return;
        if (!(buff.isBedInstaBreakEnabled())) return;
        if (Utility.getManager().privateArenas.contains(arena)){
            if (block.getType() == Material.BED){
                if (event.getAction() == Action.LEFT_CLICK_BLOCK){
                    arena.destroyBed(arena.getPlayerTeam(player));
                }
            }
        }
    }
    @EventHandler
    public void onPlayerDeath(PlayerIngameDeathEvent event){
        final Arena arena = event.getArena();
        if (!Utility.getManager().privateArenas.contains(arena)) return;
        final ArenaBuff buff = Utility.getBuff(arena);
        if (buff == null) return;
        event.setDeathSpectateDuration(buff.getRespawnTime());
    }

    @EventHandler
    public void onPlayerBlockBreak(PlayerModifyBlockPermissionEvent event){
        final Arena arena = event.getArena();
        if (!Utility.getManager().privateArenas.contains(arena)) return;
        final ArenaBuff buff = Utility.getBuff(arena);
        if (buff == null) return;
        if (!buff.isBlocksProtected()){
            event.setIssuePresent(PlayerModifyBlockPermissionEvent.Issue.INSIDE_NON_BUILD_RADIUS, false);
            event.setIssuePresent(PlayerModifyBlockPermissionEvent.Issue.NON_PLAYER_PLACED, false);
            event.setIssuePresent(PlayerModifyBlockPermissionEvent.Issue.BLACKLISTED_MATERIAL, false);
        }
    }
}
