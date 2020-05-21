package net.phoenix1355.murder.room;

import net.phoenix1355.murder.Murder;
import net.phoenix1355.murder.arena.ArenaClue;
import net.phoenix1355.murder.config.MainConfigHandler;
import net.phoenix1355.murder.config.RoomConfigHandler;
import net.phoenix1355.murder.user.User;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasBlock() || event.getClickedBlock() == null) { // Interaction event wasn't related to a block
            return;
        }

        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();

        if (clickedBlock.getType() != ArenaClue.CLUE_MATERIAL) {
            return;
        }

        Player player = event.getPlayer();
        Room room = getPlayerRoom(player);

        if (room == null) { // Player not in a room
            return;
        }

        room.clueInteract(player, clickedBlock);

    }

    private Room getPlayerRoom(Player player) {
        RoomManager rm = RoomManager.getInstance();

        return rm.getRoomFromPlayer(player);
    }
}
