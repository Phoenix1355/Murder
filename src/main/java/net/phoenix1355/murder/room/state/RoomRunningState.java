package net.phoenix1355.murder.room.state;

import net.phoenix1355.murder.arena.ArenaClueLocation;
import net.phoenix1355.murder.arena.ArenaException;
import net.phoenix1355.murder.room.RoomStateManager;
import net.phoenix1355.murder.user.User;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class RoomRunningState extends BaseRoomState {
    @Override
    public void onStart() {
        try {
            for (User user : getRoom().getUsers()) {
                getRoom().getArenaHandler().spawnUser(user);
            }
        } catch (ArenaException e) {
            getRoom().broadcast(e.getMessage());
            failure();
            return;
        }

        // Setup gamer timer
        setTimer(270);
    }

    @Override
    public void onUpdate() {
        if (getTimer() % 5 == 0) {
            getRoom().getArenaHandler().spawnRandomClue();
        }

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
            getRoom().broadcast(ChatFormatter.format("The &cmurderer&e has killed everyone! It was &c%s&e!", getRoom().getMurderer().getPlayer().getName()));
            getRoom().broadcast(ChatFormatter.format("The &cmurderer&e wins!"));
            gameOver();
        }
    }

    @Override
    public void onClueInteract(Player player, ArenaClueLocation clickedBlock) {
        User user = getRoom().getUser(player);

        user.foundClue();

        clickedBlock.remove();
    }

    private void gameOver() {
        getStateManager().setState(RoomStateManager.RoomState.ENDING);
    }

    private void failure() {
        getRoom().broadcast(ChatFormatter.format("Unexpected game error. Returning to lobby..."));
        getStateManager().setState(RoomStateManager.RoomState.WAITING);
    }
}
