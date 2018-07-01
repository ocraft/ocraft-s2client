package com.github.ocraft.s2client.protocol.debug;

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

import SC2APIProtocol.Debug;
import com.github.ocraft.s2client.protocol.Defaults;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.syntax.debug.*;

import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Preconditions.*;

public final class DebugText implements Sc2ApiSerializable<Debug.DebugText> {

    private static final long serialVersionUID = 4614430762986089060L;

    private static final float MIN_VIRTUAL_COORD = 0.0f;
    private static final float MAX_VIRTUAL_COORD = 1.0f;

    private final Color color;
    private final String text;      // Text to display.
    private final Point point3d;    // Position in the world.
    private final Point point2d;    // Virtualized position in 2D (the screen is 0..1, 0..1 for any resolution).
    // since 4.0
    private final Integer size;     // Pixel height of the text. Defaults to 8px.

    public static final class Builder implements DebugTextSyntax, WithSizeSyntax, WithTextColorSyntax, TextPositionSyntax {

        private Color color = Defaults.COLOR;
        private String text;
        private Point point2d;
        private Point point3d;
        private Integer size;

        @Override
        public WithTextColorSyntax of(String text) {
            this.text = text;
            return this;
        }

        @Override
        public WithSizeSyntax withColor(Color color) {
            this.color = color;
            return this;
        }

        @Override
        public TextPositionSyntax withSize(int size) {
            this.size = size;
            return this;
        }

        @Override
        public DebugTextBuilder on(Point point2d) {
            this.point2d = point2d;
            this.point3d = nothing();
            return this;
        }

        @Override
        public DebugTextBuilder onMap(Point point3d) {
            this.point3d = point3d;
            this.point2d = nothing();
            return this;
        }

        @Override
        public DebugText build() {
            require("color", color);
            require("text", text);
            if (isSet(point2d)) {
                between("virtualized point 2d [x]", point2d.getX(), MIN_VIRTUAL_COORD, MAX_VIRTUAL_COORD);
                between("virtualized point 2d [y]", point2d.getY(), MIN_VIRTUAL_COORD, MAX_VIRTUAL_COORD);
            }
            return new DebugText(this);
        }
    }

    private DebugText(Builder builder) {
        this.color = builder.color;
        this.size = builder.size;
        this.text = builder.text;
        this.point2d = builder.point2d;
        this.point3d = builder.point3d;
    }

    public static DebugTextSyntax text() {
        return new Builder();
    }

    @Override
    public Debug.DebugText toSc2Api() {
        Debug.DebugText.Builder aSc2ApiText = Debug.DebugText.newBuilder().setColor(color.toSc2Api()).setText(text);

        getSize().ifPresent(aSc2ApiText::setSize);
        getPoint2d().map(Point::toSc2Api).ifPresent(aSc2ApiText::setVirtualPos);
        getPoint3d().map(Point::toSc2Api).ifPresent(aSc2ApiText::setWorldPos);

        return aSc2ApiText.build();
    }

    public Color getColor() {
        return color;
    }

    public Optional<Integer> getSize() {
        return Optional.ofNullable(size);
    }

    public String getText() {
        return text;
    }

    public Optional<Point> getPoint3d() {
        return Optional.ofNullable(point3d);
    }

    public Optional<Point> getPoint2d() {
        return Optional.ofNullable(point2d);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DebugText debugText = (DebugText) o;

        return color.equals(debugText.color) &&
                text.equals(debugText.text) &&
                (size != null ? size.equals(debugText.size) : debugText.size == null) &&
                (point3d != null ? point3d.equals(debugText.point3d) : debugText.point3d == null) &&
                (point2d != null ? point2d.equals(debugText.point2d) : debugText.point2d == null);
    }

    @Override
    public int hashCode() {
        int result = color.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (point3d != null ? point3d.hashCode() : 0);
        result = 31 * result + (point2d != null ? point2d.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
