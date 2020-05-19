package net.phoenix1355.murder.config;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class MainConfigHandler extends BaseConfigHandler {
    public static final String FILE = "config.yml";

    private static MainConfigHandler _instance;

    private MainConfigHandler(JavaPlugin plugin) {
        super(plugin, FILE);
    }

    public static void init(JavaPlugin plugin) {
        _instance = new MainConfigHandler(plugin);
    }

    public static MainConfigHandler getInstance() {
        return _instance;
    }

    public Material getMurderWeapon() {
        String material = getConfig().getString("murder-weapon");

        return material != null ? Material.getMaterial(material) : Material.IRON_SWORD;
    }
}
