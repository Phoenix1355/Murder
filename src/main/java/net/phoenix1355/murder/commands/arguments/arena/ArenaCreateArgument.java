package net.phoenix1355.murder.commands.arguments.arena;

import net.phoenix1355.murder.arena.ArenaException;
import net.phoenix1355.murder.arena.ArenaManager;
import net.phoenix1355.murder.commands.CommandUsage;
import net.phoenix1355.murder.commands.arguments.BaseArgument;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ArenaCreateArgument extends BaseArgument {
    @Override
    public boolean handleCommand(CommandSender sender, Command command, String[] args, int argIndex) {
        if (args.length != 3) {
            sendUsage(sender);
            return false;
        }

        ArenaManager am = ArenaManager.getInstance();

        try {
            am.createArena(args[2].toLowerCase());
            sender.sendMessage(ChatFormatter.format("Arena &b%s&e has been created", args[2]));
        } catch (ArenaException e) {
            sender.sendMessage(ChatFormatter.format(e.getMessage()));
        }

        return true;
    }

    @Override
    public String getArgumentString() {
        return "create";
    }

    @Override
    public String requirePermission() {
        return "murder.admin.arena.create";
    }

    @Override
    public CommandUsage getUsage() {
        return new CommandUsage("/mm arena create <arena>", "Creates a new arena");
    }
}
