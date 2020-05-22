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
    public static final Material DETECTIVE_WEAPON = Material.BOW;
    public static ParticleEffect _trail;

    private static final int MIN_CLUES_FOR_DETECTIVE =
            MainConfigHandler.getInstance().getPlayerMinClues();
    private static final int BOW_RECHARGE_TIME =
            MainConfigHandler.getInstance().getDetectiveRechargeTime();

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

    /**
     * Adds a clue to the count. Also upgrades the user to detective if the clue
     * count exceeds the minimum amount required to upgrade
     */
    public void foundClue() {
        _player.getWorld()
               .playSound(_player.getLocation(), Sound.BLOCK_BELL_USE, 50, 1);

        _clueCount++;

        if (_role != Role.BYSTANDER) {
            return;
        }

        if (_clueCount >= MIN_CLUES_FOR_DETECTIVE) {
            _player.sendMessage(ChatFormatter.format(
                    "You found &b%s&e clues! You are now a &bdetective&e!",
                    _clueCount
            ));
            RoomManager.getInstance()
                       .getRoomFromPlayer(_player)
                       .makeDetective(this);
        } else {
            _player.sendMessage(ChatFormatter.format(
                    "You found a clue! Find &b%s&e more to become "
                            + "&bdetective&e!",
                    MIN_CLUES_FOR_DETECTIVE - _clueCount
            ));
        }
    }

    /**
     * Reset amount of clues found
     */
    public void resetClues() {
        _clueCount = 0;
    }

    /**
     * Gives the user the detective bow
     */
    public void addBow() {
        getPlayer().getInventory()
                   .setItem(1, new ItemStack(DETECTIVE_WEAPON, 1));
    }

    /**
     * Gives the user an arrow
     */
    public void addArrow() {
        getPlayer().getInventory().setItem(8, new ItemStack(Material.ARROW, 1));
    }

    /**
     * Gives the user the murder weapon
     */
    public void addMurderWeapon() {
        Material murderWeapon =
                MainConfigHandler.getInstance().getMurderWeapon();

        getPlayer().getInventory().setItem(1, new ItemStack(murderWeapon, 1));
    }

    /**
     * Saves the current timestamp, for {@link #hasBowCooldown()} to use to
     * check if the user has cooldown
     */
    public void startBowCooldown() {
        _bowCooldownTimestamp = System.currentTimeMillis();
    }

    /**
     * Checks if the user has a bow cooldown
     *
     * @return True if cooldown is active
     */
    public boolean hasBowCooldown() {
        return System.currentTimeMillis() / 1000 - _bowCooldownTimestamp / 1000
                < BOW_RECHARGE_TIME;
    }

    /**
     * Set users experience bar
     *
     * @param xp Amount of xp (max 1F)
     */
    public void setExp(float xp) {
        _player.setExp(xp);
    }

    /**
     * Add experience to the users experience bar. Will max the experience at 1F
     *
     * @param experience The amount of xp to add
     */
    public void addExp(float experience) {
        float xp = _player.getExp() + experience;
        _player.setExp(Math.min(xp, 1F));
    }

    /**
     * Proxy method of {@link Player#playSound(Location, Sound, float, float)}
     * with the location set to the player location
     *
     * @param sound  The sound to play
     * @param volume The volume of the sound
     * @param pitch  The pitch of the sound
     */
    public void playSound(Sound sound, float volume, float pitch) {
        playSound(_player.getLocation(), sound, volume, pitch);
    }

    /**
     * Proxy method of {@link Player#playSound(Location, Sound, float, float)}
     *
     * @param location The location to play the sound
     * @param sound    The sound to play
     * @param volume   The volume of the sound
     * @param pitch    The pitch of the sound
     */
    public void playSound(Location location, Sound sound, float volume,
            float pitch) {
        _player.playSound(location, sound, volume, pitch);
    }

    /**
     * Show a smoke player trail that follows the player
     */
    public void showTrail() {
        if (_trail == null) {
            _trail = new TrailParticleEffect(_player.getLocation());
            _trail.bind(_player);
            _trail.setOffset(0, 1, 0);
        }
    }

    /**
     * Hide the trail if the trail is visible
     */
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
