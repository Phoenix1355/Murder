package net.phoenix1355.murder.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class ConfigUtils {
    private static final String LOCATION_SEPARATOR = ",";
    public static String locationToString(Location location) {
        return String.join(
                LOCATION_SEPARATOR,
                location.getWorld().getName(),
                String.valueOf(location.getBlockX()),
                String.valueOf(location.getBlockY()),
                String.valueOf(location.getBlockZ())
        );
    }

    public static Location locationFromString(String location) {
        String[] args = location.split(LOCATION_SEPARATOR);

        return new Location(
                Bukkit.getWorld(args[0]),
                Double.parseDouble(args[1]),
                Double.parseDouble(args[2]),
                Double.parseDouble(args[3])
        );
    }
}
