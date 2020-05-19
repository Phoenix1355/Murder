package net.phoenix1355.murder.room;

import net.phoenix1355.murder.config.RoomConfigHandler;

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
            throw new RoomException(String.format("Room with id '%s' already exists", roomId));
        }

        _rooms.put(roomId, room);
    }

    public Room getRoom(String roomId) {
        return _rooms.get(roomId);
    }

    public Map<String, Room> getAllRooms() {
        return _rooms;
    }
}
