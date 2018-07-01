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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static org.assertj.core.api.Assertions.assertThat;

class GameStatusTest {

    @Test
    void isUnknownForNullInput() {
        assertThat(GameStatus.from(nothing()))
                .as("status of game for null value")
                .isEqualTo(GameStatus.UNKNOWN);
    }

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "gameStatusMappings")
    void mapsSc2ApiStatus(Sc2Api.Status sc2ApiStatus, GameStatus expectedGameStatus) {
        assertThat(GameStatus.from(sc2ApiStatus)).isEqualTo(expectedGameStatus);

    }

    private static Stream<Arguments> gameStatusMappings() {
        return Stream.of(
                Arguments.of(Sc2Api.Status.launched, GameStatus.LAUNCHED),
                Arguments.of(Sc2Api.Status.init_game, GameStatus.INIT_GAME),
                Arguments.of(Sc2Api.Status.in_game, GameStatus.IN_GAME),
                Arguments.of(Sc2Api.Status.in_replay, GameStatus.IN_REPLAY),
                Arguments.of(Sc2Api.Status.ended, GameStatus.ENDED),
                Arguments.of(Sc2Api.Status.quit, GameStatus.QUIT),
                Arguments.of(Sc2Api.Status.unknown, GameStatus.UNKNOWN)
        );
    }

}
