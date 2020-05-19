package net.phoenix1355.murder.commands.arguments.arena.edit.spawn;

import net.phoenix1355.murder.arena.ArenaException;
import net.phoenix1355.murder.arena.ArenaManager;
import net.phoenix1355.murder.commands.CommandUsage;
import net.phoenix1355.murder.commands.arguments.BaseArgument;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCreateArgument extends BaseArgument {
    @Override
    public boolean handleCommand(CommandSender sender, Command command, String[] args, int argIndex) {
        ArenaManager rm = ArenaManager.getInstance();
        Location location = ((Player) sender).getLocation();

        try {
            rm.createArenaSpawn(args[2], location);

            sender.sendMessage(ChatFormatter.format(
                    "&eCreated new spawn for &b%s&e at &b%s, %s, %s",
                    args[2], location.getBlockX(), location.getBlockY(), location.getBlockZ()
            ));
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
    public boolean requirePlayer() {
        return true;
    }

    @Override
    public String requirePermission() {
        return "murder.admin.arena.edit.spawn.create";
    }

    @Override
    public CommandUsage getUsage() {
        return new CommandUsage("/mm arena edit <arena> spawn create", "Creates a new arena spawn at your location");
    }
}
