package net.phoenix1355.murder.particles;

import net.phoenix1355.murder.Murder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ParticleManager extends BukkitRunnable {
    private final List<ParticleEffect> _particleEffects;

    private static ParticleManager _instance;
    public static ParticleManager getInstance() {
        if (_instance == null)
            _instance = new ParticleManager();

        return _instance;
    }

    private ParticleManager() {
        _particleEffects = new ArrayList<>();

        runTaskTimer(JavaPlugin.getPlugin(Murder.class), 5L, 5L);
    }

    @Override
    public void run() {
        for (ParticleEffect effect : _particleEffects) {
            effect.run();
        }
    }

    void addEffect(ParticleEffect effect) {
        _particleEffects.add(effect);
    }

    void cancelEffect(ParticleEffect effect) {
        _particleEffects.remove(effect);
    }
}
