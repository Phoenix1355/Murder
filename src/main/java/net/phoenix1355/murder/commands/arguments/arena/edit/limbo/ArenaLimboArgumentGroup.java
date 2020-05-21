package net.phoenix1355.murder.commands.arguments.arena.edit.limbo;

import net.phoenix1355.murder.commands.arguments.BaseArgumentGroup;

public class ArenaLimboArgumentGroup extends BaseArgumentGroup {
    public ArenaLimboArgumentGroup() {
        registerArgument(new LimboSetArgument());
    }

    @Override
    public String getArgumentString() {
        return "limbo";
    }
}
