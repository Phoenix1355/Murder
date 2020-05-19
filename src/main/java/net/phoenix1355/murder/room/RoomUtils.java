package net.phoenix1355.murder.room;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RoomUtils {
    public static boolean areInSameRoom(Player player, Player target) {
        RoomManager rm = RoomManager.getInstance();

        return rm.getRoomFromPlayer(player) == rm.getRoomFromPlayer(target);
    }
}
