package net.phoenix1355.murder.room;

import net.phoenix1355.murder.Murder;
import net.phoenix1355.murder.user.User;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Room {
    private final Random _random = new Random();
    private final String _roomId;
    private final RoomStateManager _roomStateManager;
    private final RoomEventHandler _roomEventHandler;
    private final RoomArenaHandler _roomArenaHandler;
    private final RoomArrowHandler _roomArrowHandler;
    private final RoomSettings _settings;
    private final List<User> _users = new ArrayList<>();
    private final List<Item> _spawnedItems = new ArrayList<>();

    public Room(String roomId) {
        _roomId = roomId;
        _roomStateManager = new RoomStateManager(this);
        _roomEventHandler = new RoomEventHandler(this);
        _roomArenaHandler = new RoomArenaHandler();
        _roomArrowHandler = new RoomArrowHandler();
        _settings = new RoomSettings(roomId);

        // Set default room state
        _roomStateManager.setState(RoomStateManager.RoomState.WAITING);
    }

    public String getId() {
        return _roomId;
    }

    public RoomStateManager getStateManager() {
        return _roomStateManager;
    }

    public RoomEventHandler getEventHandler() {
        return _roomEventHandler;
    }

    public RoomArenaHandler getArenaHandler() {
        return _roomArenaHandler;
    }

    public RoomArrowHandler getArrowHandler() {
        return _roomArrowHandler;
    }

    /**
     * Fetch a list of all users in the room
     *
     * @return List of users
     */
    public List<User> getUsers() {
        return _users;
    }

    /**
     * Fetch a list of all users who are bystanders
     *
     * @return List of users
     */
    public List<User> getBystanders() {
        return _users.stream()
                     .filter(user -> user.getRole() == User.Role.BYSTANDER)
                     .collect(Collectors.toList());
    }

    /**
     * Fetch a random bystander
     *
     * @return User who is a bystander
     */
    public User getRandomBystander() {
        List<User> bystanders = getBystanders();
        return bystanders.get(_random.nextInt(bystanders.size()));
    }

    /**
     * Fetch a list of all users who are detectives
     *
     * @return List of users
     */
    public List<User> getDetectives() {
        return _users.stream()
                     .filter(user -> user.getRole() == User.Role.DETECTIVE)
                     .collect(Collectors.toList());
    }

    /**
     * Make all players in room visible
     */
    public void showPlayers() {
        JavaPlugin plugin = JavaPlugin.getPlugin(Murder.class);

        for (User user : getUsers()) {
            for (User u : getUsers()) {
                user.getPlayer().showPlayer(plugin, u.getPlayer());
            }
        }
    }

    /**
     * Make all players in room invisible
     */
    public void hidePlayers() {
        JavaPlugin plugin = JavaPlugin.getPlugin(Murder.class);

        for (User user : getUsers()) {
            for (User u : getUsers()) {
                if (user != u)
                    user.getPlayer().hidePlayer(plugin, u.getPlayer());
            }
        }
    }

    /**
     * Broadcast message to all players
     *
     * @param message The message to broadcast
     */
    public void broadcast(String message) {
        for (User user : getUsers()) {
            user.getPlayer().sendMessage(message);
        }
    }

    /**
     * Find a user from a given player
     *
     * @param player The player entity to match the user with
     * @return The found user
     */
    public User getUser(Player player) {
        for (User user : getUsers()) {
            if (user.is(player))
                return user;
        }

        return null;
    }

    /**
     * Get the settings of the room
     *
     * @return Room settings
     */
    public RoomSettings getSettings() {
        return _settings;
    }

    /**
     * Reset the room and clean up arena, players and spawned items
     */
    public void reset() {
        // This will return all players to the lobby and clean up the arena
        getArenaHandler().reset();
        getArrowHandler().reset();

        showPlayers();

        for (User user : getUsers()) {
            RoomUtils.resetUser(user);
            user.getPlayer().teleport(getSettings().getLobbySpawnLocation());
            user.getPlayer().setGameMode(GameMode.ADVENTURE);
            user.hideTrail();
        }

        for (Item item : _spawnedItems) {
            item.remove();
        }
    }

    /**
     * Drop a bow at a given location and saved the item for later cleanup
     *
     * @param location The location of the bow drop
     */
    public void dropBow(Location location) {
        World world = location.getWorld();

        if (world == null)
            return;

        ItemStack bow = new ItemStack(Material.BOW, 1);
        _spawnedItems.add(location.getWorld().dropItemNaturally(location, bow));
    }

    /**
     * Make a user a detective
     *
     * @param user The user to make a detective
     */
    public void makeDetective(User user) {
        user.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

        user.setRole(User.Role.DETECTIVE);
        user.addBow();
        user.addArrow();

        _roomArrowHandler.addDetective(user);
    }

    /**
     * Make a user a murderer
     *
     * @param user The user to make a murderer
     */
    public void makeMurderer(User user) {
        user.playSound(Sound.AMBIENT_CAVE, 1, 1);

        user.setRole(User.Role.MURDERER);
        user.getPlayer().setFoodLevel(20); // Allow sprinting
        user.addMurderWeapon();
    }

    /**
     * Make a user a bystander
     *
     * @param user The user to make a bystander
     */
    public void makeBystander(User user) {
        user.setRole(User.Role.BYSTANDER);
        user.getPlayer().getInventory().clear();
        user.getPlayer().setFoodLevel(6); // Prevent sprinting
        user.resetClues();

        _roomArrowHandler.removeDetective(user);
    }

    /**
     * Adds a user to the room
     *
     * @param user The user to add
     */
    public void addUser(User user) {
        if (!_users.contains(user))
            _users.add(user);
    }

    /**
     * Remove a user from the room
     *
     * @param user The user to remove
     */
    public void removeUser(User user) {
        _users.remove(user);
    }
}
