package com.github.ocraft.s2client.protocol.spatial;

import SC2APIProtocol.Common;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiRectangleI;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RectangleITest {

    @Test
    void throwsExceptionWhenSc2ApiRectangleIIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> RectangleI.from(nothing()))
                .withMessage("sc2api rectanglei is required");
    }

    @Test
    void convertsSc2ApiRectangleIToRectangleI() {
        RectangleI rectangleI = RectangleI.from(sc2ApiRectangleI());
        assertThat(rectangleI.getP0()).as("rectanglei: p0").isNotNull();
        assertThat(rectangleI.getP1()).as("rectanglei: p1").isNotNull();
    }

    @Test
    void throwsExceptionWhenSc2ApiRectangleIDoesNotHaveP0() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> RectangleI.from(without(
                        () -> sc2ApiRectangleI().toBuilder(),
                        Common.RectangleI.Builder::clearP0).build()))
                .withMessage("p0 is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> RectangleI.of(nothing(), PointI.of(1, 1)))
                .withMessage("p0 is required");
    }

    @Test
    void throwsExceptionWhenSc2ApiRectangleIDoesNotHaveP1() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> RectangleI.from(without(
                        () -> sc2ApiRectangleI().toBuilder(),
                        Common.RectangleI.Builder::clearP1).build()))
                .withMessage("p1 is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> RectangleI.of(PointI.of(1, 1), nothing()))
                .withMessage("p1 is required");
    }

    @Test
    void serializesToSc2ApiRectangleI() {
        assertThatAllFieldsInRequestAreSerialized(RectangleI.of(PointI.of(1, 1), PointI.of(2, 2)).toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Common.RectangleI sc2ApiRectangleI) {
        assertThat(sc2ApiRectangleI.hasP0()).as("sc2api rectanglei: p0").isTrue();
        assertThat(sc2ApiRectangleI.hasP1()).as("sc2api rectanglei: p1").isTrue();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RectangleI.class).withNonnullFields("p0", "p1").verify();
    }
}