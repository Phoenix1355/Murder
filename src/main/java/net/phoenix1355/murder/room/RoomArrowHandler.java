package net.phoenix1355.murder.room;

import net.phoenix1355.murder.Murder;
import net.phoenix1355.murder.config.MainConfigHandler;
import net.phoenix1355.murder.user.User;
import net.phoenix1355.murder.utils.Log;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class RoomArrowHandler {
    private final List<DetectiveHolder> _detectives = new ArrayList<>();
    private BukkitRunnable _runnable;
    private boolean _isRunning = false;

    public void start() {
        if (!_isRunning) {
            _isRunning = true;
            _runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    for (DetectiveHolder holder : _detectives) {
                        holder.increase();
                    }
                }
            };
            _runnable.runTaskTimer(JavaPlugin.getPlugin(Murder.class), 20L, 20L);
        } else {
            Log.warn("Start was called when task was already running");
        }
    }

    public void reset() {
        if (_isRunning) {
            _isRunning = false;
            _runnable.cancel();
            Log.info("Cancelled arrow handler task");
        }

        for (DetectiveHolder holder : _detectives) {
            holder.reset();
        }

        _detectives.clear();
    }

    public void addDetective(User user) {
        DetectiveHolder holder = getHolderFromUser(user);

        if (holder == null)
            _detectives.add(new DetectiveHolder(user));
    }

    public void removeDetective(User user) {
        _detectives.removeIf(h -> h.isUser(user));
    }

    public void resetDetective(User user) {
        DetectiveHolder holder = getHolderFromUser(user);

        if (holder != null)
            holder.reset();
    }

    private DetectiveHolder getHolderFromUser(User user) {
        for (DetectiveHolder holder : _detectives) {
            if (holder.isUser(user))
                return holder;
        }

        return null;
    }

    private static class DetectiveHolder {
        private static final int MAX = MainConfigHandler.getInstance().getDetectiveRechargeTime();
        private final User _user;

        // Has arrow by default, so value is max
        private int _value = MAX;
        private boolean _hasArrow = true;

        public DetectiveHolder(User user) {
            _user = user;
        }

        public void increase() {
            if (_hasArrow)
                return;

            _value++;

            _user.addExp(1F / MAX);

            if (_value >= MAX) {
                _user.addArrow();
                _user.setExp(1F);
                _user.getPlayer().playSound(_user.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
                _hasArrow = true;
            }
        }

        public void reset() {
            _hasArrow = false;
            _user.setExp(0F);
            _value = 0;
        }

        public boolean isUser(User user) {
            return _user.equals(user);
        }
    }
}
