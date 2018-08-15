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
    void performsBasicMathOperations() {
        Point2d p0 = Point2d.of(2, 4);
        Point2d p1 = Point2d.of(5, 8);

        assertThat(p0.add(p1)).isEqualTo(Point2d.of(7, 12));
        assertThat(p1.sub(p0)).isEqualTo(Point2d.of(3, 4));
        assertThat(p0.div(2)).isEqualTo(Point2d.of(1, 2));
        assertThat(p1.mul(2)).isEqualTo(Point2d.of(10, 16));
        assertThat(p0.dot(p1)).isEqualTo(42);
        assertThat(p0.distance(p1)).isEqualTo(5.0);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(Point2d.class).verify();
    }

}
