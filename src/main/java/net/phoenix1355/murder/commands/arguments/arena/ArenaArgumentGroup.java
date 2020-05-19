package net.phoenix1355.murder.commands.arguments.arena;

import net.phoenix1355.murder.commands.arguments.BaseArgumentGroup;

public class ArenaArgumentGroup extends BaseArgumentGroup {
    @Override
    public String getArgumentString() {
        return "arena";
    }

    @Override
    public String requirePermission() {
        return "murder.admin.*";
    }
}
