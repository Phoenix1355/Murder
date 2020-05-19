package net.phoenix1355.murder.config;

import net.phoenix1355.murder.room.Room;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RoomConfigHandler extends BaseConfigHandler {
    public static final String FILE = "rooms.yml";

    private static RoomConfigHandler _instance;

    private RoomConfigHandler(JavaPlugin plugin) {
        super(plugin, FILE);
    }

    public static void init(JavaPlugin plugin) {
        _instance = new RoomConfigHandler(plugin);
    }

    public static RoomConfigHandler getInstance() {
        return _instance;
    }

    public Map<String, Room> loadRooms() {
        Map<String, Room> rooms = new HashMap<>();

        // Load individual room
        ConfigurationSection section = getRoomsSection();
        Set<String> keys = section.getKeys(false);

        if (keys.isEmpty()) {
            return rooms;
        }

        for (String roomId : keys) {
            rooms.put(roomId, new Room(roomId));
        }

        return rooms;
    }

    public ConfigurationSection getRoomsSection() {
        String path = "rooms";

        if (!getConfig().isSet(path)) {
            getConfig().createSection(path);
        }

        return getConfig().getConfigurationSection(path);
    }

    public ConfigurationSection getRoomSection(String roomId) {
        ConfigurationSection roomsSection = getRoomsSection();

        if (!roomsSection.isSet(roomId)) {
            roomsSection.createSection(roomId);
        }

        return getRoomsSection().getConfigurationSection(roomId);
    }
}
