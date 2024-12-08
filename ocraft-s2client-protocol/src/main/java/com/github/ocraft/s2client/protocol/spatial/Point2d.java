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
import com.github.ocraft.s2client.protocol.unit.Unit;

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
        return Math.hypot(x - b.getX(), y - b.getY());
    }

    public float dot(Point2d b) {
        return x * b.getX() + y * b.getY();
    }

    public Point toPoint(float z) {
        return Point.of(x, y, z);
    }

    public float getAngle(Unit b) {
        return getAngle(b.getPosition().toPoint2d());
    }
    // 0 = right, pi/2 = up, pi = left, 3pi/2 = down
    public float getAngle(Point2d b) {
        return (float)((Math.atan2(b.getY() - y, b.getX() - x) + Math.PI*2) % (Math.PI*2));
    }

    public Point2d addDistanceByAngle(double angleInRads, float distance) {
        return Point2d.of(
                distance * (float)Math.cos(angleInRads) + x,
                distance * (float)Math.sin(angleInRads) + y
        );
    }

    public Point2d midPoint(Point2d b) {
        return this.add(b).div(2);
    }

    public Point2d rotate(Point2d pivotPoint, double angleInRads) {
        double sin = Math.sin(angleInRads);
        double cos = Math.cos(angleInRads);

        //subtract pivot point
        Point2d origin = this.sub(pivotPoint);

        //rotate point
        float xNew = (float)(origin.getX() * cos - origin.getY() * sin);
        float yNew = (float)(origin.getX() * sin + origin.getY() * cos);

        //add back the pivot point
        float x = xNew + pivotPoint.getX();
        float y = yNew + pivotPoint.getY();

        return Point2d.of(x, y);
    }

    public Point2d roundToHalfPointAccuracy() {
        return Point2d.of(Math.round(x * 2) / 2f, Math.round(y * 2) / 2f);
    }

    //useful for 1x1, 3x3, and 5x5 structure placements
    public Point2d toNearestHalfPoint() {
        return Point2d.of((int)x + 0.5f, (int)y + 0.5f);
    }

    //useful for 2x2 structure placements
    public Point2d toNearestWholePoint() {
        return Point2d.of(Math.round(x), Math.round(y));
    }

    public Point2d towards(Unit b, float distance) {
        return towards(b.getPosition().toPoint2d(), distance);
    }

    public Point2d towards(Point2d b, float distance) {
        if (this.equals(b)) {
            return b;
        }
        Point2d vector = unitVector(b);
        return this.add(vector.mul(distance));
    }

    public Point2d unitVector(Point2d b) {
        return b.sub(this).normalize();
    }

    public Point2d normalize() {
        float length = (float)Math.hypot(x, y);
        return this.div(length);
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
        int result = (x != 0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != 0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
