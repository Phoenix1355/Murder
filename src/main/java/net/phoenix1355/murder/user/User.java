package net.phoenix1355.murder.user;

import org.bukkit.entity.Player;

public class User {
    private final Player _player;
    private Role _role;

    public User(Player player) {
        _player = player;
        _role = Role.BYSTANDER;
    }

    public Player getPlayer() {
        return _player;
    }

    public boolean is(Player player) {
        return _player == player;
    }

    public Role getRole() {
        return _role;
    }
    public void setRole(Role role) {
        _role = role;
    }

    public enum Role {
        BYSTANDER,
        DETECTIVE,
        MURDERER,
        SPECTATOR,
    }
}
