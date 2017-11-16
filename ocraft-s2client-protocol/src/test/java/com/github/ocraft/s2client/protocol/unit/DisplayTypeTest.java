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

class DisplayTypeTest {
    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "displayTypeMappings")
    void mapsSc2ApiDisplayType(Raw.DisplayType sc2ApiDisplayType, DisplayType expectedDisplayType) {
        assertThat(DisplayType.from(sc2ApiDisplayType)).isEqualTo(expectedDisplayType);
    }

    private static Stream<Arguments> displayTypeMappings() {
        return Stream.of(
                of(Raw.DisplayType.Visible, DisplayType.VISIBLE),
                of(Raw.DisplayType.Hidden, DisplayType.HIDDEN),
                of(Raw.DisplayType.Snapshot, DisplayType.SNAPSHOT));
    }

    @Test
    void throwsExceptionWhenDisplayTypeIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DisplayType.from(nothing()))
                .withMessage("sc2api display type is required");
    }


}