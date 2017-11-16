package com.github.ocraft.s2client.protocol.unit;

import SC2APIProtocol.Raw;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public enum Alliance {
    SELF,
    ALLY,
    ENEMY,
    NEUTRAL;

    public static Alliance from(Raw.Alliance sc2ApiAlliance) {
        require("sc2api alliance", sc2ApiAlliance);
        switch (sc2ApiAlliance) {
            case Self:
                return SELF;
            case Ally:
                return ALLY;
            case Enemy:
                return ENEMY;
            case Neutral:
                return NEUTRAL;
            default:
                throw new AssertionError("unknown sc2api alliance: " + sc2ApiAlliance);
        }
    }

    public static Alliance from(int sc2ApiPlayerRelative) {
        switch (sc2ApiPlayerRelative) {
            case 1:
                return SELF;
            case 2:
                return ALLY;
            case 3:
                return NEUTRAL;
            case 4:
                return ENEMY;
            default:
                throw new AssertionError("unknown sc2api alliance: " + sc2ApiPlayerRelative);
        }
    }
}
