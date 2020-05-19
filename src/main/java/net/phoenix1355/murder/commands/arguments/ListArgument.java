package net.phoenix1355.murder.commands.arguments;

import net.phoenix1355.murder.commands.CommandUsage;
import net.phoenix1355.murder.room.Room;
import net.phoenix1355.murder.room.RoomManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class ListArgument extends BaseArgument {
    @Override
    public boolean handleCommand(CommandSender sender, Command command, String[] args, int argIndex) {
        if (args.length != 1) {
            sendUsage(sender);
            return false;
        }

        RoomManager rm = RoomManager.getInstance();

        if (rm.getAllRooms().isEmpty()) {
            sender.sendMessage("No rooms created yet");
            return true;
        }

        sender.sendMessage("List of Rooms:");
        for (Map.Entry<String, Room> room : RoomManager.getInstance().getAllRooms().entrySet()) {
            sender.sendMessage(String.format("  %s", room.getKey()));
        }

        return true;
    }

    @Override
    public String getArgumentString() {
        return "list";
    }

    @Override
    public CommandUsage getUsage() {
        return new CommandUsage("/mm list", "List all rooms");
    }
}
