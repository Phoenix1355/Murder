package net.phoenix1355.murder.room.state;

import net.phoenix1355.murder.arena.ArenaClueLocation;
import net.phoenix1355.murder.arena.ArenaException;
import net.phoenix1355.murder.config.MainConfigHandler;
import net.phoenix1355.murder.room.RoomStateManager;
import net.phoenix1355.murder.user.User;
import net.phoenix1355.murder.utils.ChatFormatter;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class RoomRunningState extends BaseRoomState {
    private static final int GAME_TIME =
            MainConfigHandler.getInstance().getGameTime();
    private static final int CLUE_SPAWN_TIME =
            MainConfigHandler.getInstance().getArenaCluesSpawnTime();

    @Override
    public void onStart() {
        getRoom().getArrowHandler().start();

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
        setTimer(GAME_TIME);
    }

    @Override
    public void onUpdate() {
        if (getTimer() % CLUE_SPAWN_TIME == 0) {
            getRoom().getArenaHandler().spawnRandomClue();
        }

        if (getTimer() == 0) {
            getStateManager().setState(RoomStateManager.RoomState.ENDING_TIMEOUT);
            return;
        }

        if (getTimer() <= 60 && getTimer() % 30 == 0) {
            getRoom().broadcast(ChatFormatter.format("There is &b%s seconds&e left. Better hurry up!", getTimer()));
        }

        setTimer(getTimer() - 1);
    }

    @Override
    public void onStop() {
        getRoom().getArrowHandler().reset();
    }

    @Override
    public void onUserDeath(User victim, User attacker) {
        victim.getPlayer().getInventory().clear();

        victim.getPlayer().setGameMode(GameMode.SPECTATOR);
        victim.getPlayer().playSound(victim.getPlayer().getLocation(), Sound.ENTITY_PLAYER_DEATH, 50, 1);

        // Check for possible game endings
        if (victim.getRole() == User.Role.MURDERER) {
            // Murder is dead, end the game
            getStateManager().setState(RoomStateManager.RoomState.ENDING_BYSTANDER_WIN);
            return;
        }

        if (victim.getRole() == User.Role.DETECTIVE) {
            getRoom().dropBow(victim.getPlayer().getLocation());
        }

        victim.setRole(User.Role.SPECTATOR);

        if (getRoom().getBystanders().isEmpty() && getRoom().getDetectives().isEmpty()) {
            // Murder wins, end the game
            getStateManager().setState(RoomStateManager.RoomState.ENDING_MURDER_WIN);
            return;
        }

        if (attacker != null && attacker.getRole() == User.Role.DETECTIVE) {
            detectivePenalty(attacker);
        }
    }

    @Override
    public void onClueInteract(Player player, ArenaClueLocation clickedBlock) {
        User user = getRoom().getUser(player);

        user.foundClue();

        clickedBlock.remove();
    }

    private void failure() {
        getRoom().broadcast(ChatFormatter.format("Unexpected game error. Returning to lobby..."));
        getStateManager().setState(RoomStateManager.RoomState.WAITING);
    }

    private void detectivePenalty(User user) {
        if (user.getRole() != User.Role.DETECTIVE) {
            return;
        }

        getRoom().makeBystander(user);

        user.startBowCooldown();
        int cooldownTime = MainConfigHandler.getInstance().getDetectiveRechargeTime();

        user.getPlayer().addPotionEffects(Arrays.asList(
                new PotionEffect(PotionEffectType.SLOW, cooldownTime * 20, 2),
                new PotionEffect(PotionEffectType.BLINDNESS, cooldownTime * 20, 2)
        ));

        // Drop a new bow
        getRoom().dropBow(user.getPlayer().getLocation());

        user.getPlayer().sendMessage(ChatFormatter.format("You killed an innocent player! You have been penalized for &b%s seconds&e", cooldownTime));
    }
}
