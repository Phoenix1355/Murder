package net.phoenix1355.murder;

import net.phoenix1355.murder.commands.arguments.CommandRegistry;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Murder extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        PluginCommand command = getCommand("murder");
        if (command != null)
            command.setExecutor(new CommandRegistry());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
