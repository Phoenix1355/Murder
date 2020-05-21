package net.phoenix1355.murder.arena;

import org.bukkit.Location;

public class ArenaClueLocation {
    private final Location _location;
    private ArenaClue _clue;

    public ArenaClueLocation(Location location) {
        _location = location;
    }

    /**
     * Determines whether this clue as already spawned or not. This is used to sort all the clues and find available
     * arena clues to spawn.
     * @return True if the clue is spawned
     */
    public boolean isSpawned() {
        return _clue != null && _clue.isSpawned();
    }

    /**
     * Compares the block at the given location with this clue block. Used to determine if a block is the clue location
     * @param location The location of the block
     * @return True if the location is this clue location
     */
    public boolean isLocation(Location location) {
        return _location.getBlock().equals(location.getBlock());
    }

    /**
     * Spawns the clue at the location
     */
    public void spawn() {
        _clue = new ArenaClue(this);
        _clue.spawn();
    }

    /**
     * Removes the clue from the location
     */
    public void remove() {
        if (_clue != null)
            _clue.remove();

        _clue = null;
    }

    public Location getLocation() {
        return _location;
    }
}
