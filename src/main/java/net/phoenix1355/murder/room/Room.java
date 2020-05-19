package net.phoenix1355.murder.room;

import net.phoenix1355.murder.Murder;
import net.phoenix1355.murder.user.User;
import org.bukkit.entity.Mule;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Room {
    private final Random _random = new Random();
    private final String _roomId;
    private final RoomStateManager _roomStateManager;
    private final RoomSettings _settings;
    private final List<User> _users = new ArrayList<>();

    public Room(String roomId) {
        _roomId = roomId;
        _roomStateManager = new RoomStateManager(this);
        _settings = new RoomSettings(roomId);

        // Set default room state
        _roomStateManager.setState(RoomStateManager.RoomState.WAITING);
    }

    public String getId() {
        return _roomId;
    }

    public void join(Player player) throws RoomException {
        if (getUser(player) != null) {
            throw new RoomException("Player is already in room " + _roomId);
        }

        User user = new User(player);
        _users.add(user);

        _roomStateManager.getState().onUserJoin(user);
    }

    public void leave(Player player) {
        User user = getUser(player);
        _users.remove(user);

        _roomStateManager.getState().onUserLeave(user);
    }

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

    public boolean contains(User user) {
        return getUsers().contains(user);
    }

    public RoomSettings getSettings() {
        return _settings;
    }
}
