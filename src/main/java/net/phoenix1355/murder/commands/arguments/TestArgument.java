package net.phoenix1355.murder.commands.arguments;

import net.phoenix1355.murder.particles.ParticleEffect;
import net.phoenix1355.murder.particles.effects.TrailParticleEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestArgument extends BaseArgument {
    private ParticleEffect _effect = null;

    @Override
    public boolean handleCommand(CommandSender sender, Command command,
            String[] args, int argIndex) {
        if (!(sender instanceof Player))
            return false;

        Player player = (Player) sender;

        if (_effect == null) {
            _effect = new TrailParticleEffect(player.getLocation());
            _effect.bind(player);
            _effect.setOffset(0, 1, 0);
        } else {
            _effect.cancel();
            _effect = null;
        }

        return true;
    }

    @Override
    public String getArgumentString() {
        return "test";
    }
}
