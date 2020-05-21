package net.phoenix1355.murder.particles.effects;

import net.phoenix1355.murder.particles.BaseParticleEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class GlowParticleEffect extends BaseParticleEffect {
    private static final Particle _particle = Particle.VILLAGER_HAPPY;
    private final World _world;

    public GlowParticleEffect(Location location) {
        super(location.clone().add(.5, .5, .5)); // Add centering offset

        _world = getLocation().getWorld();
    }

    @Override
    public void run() {
        _world.spawnParticle(_particle, getLocation(), 1, .2f, .2f, .2f);
    }
}
