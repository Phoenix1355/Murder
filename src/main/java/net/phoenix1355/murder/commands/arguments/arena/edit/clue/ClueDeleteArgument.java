package net.phoenix1355.murder.commands.arguments.arena.edit.clue;

import net.phoenix1355.murder.commands.arguments.BaseArgument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ClueDeleteArgument extends BaseArgument {
    @Override
    public boolean handleCommand(CommandSender sender, Command command, String[] args, int argIndex) {
        return true;
    }

    @Override
    public String getArgumentString() {
        return "delete";
    }
}
