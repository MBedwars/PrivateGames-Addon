package me.harsh.privategamesaddon.commands;

import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.earth2me.essentials.libs.configurate.objectmapping.meta.Setting;
import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.remain.Remain;

import java.util.UUID;

public class PrivateGamePartyWarpCommand extends SimpleSubCommand {

    protected PrivateGamePartyWarpCommand(SimpleCommandGroup parent) {
        super(parent, "warp");
    }

    @Override
    protected void onCommand() {
        checkConsole();
        final Player player = getPlayer();
        final Arena arena = GameAPI.get().getArenaByPlayer(player);
        if (arena == null) {
            Common.tell(player, Settings.PREFIX + " &cYou're not in an arena!");

        }
        if (!Utility.getManager().getMode(player)){
            Common.tell(player, Settings.PREFIX + " &cYou're not in private game creation mode!");
        }
        if (!Utility.getManager().privateArenas.contains(arena)){
            Common.tell(player, Settings.PREFIX + "&cYou cannot warp players in a non-private game room!");
        }
        final PartyPlayer partyPlayer = Utility.getPlayer(player);
        if (partyPlayer.isInParty()){
            final Party party = Utility.getParty(player);
            if (party == null)  Common.tell(player, Settings.PREFIX + " &cYou're not in a party!");
            Utility.getManager().partyMembersMangingMap.put(arena, party);
            for (UUID uuid : party.getMembers()){
                final Player p = Utility.getPlayerByUuid(uuid);
                if (p == null) return;
                if (p == player) return;
                Common.tell(player, Settings.PREFIX + "&aWarping " + p.getName());
                arena.addPlayer(player);
            }
        }else {
            Common.tell(player,Settings.PREFIX + "&cSorry you're not in a party to warp players");
        }

    }
}
