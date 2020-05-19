package net.phoenix1355.murder.commands.arguments.room;

import net.phoenix1355.murder.commands.arguments.BaseArgumentGroup;
import net.phoenix1355.murder.commands.arguments.JoinArgument;
import net.phoenix1355.murder.commands.arguments.LeaveArgument;

public class RoomArgumentGroup extends BaseArgumentGroup {

    public RoomArgumentGroup() {
        registerArgument(new RoomCreateArgument());
    }

    @Override
    public String getArgumentString() {
        return "room";
    }


}
