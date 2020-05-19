package net.phoenix1355.murder.utils;

import org.bukkit.ChatColor;

public class ChatFormatter {
    public static String format(String format, Object... args) {
        return ChatColor.translateAlternateColorCodes('&', String.format("&e" + format, args));
    }
}
