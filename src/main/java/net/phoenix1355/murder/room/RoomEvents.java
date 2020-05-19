package net.phoenix1355.murder.room;

import net.phoenix1355.murder.Murder;
import net.phoenix1355.murder.config.MainConfigHandler;
import net.phoenix1355.murder.config.RoomConfigHandler;
import net.phoenix1355.murder.user.User;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class RoomEvents implements Listener {
    private final MainConfigHandler _configHandler;

    public RoomEvents(Murder plugin) {
        _configHandler = MainConfigHandler.getInstance();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityPickup(EntityPickupItemEvent e) {

    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) {
            return;
        }

        if (!RoomUtils.areInSameRoom((Player) e.getDamager(), (Player) e.getEntity())) {
            return;
        }

        RoomManager rm = RoomManager.getInstance();
        Room room = rm.getRoomFromPlayer((Player) e.getDamager());

        User attacker = room.getUser((Player) e.getDamager());
        User victim = room.getUser((Player) e.getEntity());

        e.setCancelled(true);

        if (attacker.getRole() != User.Role.MURDERER) {
            return;
        }

        if (victim.getRole() == User.Role.MURDERER) {
            return;
        }

        Material murderWeapon = _configHandler.getMurderWeapon();

        if (attacker.getPlayer().getInventory().getItemInMainHand().getType() != murderWeapon) {
            return;
        }

        if (attacker.getPlayer().hasCooldown(murderWeapon)) {
            return;
        }

        room.kill(victim.getPlayer());
    }
}
