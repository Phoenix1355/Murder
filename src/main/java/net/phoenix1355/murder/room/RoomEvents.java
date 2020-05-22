package net.phoenix1355.murder.room;

import net.phoenix1355.murder.Murder;
import net.phoenix1355.murder.arena.ArenaClue;
import net.phoenix1355.murder.config.MainConfigHandler;
import net.phoenix1355.murder.user.User;
import net.phoenix1355.murder.utils.Log;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;

public class RoomEvents implements Listener {
    private final MainConfigHandler _configHandler;

    public RoomEvents(Murder plugin) {
        _configHandler = MainConfigHandler.getInstance();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Handle murder damage on bystanders/detectives
     * @param e
     */
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) {
            return;
        }

        if (!RoomUtils.areInSameRoom((Player) e.getDamager(), (Player) e.getEntity())) {
            return;
        }

        Room room = getPlayerRoom((Player) e.getDamager());

        User attacker = room.getUser((Player) e.getDamager());
        User victim = room.getUser((Player) e.getEntity());

        e.setCancelled(true);

        if (attacker.getRole() != User.Role.MURDERER || victim.getRole() == User.Role.MURDERER) {
            return;
        }

        Material murderWeapon = _configHandler.getMurderWeapon();

        if (attacker.getPlayer().getInventory().getItemInMainHand().getType() != murderWeapon) {
            return;
        }

        if (attacker.getPlayer().hasCooldown(murderWeapon)) {
            return;
        }

        room.getEventHandler().kill(victim.getPlayer(), attacker.getPlayer());
    }

    /**
     * Handle player getting hit by an arrow
     * @param e Arrow damage event
     */
    @EventHandler
    public void onArrowDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Arrow && e.getEntity() instanceof Player)) {
            return;
        }
        Arrow arrow = (Arrow) e.getDamager();

        if (!(arrow.getShooter() instanceof  Player)) {
            return;
        }

        Player attackerPlayer = (Player) arrow.getShooter();
        Player victimPlayer = (Player) e.getEntity();

        if (!RoomUtils.areInSameRoom(attackerPlayer, victimPlayer)) {
            return;
        }

        e.setCancelled(true);
        arrow.remove();

        // Prevent suicide
        if (attackerPlayer.equals(victimPlayer)) {
            return;
        }

        Room room = getPlayerRoom(attackerPlayer);

        if (room.getUser(attackerPlayer).getRole() != User.Role.DETECTIVE) {
            return;
        }

        room.getEventHandler().kill(victimPlayer, attackerPlayer);
    }

    /**
     * Reset arrow respawn timer when detective uses bow
     * @param e Entity shoot bow event
     */
    @EventHandler
    public void onBowShot(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        if (!RoomUtils.isInRoom((Player) e.getEntity())) {
            return;
        }

        Room room = getPlayerRoom((Player) e.getEntity());
        User user = room.getUser((Player) e.getEntity());

        if (user.getRole() != User.Role.DETECTIVE) {
            return;
        }

        room.getArrowHandler().resetDetective(user);
    }

    /**
     * Disable re-pickup of arrows
     * @param e Arrow pickup event
     */
    @EventHandler
    public void onArrowPickup(PlayerPickupArrowEvent e) {
        if (RoomUtils.isInRoom(e.getPlayer())) {
            e.getItem().remove();
            e.setCancelled(true);
        }
    }

    /**
     * When user interacts with an entity
     * @param event Player interaction event
     */
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

        room.getEventHandler().clueInteract(player, clickedBlock);

    }

    /**
     * Prevent fall damage unless it's attempting suicide
     * @param e Entity damage event
     */
    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) e.getEntity();
        Room room = getPlayerRoom(victim);

        if (room == null) {
            return;
        }

        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            e.setCancelled(true);

            if (e.getDamage() >= 20.0) {
                room.getEventHandler().kill(victim, null);
            }
        }
    }

    /**
     * Handle item pickup when player picks up a bow
     * @param e Entity pickup event
     */
    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) e.getEntity();
        Room room = getPlayerRoom(player);

        if (room == null) {
            return;
        }

        e.setCancelled(true);

        User user = room.getUser(player);

        if (user == null || user.getRole() != User.Role.BYSTANDER) {
            return;
        }

        if (e.getItem().getItemStack().getType() != Material.BOW) {
            return;
        }

        if (user.hasBowCooldown()) {
            return;
        }

        e.getItem().remove();

        room.makeDetective(user);
    }

    /**
     * Prevent players from moving inventory items when in a room
     * @param e Inventory click event
     */
    @EventHandler
    public void onItemMove(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (!RoomUtils.isInRoom(player)) {
            return;
        }

        e.setCancelled(true);
    }

    /**
     * Disable item durability when in a room
     * @param e Player item damage event
     */
    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent e) {
        Player player = e.getPlayer();
        Room room = getPlayerRoom(player);

        if (room != null) {
            e.setCancelled(true);
        }
    }

    /**
     * Show trails when murder is sprinting
     * @param e Player move event
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        if (!RoomUtils.isInRoom(player)) {
            return;
        }

        Room room = getPlayerRoom(player);
        User user = room.getUser(player);

        if (user.getRole() != User.Role.MURDERER) {
            if (player.isSprinting()) {
                player.setSprinting(false);
            }

            return;
        }

        if (player.isSprinting()) {
            user.showTrail();
        } else {
            user.hideTrail();
        }
    }

    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) e.getEntity();
        Room room = getPlayerRoom(player);

        if (room != null) {
            e.setCancelled(true);
        }
    }

    /**
     * Prevent players from destroying paintings and signs while in a room
     * @param e The hanging break event
     */
    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent e) {
        if (!(e.getRemover() instanceof Player)) {
            return;
        }

        Player player = (Player) e.getRemover();
        Room room = getPlayerRoom(player);

        if (room != null) {
            e.setCancelled(true);
        }
    }

    private Room getPlayerRoom(Player player) {
        RoomManager rm = RoomManager.getInstance();

        return rm.getRoomFromPlayer(player);
    }
}
