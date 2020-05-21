package net.phoenix1355.murder.arena;

import net.phoenix1355.murder.particles.ParticleEffect;
import net.phoenix1355.murder.particles.effects.GlowParticleEffect;
import net.phoenix1355.murder.utils.Log;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;

import java.util.UUID;

public class ArenaClue {
    public static final Material CLUE_MATERIAL = Material.PLAYER_HEAD;
    public static final UUID CLUE_PLAYER_UUID = UUID.fromString("8c1da1ba-e0f0-48f7-a1d0-88fffc7ece70");
    public static final OfflinePlayer CLUE_PLAYER = Bukkit.getOfflinePlayer("Trajan");

    private final ArenaClueLocation _location;
    private ParticleEffect _effect;

    public ArenaClue(ArenaClueLocation location) {
        _location = location;
    }

    /**
     * Spawns the clue entity at the given location
     */
    public void spawn() {
        Block block = getSpawnLocationBlock();

        if (block != null) {
            if (block.getType() != Material.AIR) {
                Log.warn("Can't set clue at position %s", block.getLocation().toString());
                return;
            }

            Log.info("Spawning new clue at %s", block.getLocation().toString());

            // Spawn block
            block.setType(CLUE_MATERIAL);

            Skull skull = (Skull) block.getState();
            skull.setOwningPlayer(CLUE_PLAYER);
            skull.update();

            // Create particle effect
            _effect = new GlowParticleEffect(_location.getLocation());
        }
    }

    public void remove() {
        Block block = getSpawnLocationBlock();

        if (block != null) {
            block.setType(Material.AIR);

            if (_effect != null)
                _effect.cancel();
        }
    }

    private Block getSpawnLocationBlock() {
        Location spawnLocation = _location.getLocation();
        World world = spawnLocation.getWorld();

        if (world == null) {
            return null;
        }

        return world.getBlockAt(spawnLocation);
    }

    public boolean isSpawned() {
        Block block = getSpawnLocationBlock();

        return block != null && block.getType() == CLUE_MATERIAL;
    }
}
