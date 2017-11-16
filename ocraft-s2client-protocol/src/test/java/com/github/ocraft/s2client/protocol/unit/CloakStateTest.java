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

class CloakStateTest {

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "cloakStateMappings")
    void mapsSc2ApiCloakState(Raw.CloakState sc2ApiCloakState, CloakState expectedCloakState) {
        assertThat(CloakState.from(sc2ApiCloakState)).isEqualTo(expectedCloakState);
    }

    private static Stream<Arguments> cloakStateMappings() {
        return Stream.of(
                of(Raw.CloakState.NotCloaked, CloakState.NOT_CLOAKED),
                of(Raw.CloakState.Cloaked, CloakState.CLOAKED),
                of(Raw.CloakState.CloakedDetected, CloakState.CLOAKED_DETECTED));
    }

    @Test
    void throwsExceptionWhenCloakStateIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CloakState.from(nothing()))
                .withMessage("sc2api cloak state is required");
    }


}