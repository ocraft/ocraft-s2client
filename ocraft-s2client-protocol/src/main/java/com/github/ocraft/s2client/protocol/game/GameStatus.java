package com.github.ocraft.s2client.protocol.game;

import SC2APIProtocol.Sc2Api;

import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.errorOf;
import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

public enum GameStatus {
    LAUNCHED, INIT_GAME, IN_GAME, IN_REPLAY, ENDED, QUIT, UNKNOWN;

    public static GameStatus from(Sc2Api.Status sc2ApiStatus) {
        if (statusIsUnknown(sc2ApiStatus)) return UNKNOWN;
        return Optional.of(sc2ApiStatus)
                .map(GameStatus::convert)
                .orElseThrow(errorOf("unknown game status"));
    }

    private static boolean statusIsUnknown(Sc2Api.Status sc2ApiStatus) {
        return !isSet(sc2ApiStatus);
    }

    private static GameStatus convert(Sc2Api.Status sc2ApiStatus) {
        switch (sc2ApiStatus) {
            case launched:
                return LAUNCHED;
            case init_game:
                return INIT_GAME;
            case in_game:
                return IN_GAME;
            case in_replay:
                return IN_REPLAY;
            case ended:
                return ENDED;
            case quit:
                return QUIT;
            case unknown:
                return UNKNOWN;
            default:
                return nothing();
        }
    }
}
