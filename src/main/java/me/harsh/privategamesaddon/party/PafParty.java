package me.harsh.privategamesaddon.party;

import de.marcely.bedwars.api.arena.Arena;

import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.api.party.PartyManager;
import de.simonsator.partyandfriends.api.party.PlayerParty;
import org.bukkit.entity.Player;

public class PafParty extends IParty{

    private final OnlinePAFPlayer player;
    private final PlayerParty party;
    private final OnlinePAFPlayer leader;



    public PafParty(Arena arena, Player player, PlayerParty party) {
        super(arena, player);
        this.player = PAFPlayerManager.getInstance().getPlayer(player);
        this.party = party;
        this.leader = party.getLeader();
    }

    public PlayerParty getParty() {
        return party;
    }

    public OnlinePAFPlayer getLeader() {
        return leader;
    }

    public OnlinePAFPlayer getPafPlayer(){
        return player;
    }

}
