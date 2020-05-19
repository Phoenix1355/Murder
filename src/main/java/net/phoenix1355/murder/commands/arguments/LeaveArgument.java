package net.phoenix1355.murder.commands.arguments;

import net.phoenix1355.murder.commands.CommandUsage;
import net.phoenix1355.murder.room.Room;
import net.phoenix1355.murder.room.RoomManager;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveArgument extends BaseArgument {
    @Override
    public boolean handleCommand(CommandSender sender, Command command, String[] args, int argIndex) {
        RoomManager rm = RoomManager.getInstance();
        Room room = rm.getRoomFromPlayer((Player) sender);

        if (room != null) {
            sender.sendMessage(ChatFormatter.format("Leaving room &b%s", room.getId()));
            room.leave((Player) sender);
            return true;
        }

        sender.sendMessage(ChatFormatter.format("You are currently not in a room"));

        return true;
    }

    @Override
    public String getArgumentString() {
        return "leave";
    }

    @Override
    public CommandUsage getUsage() {
        return new CommandUsage("/mm leave", "Leave current game");
    }

    @Override
    public boolean requirePlayer() {
        return true;
    }
}
