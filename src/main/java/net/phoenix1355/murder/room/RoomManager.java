package net.phoenix1355.murder.room;

import java.util.HashMap;
import java.util.Map;

public class RoomManager {
    private static RoomManager _instance;
    private Map<String, Room> _rooms = new HashMap<>();

    private RoomManager() {

    }

    public static RoomManager getInstance() {
        if (_instance == null) {
            _instance = new RoomManager();
        }

        return _instance;
    }

    public void createRoom(String roomId) throws RoomException {
        Room room = new Room();

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
