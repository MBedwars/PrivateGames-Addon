package me.harsh.privategamesaddon.commands;

import me.harsh.privategamesaddon.settings.Settings;
import org.bukkit.command.CommandSender;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.model.SimpleComponent;

import java.util.ArrayList;
import java.util.List;

public class PrivateCommandGroup extends SimpleCommandGroup {
    @Override
    protected void registerSubcommands() {
        registerSubcommand(new PrivateGameCreateCommand(this));
        registerSubcommand(new PrivateGamePartyWarpCommand(this));
    }

    @Override
    protected List<SimpleComponent> getNoParamsHeader(CommandSender sender) {
        final List<String> message = new ArrayList<>();
        message.add("&f");
        message.add(" " + Settings.PREFIX + "&8\u2122");
        message.add("&f");
        message.add(" &7Made By WhoTech &7\u00a9 ");
        message.add("&7 Type &f/" + getLabel() + " private &7to go into private game creation room!");
        message.add("f");


        return Common.convert(message, SimpleComponent::of);
    }
}
