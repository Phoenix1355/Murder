package net.phoenix1355.murder;

import net.phoenix1355.murder.commands.CommandRegistry;
import net.phoenix1355.murder.config.MainConfigHandler;
import net.phoenix1355.murder.config.RoomConfigHandler;
import net.phoenix1355.murder.room.RoomEvents;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Murder extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        MainConfigHandler.init(this);
        RoomConfigHandler.init(this);

        // Register event listeners
        new RoomEvents(this);

        PluginCommand command = getCommand("murder");
        if (command != null)
            command.setExecutor(new CommandRegistry());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
