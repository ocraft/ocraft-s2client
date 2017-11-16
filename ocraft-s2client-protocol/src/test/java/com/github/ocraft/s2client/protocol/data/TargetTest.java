package com.github.ocraft.s2client.protocol.data;

import SC2APIProtocol.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class TargetTest {
    @ParameterizedTest(name = "\"{0}\" is serialization of {1}")
    @MethodSource(value = "targetMappings")
    void mapsSc2ApiTarget(Data.AbilityData.Target sc2ApiTarget, Target expectedTarget) {
        assertThat(Target.from(sc2ApiTarget)).isEqualTo(expectedTarget);
    }

    private static Stream<Arguments> targetMappings() {
        return Stream.of(
                Arguments.of(Data.AbilityData.Target.None, Target.NONE),
                Arguments.of(Data.AbilityData.Target.Point, Target.POINT),
                Arguments.of(Data.AbilityData.Target.Unit, Target.UNIT),
                Arguments.of(Data.AbilityData.Target.PointOrUnit, Target.POINT_OR_UNIT),
                Arguments.of(Data.AbilityData.Target.PointOrNone, Target.POINT_OR_NONE));
    }

    @Test
    void throwsExceptionWhenTargetIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Target.from(nothing()))
                .withMessage("sc2api target is required");
    }
}