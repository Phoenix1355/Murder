package net.phoenix1355.murder.commands.arguments.arena;

import net.phoenix1355.murder.commands.arguments.BaseArgumentGroup;
import net.phoenix1355.murder.commands.arguments.arena.edit.ArenaEditArgumentGroup;

public class ArenaArgumentGroup extends BaseArgumentGroup {

    public ArenaArgumentGroup() {
        registerArgument(new ArenaCreateArgument());
        registerArgument(new ArenaDeleteArgument());
        registerArgument(new ArenaEditArgumentGroup());
    }

    @Override
    public String getArgumentString() {
        return "arena";
    }

    @Override
    public String requirePermission() {
        return "murder.admin.*";
    }
}
