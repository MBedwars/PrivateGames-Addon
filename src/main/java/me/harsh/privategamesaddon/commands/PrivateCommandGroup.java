package me.harsh.privategamesaddon.commands;

import org.mineacademy.fo.command.SimpleCommandGroup;

public class PrivateCommandGroup extends SimpleCommandGroup {
    @Override
    protected void registerSubcommands() {
        registerSubcommand(new PrivateGameCreateCommand(this));
        registerSubcommand(new PrivateGamePartyWarpCommand(this));
    }
}
