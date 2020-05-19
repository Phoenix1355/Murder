package net.phoenix1355.murder.commands.arguments;

import net.phoenix1355.murder.commands.CommandUsage;
import net.phoenix1355.murder.commands.arguments.BaseArgument;
import net.phoenix1355.murder.room.Room;
import net.phoenix1355.murder.room.RoomException;
import net.phoenix1355.murder.room.RoomManager;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinArgument extends BaseArgument {
    @Override
    public boolean handleCommand(CommandSender sender, Command command, String[] args, int argIndex) {
        if (args.length != 2) {
            sendUsage(sender);
            return false;
        }

        RoomManager rm = RoomManager.getInstance();
        Room room = rm.getRoom(args[1]);

        if (room == null) {
            sender.sendMessage(ChatFormatter.format("The room %s doesn't exist", args[1]));
            return true;
        }

        if (rm.getRoomFromPlayer((Player) sender) != null) {
            sender.sendMessage(ChatFormatter.format("You are already in a room. Use &b/mm leave&e to leave, before joining another room"));
            return true;
        }

        try {
            room.join((Player) sender);
        } catch (RoomException e) {
            sender.sendMessage(ChatFormatter.format("Unexpected error: &c%s", e.getMessage()));
            return true;
        }

        return true;
    }

    @Override
    public String getArgumentString() {
        return "join";
    }

    @Override
    public CommandUsage getUsage() {
        return new CommandUsage("/mm join <room>", "Join a game room");
    }



    @Override
    public boolean requirePlayer() {
        return true;
    }
}
