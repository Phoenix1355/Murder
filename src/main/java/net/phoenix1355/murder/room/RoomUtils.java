package net.phoenix1355.murder.room;

import net.phoenix1355.murder.user.User;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RoomUtils {
    public static boolean areInSameRoom(Player player, Player target) {
        RoomManager rm = RoomManager.getInstance();

        return rm.getRoomFromPlayer(player) == rm.getRoomFromPlayer(target);
    }

    public static boolean isInRoom(Player player) {
        return RoomManager.getInstance().getRoomFromPlayer(player) != null;
    }

    public static void resetUser(User user) {
        Player player = user.getPlayer();
        user.setRole(User.Role.SPECTATOR);
        user.resetClues();
        player.getInventory().clear();
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        player.setHealth(healthAttribute != null ? healthAttribute.getBaseValue() : 20);
        player.setFoodLevel(20);
    }
}
