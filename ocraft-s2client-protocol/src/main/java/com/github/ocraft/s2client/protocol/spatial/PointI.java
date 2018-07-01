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
import static com.github.ocraft.s2client.protocol.Preconditions.greaterOrEqual;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class PointI implements Sc2ApiSerializable<Common.PointI> {

    private static final long serialVersionUID = 5800017909513794226L;

    private static final int MIN_COORD = 0;

    private final int x;
    private final int y;

    private PointI(Common.PointI sc2ApiPointI) {
        this.x = tryGet(Common.PointI::getX, Common.PointI::hasX).apply(sc2ApiPointI).orElseThrow(required("x"));
        this.y = tryGet(Common.PointI::getY, Common.PointI::hasY).apply(sc2ApiPointI).orElseThrow(required("y"));

        validate();
    }

    private void validate() {
        greaterOrEqual("pointi [x]", x, MIN_COORD);
        greaterOrEqual("pointi [y]", y, MIN_COORD);
    }

    private PointI(int x, int y) {
        this.x = x;
        this.y = y;

        validate();
    }

    public static PointI from(Common.PointI sc2ApiPointI) {
        require("sc2api pointi", sc2ApiPointI);
        return new PointI(sc2ApiPointI);
    }

    public static PointI of(int x, int y) {
        return new PointI(x, y);
    }

    @Override
    public Common.PointI toSc2Api() {
        return Common.PointI.newBuilder().setX(x).setY(y).build();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointI pointI = (PointI) o;

        return x == pointI.x && y == pointI.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
