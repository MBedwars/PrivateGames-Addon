package me.harsh.privategamesaddon.party;


import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import de.marcely.bedwars.api.arena.Arena;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PartiesIParty extends IParty{

    private final PartyPlayer partyPlayer;
    private final Party party;
    private final UUID leader;


    public PartiesIParty(Arena arena, Player player, Party party) {
        super(arena, player);
        this.partyPlayer = Parties.getApi().getPartyPlayer(player.getUniqueId());
        this.party = party;
        leader = this.party.getLeader();
    }

    public Party getParty() {
        if (!this.partyPlayer.isInParty()) return null;
        return party;
    }

    public PartyPlayer getPartyPlayer() {
        return partyPlayer;
    }

    public UUID getLeader() {
        if (this.party == null) return null;
        return leader;
    }
}
