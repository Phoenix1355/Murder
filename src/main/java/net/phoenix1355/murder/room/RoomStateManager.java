package net.phoenix1355.murder.room;

import net.phoenix1355.murder.Murder;
import net.phoenix1355.murder.room.state.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public class RoomStateManager {
    private final Room _room;

    private BaseRoomState _roomState;

    public RoomStateManager(Room room) {
        _room = room;
    }

    public Room getRoom() {
        return _room;
    }

    public void setState(RoomState state) {
        try {
            if (_roomState != null) {
                _roomState.cancel();
                _roomState.onStop();
            }

            _roomState = state.getInstance();
            _roomState.attachStateManager(this);
            _roomState.start();

            if (_roomState == null) {
                System.out.println("Unexpected null on setting state");
            }

            _roomState.onStart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BaseRoomState getState() {
        return _roomState;
    }

    public enum RoomState {
        WAITING(RoomWaitingState.class),
        STARTING(RoomStartingState.class),
        RUNNING(RoomRunningState.class),
        ENDING(RoomEndingState.class);

        private final Class<?> _targetState;

        RoomState(Class<? extends BaseRoomState> targetState) {
            _targetState = targetState;
        }

        BaseRoomState getInstance() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            return (BaseRoomState) _targetState.getDeclaredConstructor().newInstance();
        }
    }
}
