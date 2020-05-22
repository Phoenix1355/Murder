package net.phoenix1355.murder.room.state;

import net.phoenix1355.murder.arena.ArenaManager;
import net.phoenix1355.murder.room.RoomStateManager;
import net.phoenix1355.murder.user.User;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.Location;

public class RoomStartingState extends BaseRoomState {
    private boolean _ready;

    @Override
    public void onStart() {
        getRoom().hidePlayers();

        // Chose random arena
        ArenaManager am = ArenaManager.getInstance();
        getRoom().getArenaHandler().setArena(am.getArena("default"));
        getRoom().broadcast(ChatFormatter.format("Playing on arena &b%s&e!", "default"));

        for (User user : getRoom().getUsers()) {
            // Set all users to bystander before choosing murderer and detective
            getRoom().makeBystander(user);

            Location waiting = getRoom().getArenaHandler().getWaitingLocation();

            if (waiting == null) {
                getRoom().broadcast(ChatFormatter.format("Waiting (limbo) location not set. Cannot continue..."));
                getStateManager().setState(RoomStateManager.RoomState.WAITING);
                return;
            }

            user.getPlayer().teleport(waiting);
        }

        // Choose a random murder
        User murderer = getRoom().getRandomBystander();
        getRoom().makeMurderer(murderer);

        murderer.getPlayer().sendTitle(ChatFormatter.format("&cYou are the murderer"), "", 10, 130, 10);
        murderer.getPlayer().sendMessage(ChatFormatter.format("You are the murderer. Kill everyone. Don't get caught..."));

        // Choose a random detective
        User detective = getRoom().getRandomBystander();
        getRoom().makeDetective(detective);

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
        getRoom().showPlayers();
    }
}
