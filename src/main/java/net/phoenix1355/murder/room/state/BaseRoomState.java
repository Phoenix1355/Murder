package net.phoenix1355.murder.room.state;

import net.phoenix1355.murder.room.RoomStateManager;
import org.bukkit.entity.Player;

public abstract class BaseRoomState {
    private RoomStateManager _roomStateManager;

    public void attachStateManager(RoomStateManager manager) {
        _roomStateManager = manager;
    }

    protected RoomStateManager getStateManager() {
        return _roomStateManager;
    }

    public abstract void onStart();
    public abstract void onStop();

    /**
     * Lifecycle event for when a player joins the room
     * @param player The player that joined the room
     */
    public void onPlayerJoin(Player player) {
        // Empty implementation
    }

    /**
     * Lifecycle event for when a player leaves the room
     * @param player The player that leaves
     */
    public void onPlayerLeave(Player player) {
        // Empty implementation
    }
}
