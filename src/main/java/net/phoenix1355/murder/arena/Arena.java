package net.phoenix1355.murder.arena;

import net.phoenix1355.murder.config.ArenaConfigHandler;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Arena {
    private final ArenaConfigHandler _configHandler;

    private final String _id;
    private final List<Location> _spawnLocations;
    private final List<Location> _clueLocations;

    /**
     * Used to create a new arena from an id. This also creates a new entry in the configuration for future persistence
     * @param id
     */
    public Arena(String id) {
        _configHandler = ArenaConfigHandler.getInstance();
        _id = id;

        _spawnLocations = new ArrayList<>();
        _clueLocations = new ArrayList<>();

        _configHandler.createArena(id);
    }

    /**
     * Used to create a new arena from an existing entry in the configuration. Does not save in the config since it's
     * already there
     * @param id
     * @param spawns
     * @param clues
     */
    public Arena(String id, List<Location> spawns, List<Location> clues) {
        _configHandler = ArenaConfigHandler.getInstance();
        _id = id;

        _spawnLocations = spawns;
        _clueLocations = clues;
    }

    public String getId() {
        return _id;
    }

    public List<Location> getSpawnLocations() {
        return _spawnLocations;
    }
    public void addSpawnLocation(Location location) {
        _spawnLocations.add(location);

        _configHandler.addArenaSpawn(_id, location);
    }

    public List<Location> getClueLocations() {
        return _clueLocations;
    }
    public void addClueLocation(Location location) {
        _clueLocations.add(location);
    }
}
