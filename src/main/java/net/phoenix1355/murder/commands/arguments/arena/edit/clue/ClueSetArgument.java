package net.phoenix1355.murder.commands.arguments.arena.edit.clue;

import net.phoenix1355.murder.commands.CommandUsage;
import net.phoenix1355.murder.commands.arguments.BaseArgument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ClueSetArgument extends BaseArgument {
    @Override
    public boolean handleCommand(CommandSender sender, Command command, String[] args, int argIndex) {
        return false;
    }

    @Override
    public String getArgumentString() {
        return "set";
    }

    @Override
    public boolean requirePlayer() {
        return true;
    }

    @Override
    public String requirePermission() {
        return "murder.admin.arena.edit.clue.set";
    }

    @Override
    public CommandUsage getUsage() {
        return new CommandUsage("/mm arena edit <arena> clue set", "Creates a new clue spawn at your location");
    }
}
