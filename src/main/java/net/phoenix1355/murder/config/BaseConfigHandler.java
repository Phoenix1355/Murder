package net.phoenix1355.murder.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public abstract class BaseConfigHandler {
    private static final String SEPARATOR = "/";
    private final File _file;
    private final FileConfiguration _config;

    public BaseConfigHandler(JavaPlugin plugin, String filename) {
        _file = new File(plugin.getDataFolder() + SEPARATOR + filename);

        if (!_file.exists()) {
            plugin.saveResource(filename, false);
        }

        _config = YamlConfiguration.loadConfiguration(_file);
    }

    protected FileConfiguration getConfig() {
        return _config;
    }

    /**
     * Save the current configuration to the file
     */
    public void save() {
        try {
            _config.save(_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
