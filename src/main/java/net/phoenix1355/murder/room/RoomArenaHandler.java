package net.phoenix1355.murder.room;

import net.phoenix1355.murder.arena.Arena;
import net.phoenix1355.murder.arena.ArenaClueLocation;
import net.phoenix1355.murder.arena.ArenaException;
import net.phoenix1355.murder.user.User;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RoomArenaHandler {
    private static final int MAX_CLUES = 2;
    private final Random _random = new Random();

    private Arena _arena;

    private final List<Location> _usedSpawnLocations = new ArrayList<>();

    public void setArena(Arena arena) {
        _arena = arena;
    }

    public void reset() {
        // Clear arena
        if (_arena != null)
            _arena.getClueHandler().cleanup();

        _usedSpawnLocations.clear();

        _arena = null;
    }

    public void spawnUser(User user) throws ArenaException {
        List<Location> spawns = getAvailableSpawnLocations();

        if (spawns.isEmpty()) {
            throw new ArenaException(ChatFormatter.format("There are no spawns left for user %s", user.getPlayer().getName()));
        }

        Location location = spawns.get(_random.nextInt(spawns.size()));
        _usedSpawnLocations.add(location);

        user.getPlayer().teleport(getLocationCenter(location));
    }

    public void spawnRandomClue() {
        if (_arena.getClueHandler().getSpawnedCluesCount() >= MAX_CLUES) {
            return;
        }

        List<ArenaClueLocation> availableLocations = _arena.getClueHandler().getAvailableClueLocations();

        if (!availableLocations.isEmpty()) {
            ArenaClueLocation clueLocation = availableLocations.get(_random.nextInt(availableLocations.size()));
            clueLocation.spawn();
        }
    }

    public Location getWaitingLocation() {
        return getLocationCenter(_arena.getLimboLocation());
    }

    private List<Location> getAvailableSpawnLocations() {
        return _arena.getSpawnLocations()
                .stream()
                .filter(location -> !_usedSpawnLocations.contains(location))
                .collect(Collectors.toList());
    }

    private Location getLocationCenter(Location location) {
        return new Location(
                location.getWorld(),
                location.getBlockX() + 0.5,
                location.getBlockY(),
                location.getBlockZ() + 0.5
        );
    }

    public ArenaClueLocation getClue(Location location) {
        return _arena.getClueHandler().getClueFromLocation(location);
    }
}
