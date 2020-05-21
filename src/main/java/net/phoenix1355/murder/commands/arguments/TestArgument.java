package net.phoenix1355.murder.commands.arguments;

import net.phoenix1355.murder.utils.ChatFormatter;
import net.phoenix1355.murder.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TestArgument extends BaseArgument {
    public static final Material CLUE_MATERIAL = Material.PLAYER_HEAD;
    public static final UUID CLUE_PLAYER_UUID = UUID.fromString("8c1da1ba-e0f0-48f7-a1d0-88fffc7ece70");

    private OfflinePlayer _selectedPlayer;

    @Override
    public boolean handleCommand(CommandSender sender, Command command, String[] args, int argIndex) {
        Block block = ((Player) sender).getTargetBlock(null, 20);

        if (args.length > 1 && args[1].equals("2")) {
            Skull skull = (Skull) block.getState();
            OfflinePlayer owner = skull.getOwningPlayer();
            sender.sendMessage(ChatFormatter.format(
                    "Block type: %s, Skull owner: %s",
                    block.getType().toString(),
                    owner != null ? "non-null" : "null"
            ));

            return true;
        }

        if (args.length > 1 && args[1].equals("copy")) {
            Skull skull = (Skull) block.getState();
            _selectedPlayer = skull.getOwningPlayer();

            sender.sendMessage(ChatFormatter.format("Skull was copied"));

            return true;
        }

        if (args.length > 1 && args[1].equals("paste")) {
            if (_selectedPlayer == null) {
                sender.sendMessage(ChatFormatter.format("A skull has not been selected"));
                return true;
            }

            Skull skull = (Skull) block.getState();
            skull.setOwningPlayer(_selectedPlayer);
            skull.update();

            sender.sendMessage(ChatFormatter.format("Skull was pasted"));

            return true;
        }

        if (args.length > 2) {
            Skull skull = (Skull) block.getState();
            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(args[2]));
            skull.setOwningPlayer(player);
            skull.update();

            sender.sendMessage(ChatFormatter.format("Skull was set to %s", args[2]));

            return true;
        }

        if (args.length > 1) {
            Skull skull = (Skull) block.getState();
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
            skull.setOwningPlayer(player);
            skull.update();

            sender.sendMessage(ChatFormatter.format("Skull was set to %s", args[1]));

            return true;
        }

        // Spawn block
        block.setType(CLUE_MATERIAL);
        OfflinePlayer player = Bukkit.getOfflinePlayer(CLUE_PLAYER_UUID);
        Log.info("Fetched offline player: %s", player.getUniqueId().toString());
        ((Skull) block.getState()).setOwningPlayer(player);
        block.getState().update();

        return true;
    }

    @Override
    public String getArgumentString() {
        return "test";
    }
}
