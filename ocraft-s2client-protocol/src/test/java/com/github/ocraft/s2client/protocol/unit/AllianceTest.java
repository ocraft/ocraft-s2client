package com.github.ocraft.s2client.protocol.unit;

import SC2APIProtocol.Raw;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.of;

class AllianceTest {
    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "allianceMappings")
    void mapsSc2ApiAlliance(Raw.Alliance sc2ApiAlliance, Alliance expectedAlliance) {
        assertThat(Alliance.from(sc2ApiAlliance)).isEqualTo(expectedAlliance);
    }

    private static Stream<Arguments> allianceMappings() {
        return Stream.of(
                of(Raw.Alliance.Self, Alliance.SELF),
                of(Raw.Alliance.Ally, Alliance.ALLY),
                of(Raw.Alliance.Enemy, Alliance.ENEMY),
                of(Raw.Alliance.Neutral, Alliance.NEUTRAL));
    }

    @Test
    void throwsExceptionWhenAllianceIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Alliance.from(nothing()))
                .withMessage("sc2api alliance is required");
    }

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "playerRelativeMappings")
    void mapsSc2ApiPlayerRelative(int sc2ApiPlayerRellative, Alliance expectedAlliance) {
        assertThat(Alliance.from(sc2ApiPlayerRellative)).isEqualTo(expectedAlliance);
    }

    private static Stream<Arguments> playerRelativeMappings() {
        return Stream.of(
                of(1, Alliance.SELF),
                of(2, Alliance.ALLY),
                of(3, Alliance.NEUTRAL),
                of(4, Alliance.ENEMY));
    }

}