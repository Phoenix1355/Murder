package net.phoenix1355.murder.commands.arguments.room.edit;

import net.phoenix1355.murder.commands.arguments.BaseArgument;
import net.phoenix1355.murder.room.Room;
import net.phoenix1355.murder.room.RoomManager;
import net.phoenix1355.murder.room.RoomSettings;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RoomEditSpawnArgument extends BaseArgument {
    @Override
    public boolean handleCommand(CommandSender sender, Command command, String[] args, int argIndex) {
        sender.sendMessage("Args:");
        for (String s : args) {
            sender.sendMessage("  " + s);
        }
        RoomManager rm = RoomManager.getInstance();
        Room room = rm.getRoom(args[2]);

        if (room == null) {
            sender.sendMessage(String.format("The room '%s' doesn't exist", args[2]));
            return true;
        }

        RoomSettings settings = room.getSettings();
        Location location = ((Player) sender).getLocation();

        settings.setLobbySpawnLocation(location);

        sender.sendMessage(ChatFormatter.format(
                        "&eSet the spawn for &b%s&e at &b%s, %s, %s",
                        args[2], location.getBlockX(), location.getBlockY(), location.getBlockZ()
        ));


        return true;
    }

    @Override
    public String getArgumentString() {
        return "spawn";
    }

    @Override
    public boolean requirePlayer() {
        return true;
    }
}
