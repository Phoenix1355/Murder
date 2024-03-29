package net.phoenix1355.murder.room.state;

import net.phoenix1355.murder.Murder;
import net.phoenix1355.murder.arena.ArenaClueLocation;
import net.phoenix1355.murder.room.Room;
import net.phoenix1355.murder.room.RoomStateManager;
import net.phoenix1355.murder.user.User;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class BaseRoomState extends BukkitRunnable {
    private RoomStateManager _roomStateManager;
    private int _timer;

    public BaseRoomState() {

    }

    public void attachStateManager(RoomStateManager manager) {
        _roomStateManager = manager;
    }

    public void start() {
        // Set timer to run every second
        JavaPlugin plugin = JavaPlugin.getPlugin(Murder.class);
        runTaskTimer(plugin, 20L, 20L);
    }

    @Override
    public void run() {
        onUpdate();
    }

    public abstract void onStart();
    public abstract void onUpdate();
    public abstract void onStop();

    /**
     * Event for when a player joins the room
     * @param user The player that joined the room
     */
    public void onUserJoin(User user) {
        // Empty implementation
    }

    /**
     * Event for when a player leaves the room
     * @param user The player that leaves
     */
    public void onUserLeave(User user) {
        // Empty implementation
    }

    /**
     * Event for when a player is killed
     * @param victim The user who is killed
     * @param attacker The user who caused the death
     */
    public void onUserDeath(User victim, User attacker) {
        // Empty implementation
    }

    /**
     * Room event for when player interacts with a clue (clicks it)
     * @param player The player
     * @param clickedBlock The block the player interacted with
     */
    public void onClueInteract(Player player, ArenaClueLocation clickedBlock) {
        // Empty implementation
    }

    protected RoomStateManager getStateManager() {
        return _roomStateManager;
    }

    protected Room getRoom() {
        return _roomStateManager.getRoom();
    }

    protected int getTimer() {
        return _timer;
    }

    protected void setTimer(int timer) {
        _timer = timer;
    }
}
