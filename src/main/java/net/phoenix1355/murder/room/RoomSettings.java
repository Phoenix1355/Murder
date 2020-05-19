package net.phoenix1355.murder.room;

import net.phoenix1355.murder.config.RoomConfigHandler;
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
        return (Location) _configHandler.getRoomSection(_roomId)
                .get(SPAWN_LOCATION_KEY);
    }
    public void setLobbySpawnLocation(Location location) {
        ConfigurationSection section = _configHandler.getRoomSection(_roomId);

        section.set(SPAWN_LOCATION_KEY + ".world", location.getWorld().getName());
        section.set(SPAWN_LOCATION_KEY + ".x", location.getBlockX());
        section.set(SPAWN_LOCATION_KEY + ".y", location.getBlockY());
        section.set(SPAWN_LOCATION_KEY + ".z", location.getBlockZ());

        _configHandler.save();
    }
}
