package net.phoenix1355.murder.user;

import net.phoenix1355.murder.config.MainConfigHandler;
import net.phoenix1355.murder.particles.ParticleEffect;
import net.phoenix1355.murder.particles.effects.TrailParticleEffect;
import net.phoenix1355.murder.room.RoomManager;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class User {
    public static final int MIN_CLUES_FOR_DETECTIVE = 5;
    public static final Material DETECTIVE_WEAPON = Material.BOW;
    public static ParticleEffect _trail;

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
            RoomManager.getInstance().getRoomFromPlayer(_player).makeDetective(this);
        } else {
            _player.sendMessage(ChatFormatter.format("You found a clue! Find &b%s&e more to become &bdetective&e!", MIN_CLUES_FOR_DETECTIVE - _clueCount));
        }
    }

    public void resetClues() {
        _clueCount = 0;
    }

    public void addBow() {
        getPlayer().getInventory().setItem(1, new ItemStack(DETECTIVE_WEAPON, 1));
    }

    public void addArrow() {
        getPlayer().getInventory().setItem(8, new ItemStack(Material.ARROW, 1));
    }

    public void addMurderWeapon() {
        Material murderWeapon = MainConfigHandler.getInstance().getMurderWeapon();

        getPlayer().getInventory().setItem(1, new ItemStack(murderWeapon, 1));
    }

    public void startBowCooldown() {
        _bowCooldownTimestamp = System.currentTimeMillis();
    }

    public boolean hasBowCooldown() {
        int cooldownTime = MainConfigHandler.getInstance().getBowCooldownTime();
        return System.currentTimeMillis() / 1000 - _bowCooldownTimestamp / 1000  < cooldownTime;
    }

    public void setExp(float xp) {
        _player.setExp(xp);
    }

    public void addExp(float experience) {
        float xp = _player.getExp() + experience;
        _player.setExp(Math.min(xp, 1F));
    }

    public void playSound(Sound sound, float volume, float pitch) {
        playSound(_player.getLocation(), sound, volume, pitch);
    }

    public void playSound(Location location, Sound sound, float volume, float pitch) {
        _player.playSound(location, sound, volume, pitch);
    }

    public void showTrail() {
        if (_trail == null) {
            _trail = new TrailParticleEffect(_player.getLocation());
            _trail.bind(_player);
            _trail.setOffset(0, 1, 0);
        }
    }

    public void hideTrail() {
        if (_trail != null) {
            _trail.cancel();
            _trail = null;
        }
    }

    public enum Role {
        BYSTANDER,
        DETECTIVE,
        MURDERER,
        SPECTATOR,
    }
}
