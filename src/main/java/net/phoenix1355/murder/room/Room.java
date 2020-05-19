package net.phoenix1355.murder.room;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private final RoomStateManager _roomStateManager;
    private final List<Player> _players = new ArrayList<>();

    public Room() {
        _roomStateManager = new RoomStateManager(this);
        // Set default room state
        _roomStateManager.setState(RoomStateManager.RoomState.WAITING);
    }

    public void join(Player player) {
        if (!_players.contains(player))
            _players.add(player);

        _roomStateManager.getState().onPlayerJoin(player);
    }

    public void leave(Player player) {
        _players.remove(player);

        _roomStateManager.getState().onPlayerLeave(player);
    }

    public List<Player> getPlayers() {
        return _players;
    }

    public void broadcast(String message) {
        for (Player player : getPlayers()) {
            player.sendMessage(message);
        }
    }
}
