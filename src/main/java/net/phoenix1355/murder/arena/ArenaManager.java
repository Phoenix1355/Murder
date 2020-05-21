package net.phoenix1355.murder.arena;

import net.phoenix1355.murder.config.ArenaConfigHandler;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArenaManager {
    private static ArenaManager _instance;

    private final Map<String, Arena> _arenas = new HashMap<>();
    private final List<String> _lockedArenas = new ArrayList<>();

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

    public void lockArena(String arenaId) throws ArenaException {
        if (_lockedArenas.contains(arenaId)) {
            throw new ArenaException("Trying to lock an already locked arena");
        }

        _lockedArenas.add(arenaId);
    }

    public void unlockArena(String arenaId) {
        _lockedArenas.remove(arenaId);
    }

    public void createArena(String arenaId) throws ArenaException {
        if (_arenas.get(arenaId) != null) {
            throw new ArenaException(String.format("Arena with id &b%s&e already exists", arenaId));
        }

        _arenas.put(arenaId, new Arena(arenaId));
    }

    public void createArenaSpawn(String arenaId, Location location) throws ArenaException {
        Arena arena = _arenas.get(arenaId);

        if (arena == null) {
            throw new ArenaException(String.format("Arena &b%s&e doesn't exist", arenaId));
        }

        arena.addSpawnLocation(location);
    }

    public Arena getArena(String roomId) {
        return _arenas.get(roomId);
    }

    public Map<String, Arena> getAllArenas() {
        return _arenas;
    }

    public void createArenaClue(String arenaId, Location location) throws ArenaException {
        Arena arena = _arenas.get(arenaId);

        if (arena == null) {
            throw new ArenaException(String.format("Arena &b%s&e doesn't exist", arenaId));
        }

        arena.getClueHandler().addClueLocation(location);
    }

    public void setArenaLimbo(String arenaId, Location location) throws ArenaException {
        Arena arena = _arenas.get(arenaId);

        if (arena == null) {
            throw new ArenaException(String.format("Arena &b%s&e doesn't exist", arenaId));
        }

        arena.setLimboLocation(location);
    }
}
