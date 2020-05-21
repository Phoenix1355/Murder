package net.phoenix1355.murder.room;

import net.phoenix1355.murder.arena.ArenaClueLocation;
import net.phoenix1355.murder.user.User;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class RoomEventHandler {
    private final Room _room;

    public RoomEventHandler(Room room) {
        _room = room;
    }

    public void onPlayerJoin(Player player) throws RoomException {
        if (_room.getUser(player) != null) {
            throw new RoomException("Player is already in room " + _room.getId());
        }

        User user = new User(player);
        _room.addUser(user);

        _room.getStateManager().getState().onUserJoin(user);
    }

    public void leave(Player player) {
        User user = _room.getUser(player);

        if (user != null) {
            _room.removeUser(user);

            _room.getStateManager().getState().onUserLeave(user);
        }
    }

    public void kill(Player victim, Player attacker) {
        _room.getStateManager().getState().onUserDeath(_room.getUser(victim), _room.getUser(attacker));
    }

    public void clueInteract(Player player, Block clickedBlock) {
        User user = _room.getUser(player);

        if (user == null)
            return;

        ArenaClueLocation clueLocation = _room.getArenaHandler().getClue(clickedBlock.getLocation());

        if (clueLocation == null)
            return;

        _room.getStateManager().getState().onClueInteract(player, clueLocation);
    }
}
