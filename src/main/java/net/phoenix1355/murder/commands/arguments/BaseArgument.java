package net.phoenix1355.murder.commands.arguments;

import net.phoenix1355.murder.commands.CommandUsage;
import net.phoenix1355.murder.utils.ChatFormatter;
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
        if (getUsage() != null) {
            sender.sendMessage(ChatFormatter.format("Usage:"));
            getUsage().send(sender);
        }
    }

    public CommandUsage getUsage() {
        return null;
    }
}
