package net.phoenix1355.murder.room;

import net.phoenix1355.murder.Murder;
import net.phoenix1355.murder.arena.Arena;
import net.phoenix1355.murder.arena.ArenaClueLocation;
import net.phoenix1355.murder.user.User;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Mule;
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
    private final RoomArenaHandler _roomArenaHandler;
    private final RoomSettings _settings;
    private final List<User> _users = new ArrayList<>();
    private final List<Item> _spawnedItems = new ArrayList<>();

    public Room(String roomId) {
        _roomId = roomId;
        _roomStateManager = new RoomStateManager(this);
        _roomArenaHandler = new RoomArenaHandler();
        _settings = new RoomSettings(roomId);

        // Set default room state
        _roomStateManager.setState(RoomStateManager.RoomState.WAITING);
    }

    public String getId() {
        return _roomId;
    }

    public RoomArenaHandler getArenaHandler() {
        return _roomArenaHandler;
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

    public void kill(Player victim, Player attacker) {
        _roomStateManager.getState().onUserDeath(getUser(victim), getUser(attacker));
    }

    public void clueInteract(Player player, Block clickedBlock) {
        User user = getUser(player);

        if (user == null)
            return;

        ArenaClueLocation clueLocation = getArenaHandler().getClue(clickedBlock.getLocation());

        if (clueLocation == null)
            return;

        _roomStateManager.getState().onClueInteract(player, clueLocation);
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
                if (user != u)
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

    public boolean contains(User user) {
        return getUsers().contains(user);
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

        for (User user : getUsers()) {
            RoomUtils.resetUser(user);
            user.getPlayer().teleport(getSettings().getLobbySpawnLocation());
            user.getPlayer().setGameMode(GameMode.ADVENTURE);
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
}
