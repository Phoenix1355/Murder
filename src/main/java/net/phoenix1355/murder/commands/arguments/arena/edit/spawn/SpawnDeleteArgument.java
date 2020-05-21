package net.phoenix1355.murder.commands.arguments.arena.edit.spawn;

import net.phoenix1355.murder.commands.CommandUsage;
import net.phoenix1355.murder.commands.arguments.BaseArgument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SpawnDeleteArgument extends BaseArgument {
    @Override
    public boolean handleCommand(CommandSender sender, Command command, String[] args, int argIndex) {
        return true;
    }

    @Override
    public String getArgumentString() {
        return "delete";
    }

    @Override
    public boolean requirePlayer() {
        return true;
    }

    @Override
    public String requirePermission() {
        return "murder.admin.arena.edit.spawn.delete";
    }

    @Override
    public CommandUsage getUsage() {
        return new CommandUsage("/mm arena edit <arena> spawn delete", "Deletes a spawn at your location");
    }
}
