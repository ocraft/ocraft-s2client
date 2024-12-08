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
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.between;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class Point implements Sc2ApiSerializable<Common.Point> {

    private static final long serialVersionUID = 5275787681411071166L;

    private static final float DEFAULT_Z = 0.0f;
    private static final float MIN_COORD = 0.0f;
    private static final float MAX_COORD = 255.0f;
    private static final float MIN_COORD_Z = -255.0f;
    private static final float MAX_COORD_Z = 255.0f;

    private final float x;
    private final float y;
    private final float z;

    private Point(Common.Point sc2ApiPoint) {
        this.x = tryGet(Common.Point::getX, Common.Point::hasX).apply(sc2ApiPoint).orElseThrow(required("x"));
        this.y = tryGet(Common.Point::getY, Common.Point::hasY).apply(sc2ApiPoint).orElseThrow(required("y"));
        this.z = tryGet(Common.Point::getZ, Common.Point::hasZ).apply(sc2ApiPoint).orElse(DEFAULT_Z);

        validate();
    }

    private void validate() {
        between("point [x]", x, MIN_COORD, MAX_COORD);
        between("point [y]", y, MIN_COORD, MAX_COORD);
        between("point [z]", z, MIN_COORD_Z, MAX_COORD_Z);
    }

    private Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Point of(float x, float y) {
        return new Point(x, y, DEFAULT_Z);
    }

    public static Point of(float x, float y, float z) {
        return new Point(x, y, z);
    }

    public static Point from(Common.Point sc2ApiPoint) {
        require("sc2api point", sc2ApiPoint);
        return new Point(sc2ApiPoint);
    }

    @Override
    public Common.Point toSc2Api() {
        validate();
        return Common.Point.newBuilder().setX(x).setY(y).setZ(z).build();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }


    public Point add(Point pointToAdd) {
        return new Point(x + pointToAdd.getX(), y + pointToAdd.getY(), z + pointToAdd.getZ());
    }

    public Point add(float addX, float addY, float addZ) {
        return new Point(x + addX, y + addY, z + addZ);
    }

    public Point sub(Point pointToSubtract) {
        return new Point(x - pointToSubtract.getX(), y - pointToSubtract.getY(), z - pointToSubtract.getZ());
    }

    public Point sub(float subX, float subY, float subZ) {
        return new Point(x - subX, y - subY, z - subZ);
    }

    public Point div(float divBy) {
        return new Point(x / divBy, y / divBy, z / divBy);
    }

    public Point mul(float mulBy) {
        return new Point(x * mulBy, y * mulBy, z * mulBy);
    }

    public double distance(Point b) {
        float x1 = x - b.getX();
        float y1 = y - b.getY();
        float z1 = z - b.getZ();

        return Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
    }

    public float dot(Point b) {
        return x * b.getX() + y * b.getY() + z * b.getZ();
    }

    public Point2d toPoint2d() {
        return Point2d.of(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (Float.compare(point.x, x) != 0) return false;
        if (Float.compare(point.y, y) != 0) return false;
        return Float.compare(point.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result = (x != 0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != 0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != 0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
