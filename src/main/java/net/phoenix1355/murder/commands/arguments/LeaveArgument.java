package net.phoenix1355.murder.commands.arguments;

import net.phoenix1355.murder.commands.arguments.BaseArgument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class LeaveArgument extends BaseArgument {
    @Override
    public boolean handleCommand(CommandSender sender, Command command, String[] args, int argIndex) {
        sender.sendMessage("Leaving game...");

        return true;
    }

    @Override
    public String getArgumentString() {
        return "leave";
    }

    @Override
    public String getUsage() {
        return "/mm leave - Leave current game";
    }

    @Override
    public boolean requirePlayer() {
        return true;
    }
}
