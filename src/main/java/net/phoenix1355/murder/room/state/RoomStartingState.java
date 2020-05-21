package net.phoenix1355.murder.room.state;

import net.phoenix1355.murder.arena.ArenaManager;
import net.phoenix1355.murder.config.MainConfigHandler;
import net.phoenix1355.murder.room.RoomStateManager;
import net.phoenix1355.murder.user.User;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RoomStartingState extends BaseRoomState {
    private final MainConfigHandler _configHandler;
    private boolean _ready;

    public RoomStartingState() {
        _configHandler = MainConfigHandler.getInstance();
    }

    @Override
    public void onStart() {
        getRoom().hidePlayers();

        // Chose random arena
        ArenaManager am = ArenaManager.getInstance();
        getRoom().getArenaHandler().setArena(am.getArena("default"));
        getRoom().broadcast(ChatFormatter.format("Playing on arena &b%s&e!", "default"));

        for (User user : getRoom().getUsers()) {
            user.getPlayer().getInventory().clear();
            // Set all users to bystander before choosing murderer and detective
            user.setRole(User.Role.BYSTANDER);

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
        murderer.makeMurderer();

        murderer.getPlayer().sendTitle(ChatFormatter.format("&cYou are the murderer"), "", 10, 130, 10);
        murderer.getPlayer().sendMessage(ChatFormatter.format("You are the murderer. Your job is to kill everyone without getting noticed. Good luck"));

        // Choose a random detective
        User detective = getRoom().getRandomBystander();
        detective.makeDetective();

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
