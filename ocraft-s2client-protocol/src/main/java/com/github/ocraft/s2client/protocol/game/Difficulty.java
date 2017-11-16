package com.github.ocraft.s2client.protocol.game;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public enum Difficulty implements Sc2ApiSerializable<Sc2Api.Difficulty> {
    VERY_EASY,
    EASY,
    MEDIUM,
    MEDIUM_HARD,
    HARD,
    HARDER,
    VERY_HARD,
    CHEAT_VISION,
    CHEAT_MONEY,
    CHEAT_INSANE;

    @Override
    public Sc2Api.Difficulty toSc2Api() {
        switch (this) {
            case VERY_EASY:
                return Sc2Api.Difficulty.VeryEasy;
            case EASY:
                return Sc2Api.Difficulty.Easy;
            case MEDIUM:
                return Sc2Api.Difficulty.Medium;
            case MEDIUM_HARD:
                return Sc2Api.Difficulty.MediumHard;
            case HARD:
                return Sc2Api.Difficulty.Hard;
            case HARDER:
                return Sc2Api.Difficulty.Harder;
            case VERY_HARD:
                return Sc2Api.Difficulty.VeryHard;
            case CHEAT_VISION:
                return Sc2Api.Difficulty.CheatVision;
            case CHEAT_MONEY:
                return Sc2Api.Difficulty.CheatMoney;
            case CHEAT_INSANE:
                return Sc2Api.Difficulty.CheatInsane;
            default:
                throw new AssertionError("unknown difficulty: " + this);
        }
    }

    public static Difficulty from(Sc2Api.Difficulty sc2ApiDifficulty) {
        require("sc2api difficulty", sc2ApiDifficulty);
        switch (sc2ApiDifficulty) {
            case VeryEasy:
                return VERY_EASY;
            case Easy:
                return EASY;
            case Medium:
                return MEDIUM;
            case MediumHard:
                return MEDIUM_HARD;
            case Hard:
                return HARD;
            case Harder:
                return HARDER;
            case VeryHard:
                return VERY_HARD;
            case CheatVision:
                return CHEAT_VISION;
            case CheatMoney:
                return CHEAT_MONEY;
            case CheatInsane:
                return CHEAT_INSANE;
            default:
                throw new AssertionError("unknown sc2api difficulty: " + sc2ApiDifficulty);
        }
    }
}
