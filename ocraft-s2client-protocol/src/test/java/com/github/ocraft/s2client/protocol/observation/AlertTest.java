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
import static org.junit.jupiter.params.provider.Arguments.of;

class AlertTest {
    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "alertMappings")
    void mapsSc2ApiAlert(Sc2Api.Alert sc2ApiAlert, Alert expectedAlert) {
        assertThat(Alert.from(sc2ApiAlert)).isEqualTo(expectedAlert);
    }

    private static Stream<Arguments> alertMappings() {
        return Stream.of(
                of(Sc2Api.Alert.NuclearLaunchDetected, Alert.NUCLEAR_LAUNCH_DETECTED),
                of(Sc2Api.Alert.NydusWormDetected, Alert.NYDUS_WORM_DETECTED));
    }

    @Test
    void throwsExceptionWhenAlertIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Alert.from(nothing()))
                .withMessage("sc2api alert is required");
    }
}