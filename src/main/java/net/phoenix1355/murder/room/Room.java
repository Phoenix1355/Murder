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

    public RoomStateManager getStateManager() { return _roomStateManager; }
    public RoomEventHandler getEventHandler() { return _roomEventHandler; }
    public RoomArenaHandler getArenaHandler() { return _roomArenaHandler; }
    public RoomArrowHandler getArrowHandler() { return _roomArrowHandler; }

    public List<User> getUsers() {
        return _users;
    }

    public List<User> getBystanders() {
        return _users
                .stream()
                .filter(user -> user.getRole() == User.Role.BYSTANDER)
                .collect(Collectors.toList());
    }

    public User getRandomBystander() {
        List<User> bystanders = getBystanders();
        return bystanders.get(_random.nextInt(bystanders.size()));
    }

    public List<User> getDetectives() {
        return _users
                .stream()
                .filter(user -> user.getRole() == User.Role.DETECTIVE)
                .collect(Collectors.toList());
    }

    public void showPlayers() {
        JavaPlugin plugin = JavaPlugin.getPlugin(Murder.class);

        for (User user : getUsers()) {
            for (User u : getUsers()) {
                user.getPlayer().showPlayer(plugin, u.getPlayer());
            }
        }
    }

    public void hidePlayers() {
        JavaPlugin plugin = JavaPlugin.getPlugin(Murder.class);

        for (User user : getUsers()) {
            for (User u : getUsers()) {
                if (user != u)
                    user.getPlayer().hidePlayer(plugin, u.getPlayer());
            }
        }
    }

    public void broadcast(String message) {
        for (User user : getUsers()) {
            user.getPlayer().sendMessage(message);
        }
    }

    public User getUser(Player player) {
        for (User user : getUsers()) {
            if (user.is(player))
                return user;
        }

        return null;
    }

    public RoomSettings getSettings() {
        return _settings;
    }

    public User getMurderer() {
        for (User user : getUsers()) {
            if (user.getRole() == User.Role.MURDERER)
                return user;
        }

        return null;
    }

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

    public void dropBow(Location location) {
        World world = location.getWorld();

        if (world == null)
            return;

        ItemStack bow = new ItemStack(Material.BOW, 1);
        _spawnedItems.add(location.getWorld().dropItemNaturally(location, bow));
    }

    public void makeDetective(User user) {
        user.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

        user.setRole(User.Role.DETECTIVE);
        user.addBow();
        user.addArrow();

        _roomArrowHandler.addDetective(user);
    }

    public void makeMurderer(User user) {
        user.playSound(Sound.AMBIENT_CAVE, 1, 1);

        user.setRole(User.Role.MURDERER);
        user.getPlayer().setFoodLevel(20); // Allow sprinting
        user.addMurderWeapon();
    }

    public void makeBystander(User user) {
        user.setRole(User.Role.BYSTANDER);
        user.getPlayer().getInventory().clear();
        user.getPlayer().setFoodLevel(6); // Prevent sprinting
        user.resetClues();

        _roomArrowHandler.removeDetective(user);
    }

    public void addUser(User user) {
        if (!_users.contains(user))
            _users.add(user);
    }

    public void removeUser(User user) {
        _users.remove(user);
    }

    public void broadcastTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        broadcastTitle(null, title, subtitle, fadeIn, stay, fadeOut);
    }

    public void broadcastTitle(User.Role roleFilter, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        for (User user : _users) {
            if (roleFilter == null || (user.getRole() == roleFilter)) {
                user.getPlayer().sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            }
        }
    }
}
