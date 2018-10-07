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
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.syntax.debug.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.ocraft.s2client.protocol.Preconditions.oneOfIsNotEmpty;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public final class DebugDraw implements Sc2ApiSerializable<Debug.DebugDraw> {

    private static final long serialVersionUID = -4496353812808919189L;

    private final List<DebugText> texts;
    private final List<DebugLine> lines;
    private final List<DebugBox> boxes;
    private final List<DebugSphere> spheres;

    public static final class Builder implements DebugDrawSyntax, BuilderSyntax<DebugDraw> {

        private List<DebugText> texts = new ArrayList<>();
        private List<DebugLine> lines = new ArrayList<>();
        private List<DebugBox> boxes = new ArrayList<>();
        private List<DebugSphere> spheres = new ArrayList<>();

        @Override
        public Builder texts(DebugText... texts) {
            this.texts.addAll(asList(texts));
            return this;
        }

        @Override
        public final Builder texts(DebugTextBuilder... texts) {
            this.texts.addAll(BuilderSyntax.buildAll(texts));
            return this;
        }

        @Override
        public Builder lines(DebugLine... lines) {
            this.lines.addAll(asList(lines));
            return this;
        }

        @Override
        public final Builder lines(DebugLineBuilder... lines) {
            this.lines.addAll(BuilderSyntax.buildAll(lines));
            return this;
        }

        @Override
        public Builder boxes(DebugBox... boxes) {
            this.boxes.addAll(asList(boxes));
            return this;
        }

        @Override
        public final Builder boxes(DebugBoxBuilder... boxes) {
            this.boxes.addAll(BuilderSyntax.buildAll(boxes));
            return this;
        }

        @Override
        public Builder spheres(DebugSphere... spheres) {
            this.spheres.addAll(asList(spheres));
            return this;
        }

        @Override
        public final Builder spheres(DebugSphereBuilder... spheres) {
            this.spheres.addAll(BuilderSyntax.buildAll(spheres));
            return this;
        }

        @Override
        public DebugDraw build() {
            oneOfIsNotEmpty("draw elements", texts, lines, boxes, spheres);
            return new DebugDraw(this);
        }
    }

    private DebugDraw(Builder builder) {
        texts = Collections.unmodifiableList(builder.texts);
        lines = Collections.unmodifiableList(builder.lines);
        boxes = Collections.unmodifiableList(builder.boxes);
        spheres = Collections.unmodifiableList(builder.spheres);
    }

    public static DebugDrawSyntax draw() {
        return new Builder();
    }

    @Override
    public Debug.DebugDraw toSc2Api() {
        return Debug.DebugDraw.newBuilder()
                .addAllText(texts.stream().map(DebugText::toSc2Api).collect(toList()))
                .addAllLines(lines.stream().map(DebugLine::toSc2Api).collect(toList()))
                .addAllBoxes(boxes.stream().map(DebugBox::toSc2Api).collect(toList()))
                .addAllSpheres(spheres.stream().map(DebugSphere::toSc2Api).collect(toList()))
                .build();
    }

    public List<DebugText> getTexts() {
        return texts;
    }

    public List<DebugLine> getLines() {
        return lines;
    }

    public List<DebugBox> getBoxes() {
        return boxes;
    }

    public List<DebugSphere> getSpheres() {
        return spheres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DebugDraw debugDraw = (DebugDraw) o;

        return texts.equals(debugDraw.texts) &&
                lines.equals(debugDraw.lines) &&
                boxes.equals(debugDraw.boxes) &&
                spheres.equals(debugDraw.spheres);
    }

    @Override
    public int hashCode() {
        int result = texts.hashCode();
        result = 31 * result + lines.hashCode();
        result = 31 * result + boxes.hashCode();
        result = 31 * result + spheres.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
