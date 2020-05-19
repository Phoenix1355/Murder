package net.phoenix1355.murder.commands;

import net.phoenix1355.murder.commands.arguments.BaseArgumentGroup;
import net.phoenix1355.murder.commands.arguments.JoinArgument;
import net.phoenix1355.murder.commands.arguments.LeaveArgument;
import net.phoenix1355.murder.commands.arguments.ListArgument;
import net.phoenix1355.murder.commands.arguments.arena.ArenaArgumentGroup;
import net.phoenix1355.murder.commands.arguments.room.RoomArgumentGroup;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandRegistry implements CommandExecutor {
    private final MainArgumentGroup _main = new MainArgumentGroup();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelp(sender);
            return true;
        }

        if (_main.handleCommand(sender, command, args, 0)) {
            return true;
        }

        sender.sendMessage("Unknown command. Type /mm help");
        return false;
    }

    private static class MainArgumentGroup extends BaseArgumentGroup {

        public MainArgumentGroup() {
            registerArgument(new JoinArgument());
            registerArgument(new LeaveArgument());
            registerArgument(new ListArgument());
            registerArgument(new RoomArgumentGroup());
            registerArgument(new ArenaArgumentGroup());
        }

        @Override
        public String getArgumentString() {
            return "mm"; // Not used
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("Murder commands:");

        for (CommandUsage usage : _main.getUsages()) {
            sender.sendMessage(ChatFormatter.format("  &b%s&7 - %s", usage.getSignature(), usage.getDescription()));
        }
    }
}
