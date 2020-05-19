package net.phoenix1355.murder.config;

import net.phoenix1355.murder.room.Room;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

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


        return rooms;
    }

    private Room loadRoom(String roomId) {
        Room room = new Room(roomId);

        return room;
    }

    public ConfigurationSection getRoomSection(String roomId) {
        String path = String.format("rooms.%s", roomId);

        if (!getConfig().isSet(path)) {
            getConfig().createSection(path);
        }

        return getConfig().getConfigurationSection(path);
    }
}
