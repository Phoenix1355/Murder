package net.phoenix1355.murder.user;

import net.phoenix1355.murder.config.MainConfigHandler;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class User {
    public static final int MIN_CLUES_FOR_DETECTIVE = 5;

    private final Player _player;
    private Role _role;
    private int _clueCount;
    private long _bowCooldownTimestamp;

    public User(Player player) {
        _player = player;
        _role = Role.BYSTANDER;
        _clueCount = 0;
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

    public void foundClue() {
        _player.getWorld().playSound(_player.getLocation(), Sound.BLOCK_BELL_USE, 50, 1);

        _clueCount++;

        if (_role != Role.BYSTANDER) {
            return;
        }

        if (_clueCount >= MIN_CLUES_FOR_DETECTIVE) {
            _player.sendMessage(ChatFormatter.format("You found &b%s&e clues! You are now a &bdetective&e!", _clueCount));
            makeDetective();
        } else {
            _player.sendMessage(ChatFormatter.format("You found a clue! Find &b%s&e more to become &bdetective&e!", MIN_CLUES_FOR_DETECTIVE - _clueCount));
        }
    }

    public void resetClues() {
        _clueCount = 0;
    }

    public void makeDetective() {
        Material detectiveWeapon = Material.BOW;

        setRole(User.Role.DETECTIVE);
        getPlayer().getInventory().setItem(
                (getPlayer().getInventory().getHeldItemSlot() + 1) % 9,
                new ItemStack(detectiveWeapon, 1)
        );
        getPlayer().getInventory().setItem(
                (getPlayer().getInventory().getHeldItemSlot() + 2) % 9,
                new ItemStack(Material.ARROW, 1)
        );
    }

    public void makeMurderer() {
        Material murderWeapon = MainConfigHandler.getInstance().getMurderWeapon();

        setRole(User.Role.MURDERER);
        getPlayer().getInventory().setItem(
                (getPlayer().getInventory().getHeldItemSlot() + 1) % 9,
                new ItemStack(murderWeapon, 1)
        );
    }

    public void startBowCooldown() {
        _bowCooldownTimestamp = System.currentTimeMillis();
    }

    public boolean hasBowCooldown() {
        int cooldownTime = MainConfigHandler.getInstance().getBowCooldownTime();
        return System.currentTimeMillis() / 1000 - _bowCooldownTimestamp / 1000  < cooldownTime;
    }

    public enum Role {
        BYSTANDER,
        DETECTIVE,
        MURDERER,
        SPECTATOR,
    }
}
