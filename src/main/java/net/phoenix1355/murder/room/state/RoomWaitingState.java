package net.phoenix1355.murder.room.state;

import net.phoenix1355.murder.room.RoomStateManager;
import net.phoenix1355.murder.room.RoomUtils;
import net.phoenix1355.murder.user.User;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;

public class RoomWaitingState extends BaseRoomState {
    private static final int MIN_PLAYERS = 2;
    private boolean _ready = false;

    @Override
    public void onStart() {
        // Cleanup arena
        getRoom().reset();

        checkPlayerCount();
    }

    @Override
    public void onUpdate() {
        if (_ready) {
            if (getTimer() == 0) {
                getStateManager().setState(RoomStateManager.RoomState.STARTING);
                return;
            }

            if (getTimer() % 5 == 0) {
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
        user.getPlayer().getInventory().clear();
        user.getPlayer().setGameMode(GameMode.ADVENTURE);

        getRoom().broadcast(
                ChatFormatter.format(
                        "%s has joined the room",
                        user.getPlayer().getName()
                )
        );

        // Teleport player to lobby spawn
        user.getPlayer().teleport(getRoom().getSettings().getLobbySpawnLocation());

        checkPlayerCount();
    }

    @Override
    public void onUserLeave(User user) {
        getRoom().broadcast(ChatFormatter.format("&b%s&e has left the room", user.getPlayer().getName()));
        checkPlayerCount();

        if (user.getPlayer().getBedSpawnLocation() != null)
            user.getPlayer().teleport(user.getPlayer().getBedSpawnLocation());
        else
            user.getPlayer().teleport(user.getPlayer().getWorld().getSpawnLocation());
    }

    private void checkPlayerCount() {
        if (getRoom().getUsers().size() >= MIN_PLAYERS) {
            if (!_ready) {
                setTimer(20);
                _ready = true;
            }
        } else {
            getRoom().broadcast(ChatFormatter.format(
                    "Not enough players to start the game (&b%d/%d&e)",
                    getStateManager().getRoom().getUsers().size(),
                    MIN_PLAYERS)
            );
            _ready = false;
        }
    }
}
