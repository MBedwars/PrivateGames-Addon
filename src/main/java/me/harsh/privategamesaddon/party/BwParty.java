package me.harsh.privategamesaddon.party;

import de.marcely.bedwars.api.arena.Arena;

import me.harsh.bedwarsparties.party.PartyData;
import org.bukkit.entity.Player;

public class BwParty extends IParty{

    private final PartyData party;



    public BwParty(Arena arena, PartyData party, Player player) {
        super(arena, player);
        this.party = party;
    }

    public PartyData getParty() {
        return party;
    }

}
