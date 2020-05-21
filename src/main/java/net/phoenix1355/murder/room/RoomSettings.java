package net.phoenix1355.murder.room;

import net.phoenix1355.murder.Murder;
import net.phoenix1355.murder.config.RoomConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class RoomSettings {
    private static final String SPAWN_LOCATION_KEY = "spawn";

    private final RoomConfigHandler _configHandler;
    private final String _roomId;

    public RoomSettings(String roomId) {
        _configHandler = RoomConfigHandler.getInstance();
        _roomId = roomId;
    }

    public Location getLobbySpawnLocation() {
        ConfigurationSection section = _configHandler.getRoomSection(_roomId);

        String worldName = section.getString(SPAWN_LOCATION_KEY + ".world");

        return new Location(
                Bukkit.getWorld(worldName != null ? worldName : "world"),
                section.getDouble(SPAWN_LOCATION_KEY + ".x"),
                section.getDouble(SPAWN_LOCATION_KEY + ".y"),
                section.getDouble(SPAWN_LOCATION_KEY + ".z")
        );
    }

    public void setLobbySpawnLocation(Location location) {
        ConfigurationSection section = _configHandler.getRoomSection(_roomId);

        section.set(SPAWN_LOCATION_KEY + ".world", location.getWorld().getName());
        section.set(SPAWN_LOCATION_KEY + ".x", location.getBlockX() + 0.5f);
        section.set(SPAWN_LOCATION_KEY + ".y", location.getBlockY());
        section.set(SPAWN_LOCATION_KEY + ".z", location.getBlockZ() + 0.5f);

        _configHandler.save();
    }
}
