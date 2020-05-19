package net.phoenix1355.murder.commands.arguments.room;

import net.phoenix1355.murder.commands.CommandUsage;
import net.phoenix1355.murder.commands.arguments.BaseArgument;
import net.phoenix1355.murder.room.RoomException;
import net.phoenix1355.murder.room.RoomManager;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RoomCreateArgument extends BaseArgument {

    @Override
    public boolean handleCommand(CommandSender sender, Command command, String[] args, int argIndex) {
        if (args.length != 3) {
            sendUsage(sender);
            return false;
        }

        RoomManager rm = RoomManager.getInstance();

        try {
            rm.createRoom(args[2].toLowerCase());
            sender.sendMessage(ChatFormatter.format("Room &b%s&e has been created", args[2]));
        } catch (RoomException e) {
            sender.sendMessage(e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public String getArgumentString() {
        return "create";
    }

    @Override
    public CommandUsage getUsage() {
        return new CommandUsage("/mm room create <name>", "Creates a new room");
    }

}
