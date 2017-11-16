/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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