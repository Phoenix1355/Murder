package net.phoenix1355.murder.config;

import net.phoenix1355.murder.arena.Arena;
import net.phoenix1355.murder.arena.ArenaClueLocation;
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
            List<Location> spawns = getArenaSpawnLocations(arenaId);
            List<ArenaClueLocation> clues = getArenaClueLocations(arenaId);
            Location limboLocation = getArenaLimboLocation(arenaId);

            Arena arena = new Arena(arenaId, spawns, clues, limboLocation);
            arenas.put(arenaId, arena);
        }

        return arenas;
    }

    private List<Location> getArenaSpawnLocations(String arenaId) {
        ConfigurationSection section = getArenaSection(arenaId);
        List<Location> spawns = new ArrayList<>();

        for (String location : section.getStringList("spawns")) {
            spawns.add(ConfigUtils.locationFromString(location));
        }

        return spawns;
    }

    private List<ArenaClueLocation> getArenaClueLocations(String arenaId) {
        ConfigurationSection section = getArenaSection(arenaId);
        List<ArenaClueLocation> clueLocations = new ArrayList<>();

        for (String str : section.getStringList("clues")) {
            Location location = ConfigUtils.locationFromString(str);
            clueLocations.add(new ArenaClueLocation(location));
        }

        return clueLocations;
    }

    private Location getArenaLimboLocation(String arenaId) {
        ConfigurationSection section = getArenaSection(arenaId);

        String location = section.getString("limbo");
        return location != null ? ConfigUtils.locationFromString(location) : null;
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

    public void addArenaSpawnLocation(String arenaId, Location location) {
        ConfigurationSection section = getArenaSection(arenaId);

        List<String> spawns = section.getStringList("spawns");
        spawns.add(ConfigUtils.locationToString(location));
        section.set("spawns", spawns);

        save();
    }

    public void addArenaClueLocation(String arenaId, Location location) {
        ConfigurationSection section = getArenaSection(arenaId);

        List<String> clues = section.getStringList("clues");
        clues.add(ConfigUtils.locationToString(location));
        section.set("clues", clues);

        save();
    }

    public void setArenaLimboLocation(String arenaId, Location location) {
        ConfigurationSection section = getArenaSection(arenaId);

        section.set("limbo", ConfigUtils.locationToString(location));

        save();
    }
}
