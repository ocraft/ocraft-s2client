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
package com.github.ocraft.s2client.protocol.spatial;

import SC2APIProtocol.Common;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.greaterOrEqual;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class Size2dI implements Sc2ApiSerializable<Common.Size2DI> {

    private static final long serialVersionUID = -5457165647885578514L;

    private final int x;
    private final int y;

    private Size2dI(Common.Size2DI sc2ApiSize2dI) {
        x = tryGet(Common.Size2DI::getX, Common.Size2DI::hasX).apply(sc2ApiSize2dI).orElseThrow(required("x"));
        y = tryGet(Common.Size2DI::getY, Common.Size2DI::hasY).apply(sc2ApiSize2dI).orElseThrow(required("y"));

        validate();
    }

    private void validate() {
        greaterOrEqual("size2di [x]", x, 0);
        greaterOrEqual("size2di [y]", y, 0);
    }

    private Size2dI(int x, int y) {
        this.x = x;
        this.y = y;

        validate();
    }

    public static Size2dI from(Common.Size2DI sc2ApiSize2dI) {
        require("sc2api size2dI", sc2ApiSize2dI);
        return new Size2dI(sc2ApiSize2dI);
    }

    @Override
    public Common.Size2DI toSc2Api() {
        return Common.Size2DI.newBuilder().setX(x).setY(y).build();
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

        Size2dI size2dI = (Size2dI) o;

        return x == size2dI.x && y == size2dI.y;
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

    public static Size2dI of(int x, int y) {
        return new Size2dI(x, y);
    }
}
