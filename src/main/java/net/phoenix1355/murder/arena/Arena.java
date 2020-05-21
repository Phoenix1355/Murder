package net.phoenix1355.murder.arena;

import net.phoenix1355.murder.config.ArenaConfigHandler;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Arena {
    private final ArenaConfigHandler _configHandler;

    private final String _id;
    private final List<Location> _spawnLocations;
    private final ArenaClueHandler _clueHandler;
    private Location _limboLocation;

    /**
     * Used to create a new arena from an id. This also creates a new entry in the configuration for future persistence
     * @param id The arena id
     */
    public Arena(String id) {
        _configHandler = ArenaConfigHandler.getInstance();
        _id = id;

        _spawnLocations = new ArrayList<>();
        _clueHandler = new ArenaClueHandler(this);
        _limboLocation = null;

        _configHandler.createArena(id);
    }

    /**
     * Used to create a new arena from an existing entry in the configuration. Does not save in the config since it's
     * already there
     *
     * @param id The arena id
     * @param spawns List of spawn locations
     * @param clues List of clue locations
     * @param limboLocation The limbo location
     */
    public Arena(String id, List<Location> spawns, List<ArenaClueLocation> clues, Location limboLocation) {
        _configHandler = ArenaConfigHandler.getInstance();
        _id = id;

        _spawnLocations = spawns;
        _clueHandler = new ArenaClueHandler(this, clues);
        _limboLocation = limboLocation;
    }

    /**
     * Getter for the arena id
     * @return The arena id
     */
    public String getId() {
        return _id;
    }

    /**
     * Returns all spawn locations
     * @return List of spawn locations
     */
    public List<Location> getSpawnLocations() {
        return _spawnLocations;
    }
    public void addSpawnLocation(Location location) {
        _spawnLocations.add(location);

        _configHandler.addArenaSpawnLocation(_id, location);
    }

    public ArenaClueHandler getClueHandler() {
        return _clueHandler;
    }

    public Location getLimboLocation() {
        return _limboLocation;
    }

    public void setLimboLocation(Location location) {
        _limboLocation = location;

        _configHandler.setArenaLimboLocation(_id, location);
    }
}
