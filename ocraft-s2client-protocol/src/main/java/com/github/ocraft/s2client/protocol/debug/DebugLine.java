package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import com.github.ocraft.s2client.protocol.Defaults;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.syntax.debug.DebugLineBuilder;
import com.github.ocraft.s2client.protocol.syntax.debug.DebugLineSyntax;
import com.github.ocraft.s2client.protocol.syntax.debug.WithLineColorSyntax;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class DebugLine implements Sc2ApiSerializable<Debug.DebugLine> {

    private static final long serialVersionUID = -7390680325827864489L;

    private final Color color;
    private final Line line;

    public static final class Builder implements DebugLineSyntax, WithLineColorSyntax, DebugLineBuilder {

        private Color color = Defaults.COLOR;
        private Line line;

        @Override
        public WithLineColorSyntax of(Line line) {
            this.line = line;
            return this;
        }

        @Override
        public WithLineColorSyntax of(Point p0, Point p1) {
            this.line = Line.of(p0, p1);
            return this;
        }

        @Override
        public DebugLineBuilder withColor(Color color) {
            this.color = color;
            return this;
        }

        @Override
        public DebugLine build() {
            require("line", line);
            require("color", color);
            return new DebugLine(this);
        }
    }

    private DebugLine(Builder builder) {
        color = builder.color;
        line = builder.line;
    }

    public static DebugLineSyntax line() {
        return new Builder();
    }

    @Override
    public Debug.DebugLine toSc2Api() {
        return Debug.DebugLine.newBuilder().setColor(color.toSc2Api()).setLine(line.toSc2Api()).build();
    }

    public Color getColor() {
        return color;
    }

    public Line getLine() {
        return line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DebugLine debugLine = (DebugLine) o;

        return color.equals(debugLine.color) && line.equals(debugLine.line);
    }

    @Override
    public int hashCode() {
        int result = color.hashCode();
        result = 31 * result + line.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
