package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import com.github.ocraft.s2client.protocol.Defaults;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.syntax.debug.DebugBoxBuilder;
import com.github.ocraft.s2client.protocol.syntax.debug.DebugBoxSyntax;
import com.github.ocraft.s2client.protocol.syntax.debug.WithBoxColorSyntax;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class DebugBox implements Sc2ApiSerializable<Debug.DebugBox> {

    private static final long serialVersionUID = 8185080227687554966L;

    private final Color color;
    private final Point min;
    private final Point max;

    public static final class Builder implements DebugBoxSyntax, WithBoxColorSyntax, DebugBoxBuilder {

        private Color color = Defaults.COLOR;
        private Point min;
        private Point max;

        @Override
        public DebugBoxBuilder withColor(Color color) {
            this.color = color;
            return this;
        }

        @Override
        public WithBoxColorSyntax of(Point min, Point max) {
            this.min = min;
            this.max = max;
            return this;
        }

        @Override
        public DebugBox build() {
            require("color", color);
            require("min", min);
            require("max", max);
            return new DebugBox(this);
        }
    }

    private DebugBox(Builder builder) {
        color = builder.color;
        min = builder.min;
        max = builder.max;
    }

    public static DebugBoxSyntax box() {
        return new Builder();
    }

    @Override
    public Debug.DebugBox toSc2Api() {
        return Debug.DebugBox.newBuilder()
                .setColor(color.toSc2Api())
                .setMin(min.toSc2Api())
                .setMax(max.toSc2Api())
                .build();
    }

    public Color getColor() {
        return color;
    }

    public Point getMin() {
        return min;
    }

    public Point getMax() {
        return max;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DebugBox debugBox = (DebugBox) o;

        return color.equals(debugBox.color) && min.equals(debugBox.min) && max.equals(debugBox.max);
    }

    @Override
    public int hashCode() {
        int result = color.hashCode();
        result = 31 * result + min.hashCode();
        result = 31 * result + max.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
