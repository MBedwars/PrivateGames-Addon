package me.harsh.privategamesaddon.events;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.*;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import de.marcely.bedwars.api.event.arena.RoundStartEvent;
import de.marcely.bedwars.api.event.player.PlayerJoinArenaEvent;
import de.marcely.bedwars.api.event.player.PlayerQuitArenaEvent;
import de.marcely.bedwars.api.event.player.PlayerStatChangeEvent;
import de.marcely.bedwars.api.hook.PartiesHook.Member;
import de.marcely.bedwars.api.hook.PartiesHook.Party;
import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.api.player.PlayerDataAPI;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import me.harsh.privategamesaddon.api.PrivateGameAPI;
import me.harsh.privategamesaddon.api.events.PrivateGameCreateEvent;
import me.harsh.privategamesaddon.api.events.PrivateGameEndEvent;
import me.harsh.privategamesaddon.api.events.PrivateGameStartEvent;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.mineacademy.fo.Common;

import java.util.UUID;

public class PlayerListener implements Listener {

    private final PrivateGameManager manager;

    public PlayerListener(PrivateGameManager manager){
        this.manager = manager;
    }

    @EventHandler
    public void onArenaJoin(PlayerJoinArenaEvent event) {
        final Arena arena = event.getArena();
        final Player player = event.getPlayer();
        final PrivateGameManager manager = Utility.manager;
        final boolean isPrivateArena = manager.isPrivateArena(arena);

        // if (isPrivateArena && event.getCause() == AddPlayerCause.PARTY_SWITCH_ARENA)
        //    event.addIssue(AddPlayerIssue.PLUGIN);

        final AtomicBoolean methodFinished = new AtomicBoolean(false);

        PlayerDataAPI.get().getProperties(player, props -> {
            final boolean privateMode = manager.getPlayerPrivateMode(props);
            final Runnable forbidJoin = () -> {
                if (!isPrivateArena)
                    return;

                // not allowed, send him back where he belongs to
                Common.tell(player,  " "  + Settings.ARENA_IS_PRIVATE);

                if (methodFinished.get())
                    GameAPI.get().sendToHub(player);
                else
                    event.addIssue(AddPlayerIssue.PLUGIN);
            };

            // no need to check, just add him
            if (manager.isJoinEnforced(props)) {
                manager.setJoinEnforced(props, false);
                return;
            }

            manager.getParty(player, member -> {
                if (!player.isOnline())
                    return;

                // not a member
                if (!member.isPresent()) {
                    forbidJoin.run();
                    return;
                }

                // he is not managing the arena
                final Party managingParty = manager.partyMembersMangingMap.get(arena);

                if (managingParty == null) {
                    // nobody is managing the arena yet, take it over
                    if (!privateMode || !member.get().isLeader())
                        return;

                    manager.setPrivateArena(arena, true);
                    Utility.getManager().partyMembersMangingMap.put(arena, member.get().getParty());

                    Bukkit.getServer().getPluginManager().callEvent(new PrivateGameCreateEvent(player, arena));

                } else if (!manager.match(member.get().getParty(), managingParty)) {
                    forbidJoin.run();
                    return;
                }

                // private game
                Utility.doStatsThing(player.getUniqueId());
                Message.build(Settings.PLAYER_JOIN_PRIVATE_GAME)
                    .placeholder("player", managingParty.getLeaders().stream()
                        .map(Member::getUsername)
                        .collect(Collectors.joining(", ")))
                    .send(player);
            });
        });

        methodFinished.set(true);

        /*manager.getPlayerPrivateMode(player, mode -> {
            if (!mode)
                return;

            manager.getParty(player, member -> {
                if (!member.isPresent())
                    return;

                manager.getPrivateArenas().add(arena);
                setupParty(player, arena);

                Utility.doStatsThing(player.getUniqueId());
                Bukkit.getServer().getPluginManager().callEvent(new PrivateGameCreateEvent(player, arena));

                PartiesIParty party = (PartiesIParty) manager.partyMembersMangingMap.get(arena);

                if (party.getParty().getMembers().size() == 1) {
                    Common.tell(player,  " " + Settings.NO_PLAYER_FOUND_IN_PARTY);
                }else {
                    new BukkitRunnable(){

                        @Override
                        public void run() {
                            if (Settings.AUTO_WARP && player.hasPermission(Settings.AUTO_WARP_PERM)){
                                Common.tell(player,  "&a Auto Warping party members...");
                                player.performCommand("bwp warp");
                            }else if (!player.hasPermission(Settings.AUTO_WARP_PERM)){
                                Common.tell(player, Settings.NO_AUTO_WARP_PERM_EROR);
                            }
                        }
                    }.runTaskLater(SimplePlugin.getInstance(), 5);
                }


                if (pafPlayer.getParty().getPlayers().size() == 0){
                    if (!pafPlayer.getPlayer().hasPermission(Settings.PARTY_BYPASS_PERM)){
                        Common.tell(player, Settings.NO_PARTY_ON_CREATE);
                        arena.kickPlayer(player, KickReason.PLUGIN);
                    }
                }
            });
        });*/
    }

    @EventHandler
    public void onPlayerLEAVE(PlayerQuitArenaEvent event){
        final Player player = event.getPlayer();
        final Arena arena = event.getArena();
        final PrivateGameManager manager = Utility.manager;

        if (arena.getStatus() != ArenaStatus.LOBBY || !manager.isPrivateArena(arena))
            return;

        manager.getParty(player, member -> {
            if (!member.isPresent() || !member.get().isLeader())
                return;

            manager.setPrivateArena(arena, false);
        });
    }
    @EventHandler
    public void onPlayerStatGain(PlayerStatChangeEvent event){
        final UUID uuid = event.getStats().getPlayerUUID();
        if (manager.playerStatsList.contains(uuid)){
            event.setCancelled(true);
        }
    }
}
