package net.phoenix1355.murder.room.state;

import net.phoenix1355.murder.room.RoomStateManager;
import net.phoenix1355.murder.user.User;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.GameMode;
import org.bukkit.Sound;

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

        setTimer(getTimer() - 1);
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onUserDeath(User victim) {
        victim.getPlayer().getInventory().clear();

        victim.getPlayer().setGameMode(GameMode.SPECTATOR);
        victim.getPlayer().playSound(victim.getPlayer().getLocation(), Sound.ENTITY_PLAYER_DEATH, 50, 1);

        // Check for possible game endings
        if (victim.getRole() == User.Role.MURDERER) {
            // Murder is dead, end the game
            getRoom().broadcast(ChatFormatter.format("The &cmurderer&e was killed! It was &c%s&e!", victim.getPlayer().getName()));
            getRoom().broadcast(ChatFormatter.format("The &bbystanders&e win!"));
            gameOver();
            return;
        }

        victim.setRole(User.Role.SPECTATOR);

        if (getRoom().getBystanders().isEmpty() && getRoom().getDetectives().isEmpty()) {
            // Murder wins, end the game
            getRoom().broadcast(ChatFormatter.format("The &cmurderer&e has killed everyone! It was &c%s&e!", victim.getPlayer().getName()));
            getRoom().broadcast(ChatFormatter.format("The &cmurderer&e wins!"));
            gameOver();
        }
    }

    private void gameOver() {
        getStateManager().setState(RoomStateManager.RoomState.ENDING);
    }
}
