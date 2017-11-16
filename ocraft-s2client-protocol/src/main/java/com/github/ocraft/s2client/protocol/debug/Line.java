/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
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
 */
package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.spatial.Point;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class Line implements Sc2ApiSerializable<Debug.Line> {

    private static final long serialVersionUID = 8937656775616894733L;

    private final Point p0;
    private final Point p1;

    public static Line of(Point p0, Point p1) {
        require("p0", p0);
        require("p1", p1);
        return new Line(p0, p1);
    }

    private Line(Point p0, Point p1) {
        this.p0 = p0;
        this.p1 = p1;
    }

    @Override
    public Debug.Line toSc2Api() {
        return Debug.Line.newBuilder().setP0(p0.toSc2Api()).setP1(p1.toSc2Api()).build();
    }

    public Point getP0() {
        return p0;
    }

    public Point getP1() {
        return p1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;

        return p0.equals(line.p0) && p1.equals(line.p1);
    }

    @Override
    public int hashCode() {
        int result = p0.hashCode();
        result = 31 * result + p1.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
