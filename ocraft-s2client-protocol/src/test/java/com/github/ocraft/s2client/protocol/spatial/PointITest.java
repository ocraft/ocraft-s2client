package com.github.ocraft.s2client.protocol.spatial;

import SC2APIProtocol.Common;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PointITest {
    private static final int POINT_X = 40;
    private static final int POINT_Y = 50;

    @Test
    void throwsExceptionWhenSc2ApiPointIIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PointI.from(nothing()))
                .withMessage("sc2api pointi is required");
    }

    @Test
    void convertsSc2ApiPointIToPointI() {
        PointI pointI = PointI.from(Common.PointI.newBuilder().setX(POINT_X).setY(POINT_Y).build());
        assertThat(pointI.getX()).as("pointi: x").isEqualTo(POINT_X);
        assertThat(pointI.getY()).as("pointi: y").isEqualTo(POINT_Y);
    }

    @Test
    void throwsExceptionWhenSc2ApiPointIDoesNotHaveX() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PointI.from(Common.PointI.newBuilder().setY(POINT_Y).build()))
                .withMessage("x is required");
    }

    @Test
    void throwsExceptionWhenSc2ApiPointIDoesNotHaveY() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PointI.from(Common.PointI.newBuilder().setX(POINT_X).build()))
                .withMessage("y is required");
    }

    @Test
    void serializesToSc2ApiPointI() {
        assertThatAllFieldsInRequestAreSerialized(PointI.of(POINT_X, POINT_Y).toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Common.PointI sc2ApiPointI) {
        assertThat(sc2ApiPointI.getX()).as("sc2api pointi x").isEqualTo(POINT_X);
        assertThat(sc2ApiPointI.getY()).as("sc2api pointi y").isEqualTo(POINT_Y);
    }

    @Test
    void throwsExceptionWhenPointIIsNotInValidRange() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PointI.of(-1, 1))
                .withMessage("pointi [x] has value -1 and is lower than 0");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PointI.of(1, -1))
                .withMessage("pointi [y] has value -1 and is lower than 0");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(PointI.class).verify();
    }
}