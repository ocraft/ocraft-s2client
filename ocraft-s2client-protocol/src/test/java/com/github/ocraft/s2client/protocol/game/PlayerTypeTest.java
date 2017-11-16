package com.github.ocraft.s2client.protocol.game;

import SC2APIProtocol.Sc2Api;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PlayerTypeTest {

    @Test
    void throwsExceptionWhenSc2ApiPlayerTypeIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PlayerType.from(nothing()))
                .withMessage("sc2api player type is required");
    }

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "playerTypeMappings")
    void mapsSc2ApiPlayerType(Sc2Api.PlayerType sc2ApiPlayerType, PlayerType expectedPlayerType) {
        assertThat(PlayerType.from(sc2ApiPlayerType)).isEqualTo(expectedPlayerType);

    }

    private static Stream<Arguments> playerTypeMappings() {
        return Stream.of(
                Arguments.of(Sc2Api.PlayerType.Participant, PlayerType.PARTICIPANT),
                Arguments.of(Sc2Api.PlayerType.Computer, PlayerType.COMPUTER),
                Arguments.of(Sc2Api.PlayerType.Observer, PlayerType.OBSERVER)
        );
    }

    @ParameterizedTest(name = "\"{0}\" is serialization of {1}")
    @MethodSource(value = "playerTypeMappings")
    void serializesToSc2ApiPlayerType(Sc2Api.PlayerType expectedSc2ApiPlayerType, PlayerType playerType) {
        assertThat(playerType.toSc2Api()).isEqualTo(expectedSc2ApiPlayerType);

    }

}