package com.github.ocraft.s2client.protocol.game;

import SC2APIProtocol.Common;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public enum Race implements Sc2ApiSerializable<Common.Race> {
    NO_RACE, TERRAN, ZERG, PROTOSS, RANDOM;

    @Override
    public Common.Race toSc2Api() {
        switch (this) {
            case NO_RACE:
                return Common.Race.NoRace;
            case TERRAN:
                return Common.Race.Terran;
            case ZERG:
                return Common.Race.Zerg;
            case PROTOSS:
                return Common.Race.Protoss;
            case RANDOM:
                return Common.Race.Random;
            default:
                throw new AssertionError("unknown race: " + this);
        }
    }

    public static Race from(Common.Race sc2ApiRace) {
        require("sc2api race", sc2ApiRace);
        switch (sc2ApiRace) {
            case NoRace:
                return NO_RACE;
            case Zerg:
                return ZERG;
            case Protoss:
                return PROTOSS;
            case Terran:
                return TERRAN;
            case Random:
                return RANDOM;
            default:
                throw new AssertionError("unknown sc2api race: " + sc2ApiRace);
        }
    }
}
