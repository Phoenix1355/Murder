package net.phoenix1355.murder.arena;

import net.phoenix1355.murder.config.ArenaConfigHandler;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArenaClueHandler {
    private final Arena _arena;
    private final List<ArenaClueLocation> _clueLocations;

    public ArenaClueHandler(Arena arena) {
        _arena = arena;
        _clueLocations = new ArrayList<>();
    }

    public ArenaClueHandler(Arena arena, List<ArenaClueLocation> clueLocations) {
        _arena = arena;
        _clueLocations = clueLocations;
    }

    /**
     * Create a new clue location. Also saves the newly created clue location to the arena configuration
     * @param location The location of the clue
     */
    public void addClueLocation(Location location) {
        _clueLocations.add(new ArenaClueLocation(location));

        ArenaConfigHandler.getInstance().addArenaClueLocation(_arena.getId(), location);
    }

    /**
     * Get list of all unused clue locations
     * @return List of clue locations
     */
    public List<ArenaClueLocation> getAvailableClueLocations() {
        return _clueLocations
                .stream()
                .filter(l -> !l.isSpawned())
                .collect(Collectors.toList());
    }

    /**
     * Get amount of clues spawned
     * @return Amount of clues spawned
     */
    public int getSpawnedCluesCount() {
        return (int) _clueLocations
                .stream()
                .filter(ArenaClueLocation::isSpawned)
                .count();
    }

    /**
     * Removes all spawned clues for the next round
     */
    public void cleanup() {
        for (ArenaClueLocation clueLocation : _clueLocations) {
            if (clueLocation.isSpawned())
                clueLocation.remove();
        }
    }

    /**
     * Return a clue location from a given location, if the clue is part of this arena
     * @param location The location to search by
     * @return An clue location or null
     */
    public ArenaClueLocation getClueFromLocation(Location location) {
        for (ArenaClueLocation clueLocation : _clueLocations) {
            if (clueLocation.isLocation(location))
                return clueLocation;
        }

        return null;
    }
}
