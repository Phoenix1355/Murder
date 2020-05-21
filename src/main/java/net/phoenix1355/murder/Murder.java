package net.phoenix1355.murder;

import net.phoenix1355.murder.commands.CommandRegistry;
import net.phoenix1355.murder.config.ArenaConfigHandler;
import net.phoenix1355.murder.config.MainConfigHandler;
import net.phoenix1355.murder.config.RoomConfigHandler;
import net.phoenix1355.murder.room.RoomEvents;
import net.phoenix1355.murder.room.RoomManager;
import net.phoenix1355.murder.utils.Log;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class Murder extends JavaPlugin {

    @Override
    public void onEnable() {
        Log.enable(getLogger());
        Log.setLevel(Level.ALL);

        // Plugin startup logic
        saveDefaultConfig();

        MainConfigHandler.init(this);
        RoomConfigHandler.init(this);
        ArenaConfigHandler.init(this);

        RoomManager.init();

        // Register event listeners
        new RoomEvents(this);

        PluginCommand command = getCommand("murder");
        if (command != null)
            command.setExecutor(new CommandRegistry());
    }

    @Override
    public void onDisable() {
        RoomManager.getInstance().reset();
    }
}
