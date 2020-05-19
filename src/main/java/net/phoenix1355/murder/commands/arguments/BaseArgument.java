package net.phoenix1355.murder.commands.arguments;

import net.phoenix1355.murder.commands.CommandUsage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class BaseArgument {
    public abstract boolean handleCommand(CommandSender sender, Command command, String[] args, int argIndex);
    public abstract String getArgumentString();

    public String requirePermission() {
        return null;
    }

    public boolean requirePlayer() {
        return false;
    }

    protected void sendUsage(CommandSender sender) {
        sender.sendMessage("Correct usage: " + getUsage());
    }

    public CommandUsage getUsage() {
        return null;
    }
}
