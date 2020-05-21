package net.phoenix1355.murder.particles.effects;

import net.phoenix1355.murder.particles.BaseParticleEffect;
import org.bukkit.Location;
import org.bukkit.Particle;

public class GlowParticleEffect extends BaseParticleEffect {
    private static final Particle PARTICLE = Particle.VILLAGER_HAPPY;

    public GlowParticleEffect(Location location) {
        super(location);
    }

    @Override
    public void run() {
        spawnParticle(PARTICLE, getLocation(), 1, .2f, .2f, .2f);
    }

    @Override
    public long getDelay() {
        return 5;
    }
}
