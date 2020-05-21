package net.phoenix1355.murder.commands.arguments.arena.edit;

import net.phoenix1355.murder.commands.CommandUsage;
import net.phoenix1355.murder.commands.arguments.BaseArgumentGroup;
import net.phoenix1355.murder.commands.arguments.arena.edit.clue.ArenaClueArgumentGroup;
import net.phoenix1355.murder.commands.arguments.arena.edit.limbo.ArenaLimboArgumentGroup;
import net.phoenix1355.murder.commands.arguments.arena.edit.spawn.ArenaSpawnArgumentGroup;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ArenaEditArgumentGroup extends BaseArgumentGroup {
    public ArenaEditArgumentGroup() {
        registerArgument(new ArenaSpawnArgumentGroup());
        registerArgument(new ArenaClueArgumentGroup());
        registerArgument(new ArenaLimboArgumentGroup());
    }

    @Override
    public boolean handleCommand(CommandSender sender, Command command, String[] args, int argIndex) {
        if (args.length < 4) {
            sendUsage(sender);
            return true;
        }

        return super.handleCommand(sender, command, args, argIndex + 1);
    }

    @Override
    public String getArgumentString() {
        return "edit";
    }

    @Override
    public String requirePermission() {
        return "murder.admin.arena.edit";
    }

    @Override
    public CommandUsage getUsage() {
        return new CommandUsage("/mm arena edit <arena> [action]", "Edit an arena");
    }
}
