package me.harsh.privategamesaddon.events;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import de.marcely.bedwars.api.arena.*;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import de.marcely.bedwars.api.event.arena.RoundStartEvent;
import de.marcely.bedwars.api.event.player.PlayerJoinArenaEvent;
import de.marcely.bedwars.api.event.player.PlayerQuitArenaEvent;
import de.marcely.bedwars.api.event.player.PlayerStatChangeEvent;
import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.api.party.PartyManager;
import de.simonsator.partyandfriends.api.party.PlayerParty;
import me.harsh.privategamesaddon.api.PrivateGameAPI;
import me.harsh.privategamesaddon.api.events.PrivateGameCreateEvent;
import me.harsh.privategamesaddon.api.events.PrivateGameEndEvent;
import me.harsh.privategamesaddon.api.events.PrivateGameStartEvent;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.party.BwParty;
import me.harsh.privategamesaddon.party.IParty;
import me.harsh.privategamesaddon.party.PafParty;
import me.harsh.privategamesaddon.party.PartiesIParty;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.util.UUID;

public class PlayerListener implements Listener {
    private final PrivateGameManager manager;
    public PlayerListener(PrivateGameManager manager){
        this.manager = manager;
    }

    @EventHandler
    public void onArenaJoin(PlayerJoinArenaEvent event){
        final Arena arena = event.getArena();
        final Player player = event.getPlayer();
        if (manager.getPrivateArenas().contains(arena)){
            if (event.getCause() == AddPlayerCause.PARTY_SWITCH_ARENA) {
                event.addIssue(AddPlayerIssue.PLUGIN);
            }
            if (Utility.isPfa){
                final OnlinePAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(player);
                if (pafPlayer.getParty() !=  null){
                    final PlayerParty party = PartyManager.getInstance().getParty(pafPlayer);
                    final PafParty pafParty = (PafParty) manager.partyMembersMangingMap.get(arena);
                    if (party == pafParty.getParty()){
                        final String name = party.getLeader().getName();
                        Utility.doStatsThing(player.getUniqueId());
                        Common.tell(player,  " " + Settings.PLAYER_JOIN_PRIVATE_GAME);
                        return;
                    }
                }

            }else if (Utility.isParty){
                final PartyPlayer partyPlayer = Parties.getApi().getPartyPlayer(player.getUniqueId());
                if (partyPlayer.isInParty()){
                    final Party party = Parties.getApi().getParty(partyPlayer.getPartyId());
                    final PartiesIParty p  = (PartiesIParty) manager.partyMembersMangingMap.get(arena);
                    if (party == p.getParty()){
                        final String name = Bukkit.getPlayer(party.getLeader()).getName();
                        Utility.doStatsThing(player.getUniqueId());
                        Common.tell(player,  " " + Settings.PLAYER_JOIN_PRIVATE_GAME.replace("{name}", name));
                        return;
                    }
                }
            }else if (Utility.isBedwarParty){
                final me.harsh.bedwarsparties.party.PartyManager man = me.harsh.bedwarsparties.Utils.Utility.getManager();
                if (!man.isInPartyAsLeader(player.getUniqueId())) return;
                final me.harsh.bedwarsparties.party.Party party = man.getPartyByLeader(player.getUniqueId());
                final BwParty p = (BwParty) manager.partyMembersMangingMap.get(arena);
                if (party.getLeader() == p.getParty().getLeader()){
                    final String name = Bukkit.getPlayer(party.getLeader()).getName();
                    Utility.doStatsThing(player.getUniqueId());
                    Common.tell(player,  " " + Settings.PLAYER_JOIN_PRIVATE_GAME.replace("{name}", name));
                    return;
                }
            }

            if (Utility.isParty){
                final PartiesIParty party = (PartiesIParty) manager.partyMembersMangingMap.get(arena);
                if (party.getParty().getMembers().contains(player.getUniqueId())) return;
                Common.tell(player,  " "  + Settings.ARENA_IS_PRIVATE);
                event.addIssue(AddPlayerIssue.PLUGIN);
            }else if (Utility.isPfa){
                final PafParty party = (PafParty) manager.partyMembersMangingMap.get(arena);
                final OnlinePAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(player);
                if (party.getParty().getPlayers().contains(pafPlayer)) return;
                Common.tell(player,  " " + Settings.ARENA_IS_PRIVATE);
                event.addIssue(AddPlayerIssue.PLUGIN);
            }else if (Utility.isBedwarParty){
                final BwParty party = (BwParty) manager.partyMembersMangingMap.get(arena);
                if (party.getParty().getPlayers().contains(player.getUniqueId())) return;
                Common.tell(player,  " "  + Settings.ARENA_IS_PRIVATE);
                event.addIssue(AddPlayerIssue.PLUGIN);
            }

        }

        if (manager.getPlayerPrivateMode(player)){
            if (Utility.isPfa){
                final OnlinePAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(player);
                if (pafPlayer.getParty() != null){
                    manager.getPrivateArenas().add(arena);
                    setupParty(player, arena);
                    Utility.doStatsThing(player.getUniqueId());
                    Bukkit.getServer().getPluginManager().callEvent(new PrivateGameCreateEvent(player, arena));
                    PafParty party = (PafParty) manager.partyMembersMangingMap.get(arena);
                    if (party.getParty().getPlayers().size() == 0){
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
                }else if (pafPlayer.getParty().getPlayers().size() == 0){
                    if (!pafPlayer.getPlayer().hasPermission(Settings.PARTY_BYPASS_PERM)){
                        Common.tell(player, Settings.NO_PARTY_ON_CREATE);
                        arena.kickPlayer(player, KickReason.PLUGIN);
                    }
                }
            } else if (Utility.isParty) {
                final PartyPlayer partyPlayer = Parties.getApi().getPartyPlayer(player.getUniqueId());
                if (partyPlayer.isInParty()){
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
            }else {
                    if (!player.hasPermission(Settings.PARTY_BYPASS_PERM)){
                        Common.tell(player, Settings.NO_PARTY_ON_CREATE);
                        arena.kickPlayer(player, KickReason.PLUGIN);
                    }
                }
            } else if (Utility.isBedwarParty) {
                final me.harsh.bedwarsparties.party.PartyManager man = me.harsh.bedwarsparties.Utils.Utility.getManager();
                if (man.isInPartyAsLeader(player.getUniqueId())){
                    manager.getPrivateArenas().add(arena);
                    setupParty(player, arena);
                    Utility.doStatsThing(player.getUniqueId());
                    Bukkit.getServer().getPluginManager().callEvent(new PrivateGameCreateEvent(player, arena));
                    BwParty party = (BwParty) manager.partyMembersMangingMap.get(arena);
                    if (party.getParty().getPlayers().size() == 1) {
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
                }else {
                    if (!player.hasPermission(Settings.PARTY_BYPASS_PERM)){
                        Common.tell(player, Settings.NO_PARTY_ON_CREATE);
                        arena.kickPlayer(player, KickReason.PLUGIN);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onArenaEnd(RoundEndEvent event){
        final Arena arena = event.getArena();
        if (manager.getPrivateArenas().contains(arena)){
            Bukkit.getServer().getPluginManager().callEvent(new PrivateGameEndEvent(arena, event.getWinners(), event.getWinnerTeam()));
        }
        for (Player player : arena.getPlayers()) {
            if (manager.playerStatsList.contains(player.getUniqueId()))
                manager.playerStatsList.remove(player.getUniqueId());
            if (PrivateGameAPI.hasPermision(player)){
                manager.setPrivateGameMode(player, false);
            }
        }
        manager.getPrivateArenas().remove(arena);
        if(manager.partyMembersMangingMap.containsKey(arena))
            manager.partyMembersMangingMap.remove(arena);
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitArenaEvent event){
        final Arena arena = event.getArena();
        final Player player = event.getPlayer();
        if (manager.getPrivateArenas().contains(arena)){
            manager.getPrivateArenas().remove(arena);
        }
        if (manager.arenaArenaBuffMap.containsKey(arena)){
            manager.arenaArenaBuffMap.remove(arena);
        }
        if(manager.partyMembersMangingMap.containsKey(arena))
            manager.partyMembersMangingMap.remove(arena);
    }
    @EventHandler
    public void onRoundStart(RoundStartEvent event){
        final Arena arena = event.getArena();
        if (manager.getPrivateArenas().contains(arena)){
            Bukkit.getServer().getPluginManager().callEvent(new PrivateGameStartEvent(arena));
        }
    }

    @EventHandler
    public void onPlayerStatGain(PlayerStatChangeEvent event){
        final UUID uuid = event.getStats().getPlayerUUID();
        if (manager.playerStatsList.contains(uuid)){
            event.setCancelled(true);
        }
    }

    private void setupParty(Player player, Arena arena){
        if (Utility.isParty){
            final PartyPlayer partyPlayer = Parties.getApi().getPartyPlayer(player.getUniqueId());
            if (!partyPlayer.isInParty()) return;
            final Party party = Parties.getApi().getParty(partyPlayer.getPartyId());
            if (party == null) return;
            IParty iParty = new PartiesIParty(arena, player, party);
            Utility.getManager().partyMembersMangingMap.put(arena, iParty);

        }else if (Utility.isPfa){
            final OnlinePAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(player);
            final PlayerParty party = PartyManager.getInstance().getParty(pafPlayer);
            if (party == null) return;
            IParty iParty = new PafParty(arena, player, party);
            Utility.getManager().partyMembersMangingMap.put(arena, iParty);
        }else if (Utility.isBedwarParty){
            final me.harsh.bedwarsparties.party.Party party = me.harsh.bedwarsparties.Utils.Utility.getManager().getPartyByLeader(player.getUniqueId());
            if (party == null) return;
            IParty iParty = new BwParty(arena, party, player);
            Utility.getManager().partyMembersMangingMap.put(arena, iParty);
        }
    }
}
