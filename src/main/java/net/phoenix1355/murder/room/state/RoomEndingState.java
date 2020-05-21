package net.phoenix1355.murder.room.state;


import net.phoenix1355.murder.room.RoomStateManager;
import net.phoenix1355.murder.user.User;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.Sound;

public class RoomEndingState extends BaseRoomState {
    @Override
    public void onStart() {
        // TODO: Fireworks
        for (User user : getRoom().getUsers()) {
            user.getPlayer().playSound(user.getPlayer().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 50, 1);
            user.getPlayer().playSound(user.getPlayer().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 50, 1);
        }

        setTimer(5);
    }

    @Override
    public void onUpdate() {
        if (getTimer() == 0) {
            getRoom().broadcast(ChatFormatter.format("Game has ended. Returning to lobby..."));
            getStateManager().setState(RoomStateManager.RoomState.WAITING);
        }

        setTimer(getTimer() - 1);
    }

    @Override
    public void onStop() {
        getRoom().getArenaHandler().reset();
    }
}
