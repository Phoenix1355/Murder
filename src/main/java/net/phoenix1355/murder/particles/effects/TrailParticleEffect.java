package net.phoenix1355.murder.particles.effects;

import net.phoenix1355.murder.particles.BaseParticleEffect;
import org.bukkit.Location;
import org.bukkit.Particle;

public class TrailParticleEffect extends BaseParticleEffect {
    private static final Particle PARTICLE = Particle.SMOKE_LARGE;

    public TrailParticleEffect(Location location) {
        super(location);
    }

    @Override
    public void run() {
        spawnParticle(PARTICLE, getLocation(), 2, .1, .3, .1, .01);
    }
}
