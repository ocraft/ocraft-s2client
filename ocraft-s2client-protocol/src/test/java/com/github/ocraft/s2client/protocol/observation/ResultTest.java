package com.github.ocraft.s2client.protocol.observation;

import SC2APIProtocol.Sc2Api;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResultTest {

    @Test
    void throwsExceptionWhenSc2ApiResultIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Result.from(nothing()))
                .withMessage("sc2api result is required");
    }

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "resultMappings")
    void mapsSc2ApiResult(Sc2Api.Result sc2ApiResult, Result expectedResult) {
        assertThat(Result.from(sc2ApiResult)).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> resultMappings() {
        return Stream.of(
                Arguments.of(Sc2Api.Result.Victory, Result.VICTORY),
                Arguments.of(Sc2Api.Result.Defeat, Result.DEFEAT),
                Arguments.of(Sc2Api.Result.Tie, Result.TIE),
                Arguments.of(Sc2Api.Result.Undecided, Result.UNDECIDED)
        );
    }
}