package net.phoenix1355.murder.commands.arguments.arena.edit.clue;

import net.phoenix1355.murder.arena.ArenaException;
import net.phoenix1355.murder.arena.ArenaManager;
import net.phoenix1355.murder.commands.CommandUsage;
import net.phoenix1355.murder.commands.arguments.BaseArgument;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClueCreateArgument extends BaseArgument {
    @Override
    public boolean handleCommand(CommandSender sender, Command command, String[] args, int argIndex) {
        ArenaManager rm = ArenaManager.getInstance();
        Location location = ((Player) sender).getLocation();

        try {
            rm.createArenaClue(args[2], new Location(
                    location.getWorld(),
                    location.getBlockX(),
                    location.getBlockY(),
                    location.getBlockZ()
            ));

            sender.sendMessage(ChatFormatter.format(
                    "&eCreated new clue location for &b%s&e at &b%s, %s, %s",
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
        return "murder.admin.arena.edit.clue.set";
    }

    @Override
    public CommandUsage getUsage() {
        return new CommandUsage("/mm arena edit <arena> clue create", "Creates a new clue spawn at your location");
    }
}
