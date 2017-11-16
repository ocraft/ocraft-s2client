package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import com.github.ocraft.s2client.protocol.Defaults;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.syntax.debug.DebugSphereBuilder;
import com.github.ocraft.s2client.protocol.syntax.debug.DebugSphereSyntax;
import com.github.ocraft.s2client.protocol.syntax.debug.WithRadiusSyntax;
import com.github.ocraft.s2client.protocol.syntax.debug.WithSphereColorSyntax;

import static com.github.ocraft.s2client.protocol.Preconditions.greaterThan;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class DebugSphere implements Sc2ApiSerializable<Debug.DebugSphere> {

    private static final long serialVersionUID = -7772580639024437922L;

    private static final double MIN_RADIUS = 0.0;

    private final Color color;
    private final Point center;
    private final float radius;

    public static final class Builder implements
            DebugSphereSyntax, WithRadiusSyntax, WithSphereColorSyntax, DebugSphereBuilder {

        private Color color = Defaults.COLOR;
        private Point center;
        private Float radius;

        @Override
        public WithRadiusSyntax on(Point center) {
            this.center = center;
            return this;
        }

        @Override
        public WithSphereColorSyntax withRadius(float radius) {
            this.radius = radius;
            return this;
        }

        @Override
        public DebugSphereBuilder withColor(Color color) {
            this.color = color;
            return this;
        }

        @Override
        public DebugSphere build() {
            require("color", color);
            require("center", center);
            require("radius", radius);
            greaterThan("radius", radius, MIN_RADIUS);
            return new DebugSphere(this);
        }
    }

    private DebugSphere(Builder builder) {
        color = builder.color;
        center = builder.center;
        radius = builder.radius;
    }

    public static DebugSphereSyntax sphere() {
        return new Builder();
    }

    @Override
    public Debug.DebugSphere toSc2Api() {
        return Debug.DebugSphere.newBuilder().setColor(color.toSc2Api()).setP(center.toSc2Api()).setR(radius).build();
    }

    public Color getColor() {
        return color;
    }

    public Point getCenter() {
        return center;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DebugSphere that = (DebugSphere) o;

        return Float.compare(that.radius, radius) == 0 && color.equals(that.color) && center.equals(that.center);
    }

    @Override
    public int hashCode() {
        int result = color.hashCode();
        result = 31 * result + center.hashCode();
        result = 31 * result + (radius != +0.0f ? Float.floatToIntBits(radius) : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
