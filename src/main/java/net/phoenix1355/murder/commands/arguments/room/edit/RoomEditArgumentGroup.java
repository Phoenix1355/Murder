package net.phoenix1355.murder.commands.arguments.room.edit;

import net.phoenix1355.murder.commands.arguments.BaseArgumentGroup;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RoomEditArgumentGroup extends BaseArgumentGroup {
    public RoomEditArgumentGroup() {
        registerArgument(new RoomEditSpawnArgument());
    }

    @Override
    public boolean handleCommand(CommandSender sender, Command command, String[] args, int argIndex) {
        if (args.length < 4) {
            sendUsage(sender);
            return false;
        }

        return super.handleCommand(sender, command, args, argIndex + 1);
    }

    @Override
    public String getArgumentString() {
        return "edit";
    }

    @Override
    public String requirePermission() {
        return "murder.admin.room.edit.*";
    }
}
