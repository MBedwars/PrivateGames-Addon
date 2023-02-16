package me.harsh.privategamesaddon.party;

import de.marcely.bedwars.api.arena.Arena;
import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.api.party.PlayerParty;
import me.harsh.bedwarsparties.party.Party;
import org.bukkit.entity.Player;

public class BwParty extends IParty{

    private final Party party;



    public BwParty(Arena arena, Party party, Player player) {
        super(arena, player);
        this.party = party;
    }

    public Party getParty() {
        return party;
    }

}
