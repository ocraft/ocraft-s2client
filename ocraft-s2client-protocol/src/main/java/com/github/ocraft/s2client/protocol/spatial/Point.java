package com.github.ocraft.s2client.protocol.spatial;

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
        between("point [z]", z, MIN_COORD, MAX_COORD);
    }

    private Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;

        validate();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        return Float.compare(point.x, x) == 0 && Float.compare(point.y, y) == 0 && Float.compare(point.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
