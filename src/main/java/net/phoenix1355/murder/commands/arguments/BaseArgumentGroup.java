package net.phoenix1355.murder.commands.arguments;

import net.phoenix1355.murder.commands.CommandUsage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseArgumentGroup extends BaseArgument {
    private final Map<String, BaseArgument> _argumentMap = new HashMap<>();

    public boolean handleCommand(CommandSender sender, Command command, String[] args, int argIndex) {
        BaseArgument argumentHandler = _argumentMap.get(args[argIndex]);

        if (argumentHandler != null) {
            if (argumentHandler.requirePermission() != null
                    && sender.hasPermission(argumentHandler.requirePermission())) {
                sender.sendMessage("You do not have permission to run this command");
                return false;
            }

            if (argumentHandler.requirePlayer() && !(sender instanceof Player)) {
                sender.sendMessage("This command can only be run by players");
                return false;
            }

            return argumentHandler.handleCommand(sender, command, args, argIndex + 1);
        }

        return false;
    }

    /**
     * Gathers a list of all the command arguments and their nested arguments along with the usage of each argument
     * @return List of command arguments usage
     */
    public List<CommandUsage> getUsages() {
        List<CommandUsage> usages = new ArrayList<>();

        for (Map.Entry<String, BaseArgument> arg : _argumentMap.entrySet()) {
            BaseArgument argument = arg.getValue();

            if (argument instanceof BaseArgumentGroup) {
                usages.addAll(
                        ((BaseArgumentGroup) argument).getUsages()
                );
            } else {
                CommandUsage usage = argument.getUsage();
                if (usage != null)
                    usages.add(usage);
            }
        }

        return usages;
    }

    /**
     * Register a command argument. Uses the {@link BaseArgument#getArgumentString()} to map the argument to a string
     * key in the internal argument map.
     *
     * @param argument The command argument to register
     */
    protected void registerArgument(BaseArgument argument) {
        if (argument == this) {
            throw new RuntimeException("Can't register instance of self. Will cause stack overflow...");
        }

        _argumentMap.put(argument.getArgumentString(), argument);
    }
}
