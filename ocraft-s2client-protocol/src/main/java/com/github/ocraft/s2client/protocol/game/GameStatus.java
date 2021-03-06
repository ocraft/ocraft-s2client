package com.github.ocraft.s2client.protocol.game;

/*-
 * #%L
 * ocraft-s2client-protocol
 * %%
 * Copyright (C) 2017 - 2018 Ocraft Project
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
