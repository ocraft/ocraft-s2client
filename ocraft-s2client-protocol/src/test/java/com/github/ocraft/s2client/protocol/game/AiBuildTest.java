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

class AiBuildTest {
    @Test
    void throwsExceptionWhenSc2ApiAiBuildIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AiBuild.from(nothing()))
                .withMessage("sc2api ai build is required");
    }

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "AiBuildMappings")
    void mapsSc2ApiAiBuild(Sc2Api.AIBuild sc2ApiAiBuild, AiBuild expectedAiBuild) {
        assertThat(AiBuild.from(sc2ApiAiBuild)).isEqualTo(expectedAiBuild);

    }

    private static Stream<Arguments> AiBuildMappings() {
        return Stream.of(
                Arguments.of(Sc2Api.AIBuild.RandomBuild, AiBuild.RANDOM_BUILD),
                Arguments.of(Sc2Api.AIBuild.Rush, AiBuild.RUSH),
                Arguments.of(Sc2Api.AIBuild.Timing, AiBuild.TIMING),
                Arguments.of(Sc2Api.AIBuild.Power, AiBuild.POWER),
                Arguments.of(Sc2Api.AIBuild.Macro, AiBuild.MACRO),
                Arguments.of(Sc2Api.AIBuild.Air, AiBuild.AIR)
        );
    }

    @ParameterizedTest(name = "\"{0}\" is serialization of {1}")
    @MethodSource(value = "AiBuildMappings")
    void serializesToSc2ApiAiBuild(Sc2Api.AIBuild expectedSc2ApiAiBuild, AiBuild aiBuild) {
        assertThat(aiBuild.toSc2Api()).isEqualTo(expectedSc2ApiAiBuild);

    }
}