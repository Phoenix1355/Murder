package net.phoenix1355.murder.room.state;

import net.phoenix1355.murder.room.RoomStateManager;
import net.phoenix1355.murder.user.User;

public class RoomEndingState extends BaseRoomState {
    @Override
    public void onStart() {
        for (User user : getRoom().getUsers()) {
            user.getPlayer().getInventory().clear();
            user.setRole(User.Role.SPECTATOR);
            user.getPlayer().teleport(getRoom().getSettings().getLobbySpawnLocation());
        }

        getStateManager().setState(RoomStateManager.RoomState.WAITING);
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onStop() {

    }
}
