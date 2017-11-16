package com.github.ocraft.s2client.protocol.game;

import SC2APIProtocol.Common;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RaceTest {

    @ParameterizedTest(name = "\"{0}\" serializes to {1}")
    @MethodSource(value = "raceMappings")
    void serializesToSc2Api(Race race, Common.Race expectedSc2ApiRace) {
        assertThat(race.toSc2Api()).isEqualTo(expectedSc2ApiRace);
    }

    private static Stream<Arguments> raceMappings() {
        return Stream.of(
                Arguments.of(Race.NO_RACE, Common.Race.NoRace),
                Arguments.of(Race.TERRAN, Common.Race.Terran),
                Arguments.of(Race.ZERG, Common.Race.Zerg),
                Arguments.of(Race.PROTOSS, Common.Race.Protoss),
                Arguments.of(Race.RANDOM, Common.Race.Random));
    }

    @ParameterizedTest(name = "\"{0}\" maps {1}")
    @MethodSource(value = "raceMappings")
    void mapsSc2ApiRace(Race expectedRace, Common.Race sc2ApiRace) {
        assertThat(Race.from(sc2ApiRace)).isEqualTo(expectedRace);
    }

    @Test
    void throwsExceptionWhenSc2ApiRaceIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Race.from(nothing()))
                .withMessage("sc2api race is required");
    }

}