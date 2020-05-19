package net.phoenix1355.murder.room;

import net.phoenix1355.murder.config.RoomConfigHandler;
import org.bukkit.entity.Player;

import java.util.Map;

public class RoomManager {
    private static RoomManager _instance;

    private final Map<String, Room> _rooms;

    private RoomManager() {
        _rooms = RoomConfigHandler.getInstance().loadRooms();
    }

    public static RoomManager getInstance() {
        if (_instance == null)
            _instance = new RoomManager();

        return _instance;
    }

    public void createRoom(String roomId) throws RoomException {
        Room room = new Room(roomId);

        if (_rooms.get(roomId) != null) {
            throw new RoomException(String.format("Room with id &b%s&e already exists", roomId));
        }

        _rooms.put(roomId, room);
    }

    public Room getRoom(String roomId) {
        return _rooms.get(roomId);
    }

    public Room getRoomFromPlayer(Player player) {
        for (Map.Entry<String, Room> room : _rooms.entrySet()) {
            if (room.getValue().getUser(player) != null) {
                return room.getValue();
            }
        }

        return null;
    }

    public Map<String, Room> getAllRooms() {
        return _rooms;
    }
}
