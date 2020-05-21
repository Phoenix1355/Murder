package net.phoenix1355.murder.particles;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public interface ParticleEffect {
    void run();
    void cancel();
    void bind(Entity entity);
    void setOffset(Vector vec);
    void setOffset(double x, double y, double z);
}
