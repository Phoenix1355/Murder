package net.phoenix1355.murder.commands.arguments.arena.edit.spawn;

import net.phoenix1355.murder.commands.arguments.BaseArgumentGroup;

public class ArenaSpawnArgumentGroup extends BaseArgumentGroup {
    public ArenaSpawnArgumentGroup() {
        registerArgument(new SpawnCreateArgument());
        registerArgument(new SpawnDeleteArgument());
    }

    @Override
    public String getArgumentString() {
        return "spawn";
    }
}
