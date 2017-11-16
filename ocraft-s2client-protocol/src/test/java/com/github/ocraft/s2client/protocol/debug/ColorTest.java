package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ColorTest {
    private static final int COLOR_R = 100;
    private static final int COLOR_G = 150;
    private static final int COLOR_B = 20;


    @Test
    void serializesToSc2ApiColor() {
        assertThatAllFieldsInRequestAreSerialized(Color.of(COLOR_R, COLOR_G, COLOR_B).toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Debug.Color sc2ApiColor) {
        assertThat(sc2ApiColor.getR()).as("sc2api color: r").isEqualTo(COLOR_R);
        assertThat(sc2ApiColor.getG()).as("sc2api color: g").isEqualTo(COLOR_G);
        assertThat(sc2ApiColor.getB()).as("sc2api color: b").isEqualTo(COLOR_B);
    }

    @Test
    void throwsExceptionWhenColorIsNotInValidRange() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Color.of(-1, 1, 1))
                .withMessage("color [r] has value -1 and is lower than 0");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Color.of(256, 1, 1))
                .withMessage("color [r] has value 256 and is greater than 255");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Color.of(1, -1, 1))
                .withMessage("color [g] has value -1 and is lower than 0");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Color.of(1, 256, 1))
                .withMessage("color [g] has value 256 and is greater than 255");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Color.of(1, 1, -1))
                .withMessage("color [b] has value -1 and is lower than 0");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Color.of(1, 1, 256))
                .withMessage("color [b] has value 256 and is greater than 255");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(Color.class).verify();
    }
}