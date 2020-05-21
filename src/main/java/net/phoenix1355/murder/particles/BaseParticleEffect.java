package net.phoenix1355.murder.particles;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public abstract class BaseParticleEffect implements ParticleEffect {
    private Location _location;
    private Vector _offset;
    private Entity _boundEntity;
    private int _timerCount;

    public BaseParticleEffect(Location location) {
        this(location, null);
    }

    public BaseParticleEffect(Location location, Vector offset) {
        _location = location.clone(); // Prevent mutation
        _offset = offset;

        ParticleManager.getInstance().addEffect(this);
    }

    void runInternal() {
        _timerCount++;

        if (_timerCount >= getDelay()) {
            _timerCount = 0;
            run();
        }
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
        Location location = _boundEntity != null
                ? _boundEntity.getLocation().clone()
                : _location.clone();

        if (_offset != null)
            location.add(_offset);

        return location;
    }

    /**
     * Binds the particle to the given entity
     * @param entity The entity to bind to
     */
    public void bind(Entity entity) {
        _boundEntity = entity;
    }

    /**
     * Sets the offset from doubles
     * @param x The x offset
     * @param y The y offset
     * @param z The z offset
     */
    public void setOffset(double x, double y, double z) {
        setOffset(new Vector(x, y, z));
    }

    /**
     * Sets the offset from a vector
     * @param vec The vector offset
     */
    public void setOffset(Vector vec) {
        _offset = vec;
    }

    public long getDelay() {
        return 1;
    }

    /**
     * Wraps the {@link org.bukkit.World#spawnParticle(Particle, Location, int, double, double, double)} method to
     * prevent handling of the world entity
     */
    protected void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        World world = location.getWorld();

        if (world != null)
            world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ);
    }

    protected void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        World world = location.getWorld();

        if (world != null)
            world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra);
    }
}
