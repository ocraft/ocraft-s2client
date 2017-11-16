package com.github.ocraft.s2client.protocol.observation;

import SC2APIProtocol.Sc2Api;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public enum Result {
    VICTORY,
    DEFEAT,
    TIE,
    UNDECIDED;

    public static Result from(Sc2Api.Result sc2ApiResult) {
        require("sc2api result", sc2ApiResult);
        switch (sc2ApiResult) {
            case Victory:
                return VICTORY;
            case Tie:
                return TIE;
            case Defeat:
                return DEFEAT;
            case Undecided:
                return UNDECIDED;
            default:
                throw new AssertionError("unknown result: " + sc2ApiResult);
        }
    }
}
