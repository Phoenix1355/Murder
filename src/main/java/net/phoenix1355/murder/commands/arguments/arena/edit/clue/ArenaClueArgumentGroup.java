package net.phoenix1355.murder.commands.arguments.arena.edit.clue;

import net.phoenix1355.murder.commands.arguments.BaseArgumentGroup;

public class ArenaClueArgumentGroup extends BaseArgumentGroup {

    public ArenaClueArgumentGroup() {
        registerArgument(new ClueSetArgument());
        registerArgument(new ClueDeleteArgument());
    }

    @Override
    public String getArgumentString() {
        return "clue";
    }
}
