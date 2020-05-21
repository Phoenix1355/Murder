package net.phoenix1355.murder.commands.arguments.arena;

import net.phoenix1355.murder.commands.CommandUsage;
import net.phoenix1355.murder.commands.arguments.BaseArgument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ArenaDeleteArgument extends BaseArgument {
    @Override
    public boolean handleCommand(CommandSender sender, Command command, String[] args, int argIndex) {
        return false;
    }

    @Override
    public String getArgumentString() {
        return null;
    }

    @Override
    public String requirePermission() {
        return "murder.admin.arena.delete";
    }

    @Override
    public CommandUsage getUsage() {
        return new CommandUsage("/mm arena delete <arena>", "Deletes a given arena");
    }
}
