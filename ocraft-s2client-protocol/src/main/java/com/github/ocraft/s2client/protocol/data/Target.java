package com.github.ocraft.s2client.protocol.data;

import SC2APIProtocol.Data;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public enum Target {

    NONE,               // Does not require a target
    POINT,              // Requires a target position
    UNIT,               // Requires a unit to target. Given by position using feature layers.
    POINT_OR_UNIT,      // Requires either a target point or target unit.
    POINT_OR_NONE;      // Requires either a target point or no target. (eg. building add-ons)

    public static Target from(Data.AbilityData.Target sc2ApiTarget) {
        require("sc2api target", sc2ApiTarget);
        switch (sc2ApiTarget) {
            case None:
                return NONE;
            case Unit:
                return UNIT;
            case Point:
                return POINT;
            case PointOrNone:
                return POINT_OR_NONE;
            case PointOrUnit:
                return POINT_OR_UNIT;
            default:
                throw new AssertionError("unknown sc2api target: " + sc2ApiTarget);
        }
    }

}
