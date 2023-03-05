package me.harsh.privategamesaddon.managers;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.player.PlayerDataAPI;
import de.marcely.bedwars.api.player.PlayerProperties;
import lombok.Getter;
import me.harsh.privategamesaddon.buffs.ArenaBuff;
import me.harsh.privategamesaddon.party.IParty;
import me.harsh.privategamesaddon.settings.Settings;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.StrictMap;

import javax.security.auth.callback.Callback;
import java.util.*;

public class PrivateGameManager {

    private final String PRIVATE = "private";
    public final Map<Arena, IParty> partyMembersMangingMap = new HashMap<>();
    public final StrictMap<Arena, ArenaBuff> arenaArenaBuffMap = new StrictMap<>();
    public final List<UUID> playerStatsList = new ArrayList<>();
    @Getter
    public final List<Arena> privateArenas = new ArrayList<>();

    // Old system
//    public Boolean checkPlayer( Player player){
//        return privateGameMode.containsKey(player.getUniqueId());
//    }
//
//    public Boolean getMode( Player player){
//        return privateGameMode.get(player.getUniqueId());
//    }
//
//    public void setMode( Player player, Boolean bol){
//        privateGameMode.remove(player.getUniqueId());
//        addPlayer(player, bol);
//    }

//    public Boolean isInPrivateGameMap(Player player){
//        AtomicBoolean bol = new AtomicBoolean(true);
//        PlayerDataAPI.get().getProperties(player.getUniqueId(), playerProperties -> {
//            if (playerProperties.get(PRIVATE).toString() == null){
//                bol.set(false);
//            }
//        });
//        return bol.get();
//    }
//    public void setPrivateGameMode(Player player, Boolean mode){
//        PlayerDataAPI.get().getProperties(player.getUniqueId(), playerProperties -> {
//            playerProperties.set(PRIVATE, mode.toString());
//            if (mode)
//                Common.tell(player,  " " + Settings.PRIVATE_GAME_MODE);
//            else
//                Common.tell(player,  " " + Settings.NORMAL_MODE);
//        });
//    }
//    public Boolean getPrivateGameMode(Player player){
//        AtomicBoolean mode = new AtomicBoolean(false);
//        PlayerDataAPI.get().getProperties(player.getUniqueId(), playerProperties -> {
//            String pga = String.valueOf(playerProperties.get(PRIVATE));
//            if (pga.equalsIgnoreCase("true"))
//                mode.set(true);
//            else if(pga.equalsIgnoreCase("false"))
//                mode.set(false);
//        });
//        return mode.get();
//    }
//
//
//
//    public void addPlayer( Player player, Boolean bol){
//        if (bol)
//            Common.tell(player,  " " + Settings.PRIVATE_GAME_MODE);
//        else
//            Common.tell(player,  " " + Settings.NORMAL_MODE);
//        privateGameMode.put(player.getUniqueId(), bol);
//    }
    public boolean getPlayerPrivateMode(Player player){
        Common.log("============================================================");
        Common.log("           M E S S A G E  F R O M  #getPlayerPrivateMode    ");
        Common.log("============================================================");
        Common.log("Value found for {p}:- ".replace("{p}", player.getName()) + " " + getValue(player));
        if (!getValue(player).isPresent()) return false;
        final Optional<String> value = getValue(player);
        if(!value.isPresent()){
            addPlayerToPrivateGameMap(player);
            return true;
        } else {
            return Boolean.getBoolean(value.get());
        }
    }

    public void setPrivateGameMode(Player player, boolean mode){
        final Optional<PlayerProperties> prop = optionalPlayerProperties(player);
        if (!prop.isPresent())return;
        if (!(getValue(player).isPresent())) {
            addPlayerToPrivateGameMap(player);
            return;
        }
        prop.get().replace(PRIVATE, String.valueOf(mode));
        if (mode)
            Common.tell(player,  " " + Settings.PRIVATE_GAME_MODE);
        else
            Common.tell(player,  " " + Settings.NORMAL_MODE);
    }

    public void addPlayerToPrivateGameMap(Player player){
        addPlayerToPrivateGameMap(player, true);
    }
    private void addPlayerToPrivateGameMap(Player player, boolean bool){
        final Optional<PlayerProperties> prop = optionalPlayerProperties(player);
        final Optional<String> value = getValue(player);
        if (value.isPresent()) return;
        if (!prop.isPresent()) return;
        prop.get().set(PRIVATE, String.valueOf(bool));
        if (bool)
            Common.tell(player,  " " + Settings.PRIVATE_GAME_MODE);
        else
            Common.tell(player,  " " + Settings.NORMAL_MODE);
    }

    private Optional<String> getValue(Player player){
        final Optional<PlayerProperties> optionalProperties = PlayerDataAPI.get().getPropertiesNow(player);
        if(!optionalProperties.isPresent())
            return Optional.empty();
        return optionalProperties.get().get(PRIVATE);
    }
    private Optional<PlayerProperties> optionalPlayerProperties(Player player){
        return PlayerDataAPI.get().getPropertiesNow(player);
    }

}
