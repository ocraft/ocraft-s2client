package com.github.ocraft.s2client.protocol.spatial;

/*-
 * #%L
 * ocraft-s2client-protocol
 * %%
 * Copyright (C) 2017 - 2018 Ocraft Project
 * %%
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
 * #L%
 */

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
    void performsBasicMathOperations() {
        PointI p0 = PointI.of(2, 4);
        PointI p1 = PointI.of(5, 8);

        assertThat(p0.add(p1)).isEqualTo(PointI.of(7, 12));
        assertThat(p1.sub(p0)).isEqualTo(PointI.of(3, 4));
        assertThat(p0.div(2)).isEqualTo(PointI.of(1, 2));
        assertThat(p1.mul(2)).isEqualTo(PointI.of(10, 16));
        assertThat(p0.dot(p1)).isEqualTo(42);
        assertThat(p0.distance(p1)).isEqualTo(5.0);
    }

    @Test
    void hasPoint2dView() {
        assertThat(PointI.of(4, 5).toPoint2d()).isEqualTo(Point2d.of(4.0f, 5.0f));
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(PointI.class).verify();
    }
}
