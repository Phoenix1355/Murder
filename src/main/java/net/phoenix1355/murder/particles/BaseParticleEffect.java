package net.phoenix1355.murder.particles;

import org.bukkit.Location;

public abstract class BaseParticleEffect implements ParticleEffect {
    private final Location _location;

    public BaseParticleEffect(Location location) {
        _location = location;

        ParticleManager.getInstance().addEffect(this);
    }

    /**
     * The runnable that gets executed every half a second
     */
    public abstract void run();

    /**
     * Cancels the current effect by removing it from the {@link ParticleManager} instance
     */
    public void cancel() {
        ParticleManager.getInstance().cancelEffect(this);
    }

    /**
     * Returns the location of the particle effect
     * @return The particle effect location
     */
    public Location getLocation() {
        return _location;
    }
}
