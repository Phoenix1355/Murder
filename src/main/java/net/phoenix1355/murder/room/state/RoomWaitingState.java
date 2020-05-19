package net.phoenix1355.murder.room.state;

import net.phoenix1355.murder.room.RoomStateManager;
import org.bukkit.entity.Player;

public class RoomWaitingState extends BaseRoomState {
    private static final int MIN_PLAYERS = 2;

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPlayerJoin(Player player) {
        getStateManager().getRoom().broadcast(
                String.format(
                        "%s has joined the room (%d/%d)",
                        player.getName(),
                        getStateManager().getRoom().getPlayers().size(),
                        MIN_PLAYERS
                )
        );

        if (getStateManager().getRoom().getPlayers().size() >= MIN_PLAYERS) {
            getStateManager().getRoom().broadcast("Game is starting in 15 seconds");
            getStateManager().setState(RoomStateManager.RoomState.RUNNING);
        }
    }

    @Override
    public void onPlayerLeave(Player player) {
        getStateManager().getRoom().broadcast(String.format("%s has left the room", player.getName()));
    }
}
