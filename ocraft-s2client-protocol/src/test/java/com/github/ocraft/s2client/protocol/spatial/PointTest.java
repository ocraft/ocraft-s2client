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

class PointTest {
    private static final float POINT_X = 10.3f;
    private static final float POINT_Y = 15.6f;
    private static final float POINT_Z = 2.0f;

    @Test
    void throwsExceptionWhenSc2ApiPointIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Point.from(nothing()))
                .withMessage("sc2api point is required");
    }

    @Test
    void convertsSc2ApiPointToPoint() {
        Point point = Point.from(Common.Point.newBuilder().setX(POINT_X).setY(POINT_Y).setZ(POINT_Z).build());
        assertThat(point.getX()).as("point: x").isEqualTo(POINT_X);
        assertThat(point.getY()).as("point: y").isEqualTo(POINT_Y);
        assertThat(point.getZ()).as("point: z").isEqualTo(POINT_Z);
    }

    @Test
    void throwsExceptionWhenSc2ApiPointDoesNotHaveX() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Point.from(Common.Point.newBuilder().setY(POINT_Y).setZ(POINT_Z).build()))
                .withMessage("x is required");
    }

    @Test
    void throwsExceptionWhenSc2ApiPointDoesNotHaveY() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Point.from(Common.Point.newBuilder().setX(POINT_X).setZ(POINT_Z).build()))
                .withMessage("y is required");
    }

    @Test
    void hasDefaultValueForZIfNotProvided() {
        assertThat(Point.from(Common.Point.newBuilder().setX(POINT_X).setY(POINT_Y).build()).getZ())
                .as("point: default value of z").isEqualTo(0.0f);
    }

    @Test
    void serializesToSc2ApiPoint() {
        assertThatAllFieldsInRequestAreSerialized(Point.of(POINT_X, POINT_Y, POINT_Z).toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Common.Point sc2ApiPoint) {
        assertThat(sc2ApiPoint.getX()).as("sc2api point: x").isEqualTo(POINT_X);
        assertThat(sc2ApiPoint.getY()).as("sc2api point: y").isEqualTo(POINT_Y);
        assertThat(sc2ApiPoint.getZ()).as("sc2api point: z").isEqualTo(POINT_Z);
    }

    @Test
    void throwsExceptionWhenPointIsNotInValidRange() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Point.of(-1, 1, 1).toSc2Api())
                .withMessage("point [x] has value -1.0 and is lower than 0.0");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Point.of(256, 1, 1).toSc2Api())
                .withMessage("point [x] has value 256.0 and is greater than 255.0");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Point.of(1, -1, 1).toSc2Api())
                .withMessage("point [y] has value -1.0 and is lower than 0.0");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Point.of(1, 256, 1).toSc2Api())
                .withMessage("point [y] has value 256.0 and is greater than 255.0");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Point.of(1, 1, -256).toSc2Api())
                .withMessage("point [z] has value -256.0 and is lower than -255.0");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Point.of(1, 1, 256).toSc2Api())
                .withMessage("point [z] has value 256.0 and is greater than 255.0");
    }

    @Test
    void performsBasicMathOperations() {
        Point p0 = Point.of(2, 4, 1);
        Point p1 = Point.of(5, 8, 1);

        assertThat(p0.add(p1)).isEqualTo(Point.of(7, 12, 2));
        assertThat(p1.sub(p0)).isEqualTo(Point.of(3, 4, 0));
        assertThat(p0.div(2)).isEqualTo(Point.of(1, 2, 0.5f));
        assertThat(p1.mul(2)).isEqualTo(Point.of(10, 16, 2));
        assertThat(p0.dot(p1)).isEqualTo(43);
        assertThat(p0.distance(p1)).isEqualTo(5.0);
    }

    @Test
    void hasPoint2dView() {
        assertThat(Point.of(4, 5, 7).toPoint2d()).isEqualTo(Point2d.of(4.0f, 5.0f));
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(Point.class).verify();
    }

}
