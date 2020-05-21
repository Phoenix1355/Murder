package net.phoenix1355.murder.commands;

import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.command.CommandSender;

public class CommandUsage {
    private final String _signature;
    private final String _description;

    public CommandUsage(String signature, String description) {
        _signature = signature;
        _description = description;
    }

    String getSignature() {
        return _signature;
    }

    String getDescription() {
        return _description;
    }

    public void send(CommandSender sender) {
        sender.sendMessage(ChatFormatter.format("  &b%s&7 - %s", getSignature(), getDescription()));
    }
}
