package com.github.ocraft.s2client.protocol.spatial;

import SC2APIProtocol.Common;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class Point2dTest {

    private static final float POINT_X = 10.3f;
    private static final float POINT_Y = 15.6f;

    @Test
    void throwsExceptionWhenSc2ApiPoint2DIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Point2d.from(nothing()))
                .withMessage("sc2api point2d is required");
    }

    @Test
    void convertsSc2ApiPoint2DToPoint2D() {
        Point2d point2D = Point2d.from(Common.Point2D.newBuilder().setX(POINT_X).setY(POINT_Y).build());
        assertThat(point2D.getX()).as("point2d: x").isEqualTo(POINT_X);
        assertThat(point2D.getY()).as("point2d: y").isEqualTo(POINT_Y);
    }

    @Test
    void throwsExceptionWhenSc2ApiPoint2DDoesNotHaveX() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Point2d.from(Common.Point2D.newBuilder().setY(POINT_Y).build()))
                .withMessage("x is required");
    }

    @Test
    void throwsExceptionWhenSc2ApiPoint2DDoesNotHaveY() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Point2d.from(Common.Point2D.newBuilder().setX(POINT_X).build()))
                .withMessage("y is required");
    }

    @Test
    void serializesToSc2ApiPoint2d() {
        assertThatAllFieldsInRequestAreSerialized(Point2d.of(POINT_X, POINT_Y).toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Common.Point2D sc2ApiPoint2d) {
        assertThat(sc2ApiPoint2d.getX()).as("sc2api point2d x").isEqualTo(POINT_X);
        assertThat(sc2ApiPoint2d.getY()).as("sc2api point2d y").isEqualTo(POINT_Y);
    }

    @Test
    void throwsExceptionWhenPoint2dIsNotInValidRange() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Point2d.of(-1, 1))
                .withMessage("point 2d [x] has value -1.0 and is lower than 0.0");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Point2d.of(256, 1))
                .withMessage("point 2d [x] has value 256.0 and is greater than 255.0");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Point2d.of(1, -1))
                .withMessage("point 2d [y] has value -1.0 and is lower than 0.0");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Point2d.of(1, 256))
                .withMessage("point 2d [y] has value 256.0 and is greater than 255.0");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(Point2d.class).verify();
    }

}