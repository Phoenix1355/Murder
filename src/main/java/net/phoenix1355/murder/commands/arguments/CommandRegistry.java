package net.phoenix1355.murder.commands.arguments;

import net.phoenix1355.murder.commands.arguments.arena.ArenaArgumentGroup;
import net.phoenix1355.murder.commands.arguments.room.RoomArgumentGroup;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandRegistry implements CommandExecutor {
    private final MainArgumentGroup _main = new MainArgumentGroup();

    public CommandRegistry() {
        _main.registerArgument(new JoinArgument());
        _main.registerArgument(new LeaveArgument());
        _main.registerArgument(new ListArgument());
        _main.registerArgument(new RoomArgumentGroup());
        _main.registerArgument(new ArenaArgumentGroup());
    }

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

        @Override
        public String getArgumentString() {
            return "mm"; // Not used
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("Murder commands:");

        for (String usage : _main.getUsages()) {
            sender.sendMessage(String.format("  %s", usage));
        }
    }
}
