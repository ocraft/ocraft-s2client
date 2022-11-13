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

public final class Point2d implements Sc2ApiSerializable<Common.Point2D> {

    private static final long serialVersionUID = 3294505281879775593L;

    private static final float MIN_COORD = 0.0f;
    private static final float MAX_COORD = 255.0f;

    private final float x;
    private final float y;

    private Point2d(Common.Point2D sc2ApiPoint2d) {
        this.x = tryGet(Common.Point2D::getX, Common.Point2D::hasX).apply(sc2ApiPoint2d).orElseThrow(required("x"));
        this.y = tryGet(Common.Point2D::getY, Common.Point2D::hasY).apply(sc2ApiPoint2d).orElseThrow(required("y"));

        validate();
    }

    private void validate() {
        between("point 2d [x]", x, MIN_COORD, MAX_COORD);
        between("point 2d [y]", y, MIN_COORD, MAX_COORD);
    }

    private Point2d(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Point2d of(float x, float y) {
        return new Point2d(x, y);
    }

    public static Point2d from(Common.Point2D sc2ApiPoint2d) {
        require("sc2api point2d", sc2ApiPoint2d);
        return new Point2d(sc2ApiPoint2d);
    }

    @Override
    public Common.Point2D toSc2Api() {
        validate();
        return Common.Point2D.newBuilder().setX(x).setY(y).build();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Point2d add(Point2d pointToAdd) {
        return new Point2d(x + pointToAdd.getX(), y + pointToAdd.getY());
    }

    public Point2d add(float addX, float addY) {
        return new Point2d(x + addX, y + addY);
    }

    public Point2d sub(Point2d pointToSubtract) {
        return new Point2d(x - pointToSubtract.getX(), y - pointToSubtract.getY());
    }

    public Point2d sub(float subX, float subY) {
        return new Point2d(x - subX, y - subY);
    }

    public Point2d div(float divBy) {
        return new Point2d(x / divBy, y / divBy);
    }

    public Point2d mul(float mulBy) {
        return new Point2d(x * mulBy, y * mulBy);
    }

    public double distance(Point2d b) {
        float x1 = x - b.getX();
        float y1 = y - b.getY();

        return Math.sqrt(x1 * x1 + y1 * y1);
    }

    public float dot(Point2d b) {
        return x * b.getX() + y * b.getY();
    }

    public Point toPoint(float z) {
        return Point.of(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point2d point2d = (Point2d) o;

        if (Float.compare(point2d.x, x) != 0) return false;
        return Float.compare(point2d.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
