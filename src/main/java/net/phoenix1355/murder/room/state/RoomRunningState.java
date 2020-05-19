package net.phoenix1355.murder.room.state;

import net.phoenix1355.murder.room.RoomStateManager;
import net.phoenix1355.murder.utils.ChatFormatter;

public class RoomRunningState extends BaseRoomState {
    @Override
    public void onStart() {
        getRoom().showPlayers();

        // TODO: Teleport players to random spawn locations in arena

        // Setup gamer timer
        setTimer(270);
    }

    @Override
    public void onUpdate() {
        if (getTimer() == 0) {
            getRoom().broadcast(ChatFormatter.format("Time has run out. &bBystanders&e win!"));
            getStateManager().setState(RoomStateManager.RoomState.ENDING);
            return;
        }

        if (getTimer() <= 60 && getTimer() % 30 == 0) {
            getRoom().broadcast(ChatFormatter.format("There is &b%s seconds&e left. Better hurry up!", getTimer()));
        }
    }

    @Override
    public void onStop() {

    }
}
