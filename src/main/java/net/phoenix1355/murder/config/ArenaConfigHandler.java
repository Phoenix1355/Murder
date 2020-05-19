package net.phoenix1355.murder.config;

import net.phoenix1355.murder.arena.Arena;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ArenaConfigHandler extends BaseConfigHandler {
    public static final String FILE = "arenas.yml";

    private static ArenaConfigHandler _instance;

    private ArenaConfigHandler(JavaPlugin plugin) {
        super(plugin, FILE);
    }

    public static void init(JavaPlugin plugin) {
        _instance = new ArenaConfigHandler(plugin);
    }

    public static ArenaConfigHandler getInstance() {
        return _instance;
    }

    public Map<String, Arena> loadArenas() {
        Map<String, Arena> arenas = new HashMap<>();

        // Load individual arena
        ConfigurationSection section = getArenasSection();
        Set<String> keys = section.getKeys(false);

        if (keys.isEmpty()) {
            return arenas;
        }

        for (String arenaId : keys) {
            List<Location> spawns = getArenaSpawns(arenaId);
            List<Location> clues = getArenaClues(arenaId);

            Arena arena = new Arena(arenaId, spawns, clues);
            arenas.put(arenaId, arena);
        }

        return arenas;
    }

    private List<Location> getArenaSpawns(String arenaId) {
        ConfigurationSection section = getArenaSection(arenaId);
        List<Location> spawns = new ArrayList<>();

        for (String location : section.getStringList("spawns")) {
            spawns.add(ConfigUtils.locationFromString(location));
        }

        return spawns;
    }

    private List<Location> getArenaClues(String arenaId) {
        ConfigurationSection section = getArenaSection(arenaId);
        List<Location> spawns = new ArrayList<>();

        for (String location : section.getStringList("clues")) {
            spawns.add(ConfigUtils.locationFromString(location));
        }

        return spawns;
    }

    public ConfigurationSection getArenasSection() {
        String path = "rooms";

        if (!getConfig().isSet(path)) {
            getConfig().createSection(path);
        }

        return getConfig().getConfigurationSection(path);
    }

    public ConfigurationSection getArenaSection(String roomId) {
        ConfigurationSection roomsSection = getArenasSection();

        if (!roomsSection.isSet(roomId)) {
            roomsSection.createSection(roomId);
        }

        return getArenasSection().getConfigurationSection(roomId);
    }

    public void createArena(String arenaId) {
        getArenasSection().createSection(arenaId);

        save();
    }

    public void addArenaSpawn(String arenaId, Location location) {
        ConfigurationSection section = getArenaSection(arenaId);

        List<String> spawns = section.getStringList("spawns");
        spawns.add(ConfigUtils.locationToString(location));
        section.set("spawns", spawns);

        save();
    }
}
