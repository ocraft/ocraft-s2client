package com.github.ocraft.s2client.protocol.game;

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