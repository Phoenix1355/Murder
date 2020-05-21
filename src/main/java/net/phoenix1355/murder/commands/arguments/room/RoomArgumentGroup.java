package net.phoenix1355.murder.commands.arguments.room;

import net.phoenix1355.murder.commands.arguments.BaseArgumentGroup;
import net.phoenix1355.murder.commands.arguments.JoinArgument;
import net.phoenix1355.murder.commands.arguments.LeaveArgument;
import net.phoenix1355.murder.commands.arguments.room.edit.RoomEditArgumentGroup;

public class RoomArgumentGroup extends BaseArgumentGroup {

    public RoomArgumentGroup() {
        registerArgument(new RoomCreateArgument());
        registerArgument(new RoomEditArgumentGroup());
    }

    @Override
    public String getArgumentString() {
        return "room";
    }

    @Override
    public String requirePermission() {
        return "murder.admin.room.*";
    }
}
