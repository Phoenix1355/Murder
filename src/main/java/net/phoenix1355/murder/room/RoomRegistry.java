package net.phoenix1355.murder.room;

import java.util.ArrayList;
import java.util.List;

public class RoomRegistry {
    private static RoomRegistry _instance;

    private List<Room> _rooms = new ArrayList<>();

    private RoomRegistry() {

    }

    public static RoomRegistry getInstance() {
        if (_instance == null)
            _instance = new RoomRegistry();

        return _instance;
    }
}
