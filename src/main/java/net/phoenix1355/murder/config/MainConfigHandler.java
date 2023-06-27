package net.phoenix1355.murder.config;

import net.phoenix1355.murder.Murder;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class MainConfigHandler extends BaseConfigHandler {
    public static final String FILE = "config.yml";

    private static MainConfigHandler _instance;

    public static MainConfigHandler getInstance() {
        if (_instance == null)
            _instance = new MainConfigHandler(JavaPlugin.getPlugin(Murder.class));
        return _instance;
    }

    private MainConfigHandler(JavaPlugin plugin) {
        super(plugin, FILE);
    }

    public Material getMurderWeapon() {
        String material = getConfig().getString("murder-weapon");
        return material != null ? Material.getMaterial(material) : Material.IRON_SWORD;
    }

    public int getDetectiveRechargeTime() {
        return getConfig().getInt("detective-bow-cooldown", 5);
    }

    public int getDetectivePunishmentTime() {
        return getConfig().getInt("detective-punishment-time", 10);
    }

    public int getPlayerMinClues() {
        return getConfig().getInt("player-min-clues", 10);
    }

    public int getGameTime() {
        return getConfig().getInt("game-time", 270);
    }

    public int getGameMinPlayers() {
        return getConfig().getInt("game-min-players", 4);
    }

    public int getGameMaxPlayers() {
        return getConfig().getInt("game-max-players", 10);
    }

    public boolean getAutoStart() {
        return getConfig().getBoolean("game-auto-start", false);
    }

    public int getArenaCluesMax() {
        return getConfig().getInt("arena-clues-max", 5);
    }

    public int getArenaCluesSpawnTime() {
        return getConfig().getInt("arena-clues-spawn-time", 5);
    }
}
