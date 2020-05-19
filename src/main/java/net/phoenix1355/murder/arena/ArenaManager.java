package net.phoenix1355.murder.arena;

import net.phoenix1355.murder.config.ArenaConfigHandler;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class ArenaManager {
    private static ArenaManager _instance;

    private final Map<String, Arena> _arenas = new HashMap<>();

    private ArenaManager() {
        Map<String, Arena> storedArenas = ArenaConfigHandler.getInstance().loadArenas();
        if (storedArenas != null)
            _arenas.putAll(storedArenas);
    }

    public static ArenaManager getInstance() {
        if (_instance == null)
            _instance = new ArenaManager();

        return _instance;
    }

    public void createArena(String roomId) throws ArenaException {
        if (_arenas.get(roomId) != null) {
            throw new ArenaException(String.format("Arena with id &b%s&e already exists", roomId));
        }

        _arenas.put(roomId, new Arena(roomId));
    }

    public void createArenaSpawn(String roomId, Location location) throws ArenaException {
        Arena arena = _arenas.get(roomId);

        if (arena == null) {
            throw new ArenaException(String.format("Arena &b%s&e doesn't exist", roomId));
        }

        arena.addSpawnLocation(location);
    }

    public Arena getArena(String roomId) {
        return _arenas.get(roomId);
    }

    public Map<String, Arena> getAllArenas() {
        return _arenas;
    }
}
