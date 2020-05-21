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

    public void addClueLocation(Location location) {
        _clueLocations.add(new ArenaClueLocation(location));

        ArenaConfigHandler.getInstance().addArenaClueLocation(_arena.getId(), location);
    }

    public List<ArenaClueLocation> getClueLocations() {
        return _clueLocations;
    }

    public List<ArenaClueLocation> getAvailableClueLocations() {
        return _clueLocations
                .stream()
                .filter(l -> !l.isSpawned())
                .collect(Collectors.toList());
    }

    public int getSpawnedCluesCount() {
        return (int) _clueLocations
                .stream()
                .filter(ArenaClueLocation::isSpawned)
                .count();
    }

    public boolean hasClue(Location location) {
        for (ArenaClueLocation clueLocation : _clueLocations) {
            if (clueLocation.isLocation(location))
                return true;
        }

        return false;
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

    public ArenaClueLocation getClueFromLocation(Location location) {
        for (ArenaClueLocation clueLocation : _clueLocations) {
            if (clueLocation.isLocation(location))
                return clueLocation;
        }

        return null;
    }
}
