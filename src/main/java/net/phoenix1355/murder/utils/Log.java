package net.phoenix1355.murder.utils;

import org.bukkit.Bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
    private static Logger _logger;

    public static void enable(Logger logger) {
        _logger = logger;
    }
    public static void setLevel(Level level) {
        _logger.setLevel(level);
    }

    private Log() {}

    public static void info(String msg, Object... params) {
        info(String.format(msg, params));
    }

    public static void info(String msg) {
        if (_logger != null)
            _logger.info(msg);
    }

    public static void debug(String msg, Object... params) {
        debug(String.format(msg, params));
    }

    public static void debug(String msg) {
        if (_logger != null)
            _logger.log(Level.FINE, msg);
    }

    public static void warn(String msg, Object... params) {
        warn(String.format(msg, params));
    }

    public static void warn(String msg) {
        if (_logger != null)
            _logger.warning(msg);
    }

    public static void error(String msg, Object... params) {
        error(String.format(msg, params));
    }

    public static void error(String msg) {
        if (_logger != null)
            _logger.log(Level.SEVERE, msg);
    }
}
