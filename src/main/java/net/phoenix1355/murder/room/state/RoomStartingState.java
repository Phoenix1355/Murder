package net.phoenix1355.murder.room.state;

import net.phoenix1355.murder.room.RoomStateManager;
import net.phoenix1355.murder.user.User;
import net.phoenix1355.murder.utils.ChatFormatter;

public class RoomStartingState extends BaseRoomState {
    private boolean _ready;

    @Override
    public void onStart() {
        getRoom().hidePlayers();

        for (User user : getRoom().getUsers()) {
            user.getPlayer().getInventory().clear();
            // Set all users to bystander before choosing murderer and detective
            user.setRole(User.Role.BYSTANDER);

            // TODO: Teleport all players to temporary locations arena
        }

        // Choose a random murder
        User murderer = getRoom().getRandomBystander();
        murderer.setRole(User.Role.MURDERER);
        murderer.getPlayer().sendTitle(ChatFormatter.format("&cYou are the murderer"), "", 10, 130, 10);
        murderer.getPlayer().sendMessage(ChatFormatter.format("You are the murderer. Your job is to kill everyone without getting noticed. Good luck"));

        // Choose a random detective
        User detective = getRoom().getRandomBystander();
        detective.setRole(User.Role.DETECTIVE);
        detective.getPlayer().sendTitle(ChatFormatter.format("&bYou are a bystander"), ChatFormatter.format("&5with a secret weapon"), 10, 130, 10);
        detective.getPlayer().sendMessage(ChatFormatter.format("You are a detective. Your job is to find and kill the murderer. Be careful though, you only have one shot"));

        for (User user : getRoom().getBystanders()) {
            user.getPlayer().sendTitle(ChatFormatter.format("&bYou are a bystander"), "", 10, 130, 10);
        }

        setTimer(10);
        _ready = true;
    }

    @Override
    public void onUpdate() {
        if (_ready) {
            if (getTimer() == 0) {
                getStateManager().setState(RoomStateManager.RoomState.RUNNING);
            }

            setTimer(getTimer() - 1);
        }
    }

    @Override
    public void onStop() {

    }
}
