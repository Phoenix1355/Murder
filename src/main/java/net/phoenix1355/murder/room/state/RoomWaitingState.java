package net.phoenix1355.murder.room.state;

import net.phoenix1355.murder.room.RoomStateManager;
import net.phoenix1355.murder.user.User;
import net.phoenix1355.murder.utils.ChatFormatter;

public class RoomWaitingState extends BaseRoomState {
    private static final int MIN_PLAYERS = 2;
    private boolean _ready = false;

    @Override
    public void onStart() {
        checkPlayerCount();
    }

    @Override
    public void onUpdate() {
        if (_ready) {
            if (getTimer() == 0) {
                getStateManager().setState(RoomStateManager.RoomState.STARTING);
            }

            if (getTimer() > 0 && getTimer() % 5 == 0) {
                getRoom().broadcast(ChatFormatter.format("Game starting in &b%s second%s", getTimer(), getTimer() != 1 ? "s" : ""));
            }

            setTimer(getTimer() - 1);
        }
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onUserJoin(User user) {
        getRoom().broadcast(
                String.format(
                        "%s has joined the room (%d/%d)",
                        user.getPlayer().getName(),
                        getStateManager().getRoom().getUsers().size(),
                        MIN_PLAYERS
                )
        );

        // Teleport player to lobby spawn
        user.getPlayer().teleport(getRoom().getSettings().getLobbySpawnLocation());

        checkPlayerCount();
    }

    @Override
    public void onUserLeave(User user) {
        getRoom().broadcast(String.format("%s has left the room", user.getPlayer().getName()));

        if (user.getPlayer().getBedSpawnLocation() != null)
            user.getPlayer().teleport(user.getPlayer().getBedSpawnLocation());
    }

    private void checkPlayerCount() {
        if (getRoom().getUsers().size() >= MIN_PLAYERS) {
            setTimer(20);
            _ready = true;
        } else {
            _ready = false;
        }
    }
}
