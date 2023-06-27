package net.phoenix1355.murder.room.state.ending;

import net.phoenix1355.murder.user.User;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.ChatColor;

public class RoomBystanderWinState extends RoomBaseEndingState {
    @Override
    public void onStart() {
        super.onStart();

        for (User user : getRoom().getUsers()) {
            String title = user.getRole() != User.Role.MURDERER
                    ? ChatFormatter.format(ChatColor.GREEN + "You win!")
                    : ChatFormatter.format(ChatColor.RED + "You lose!");
            String subtitle = user.getRole() != User.Role.MURDERER
                    ? ChatFormatter.format("The &cmurderer&e was killed!")
                    : ChatFormatter.format("You were stopped before killing everyone");

            user.getPlayer().sendTitle(title, subtitle, 10, 20 * 4, 10);
        }
    }
}
