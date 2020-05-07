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
import com.github.ocraft.s2client.protocol.OsCheck;
import com.github.ocraft.s2client.protocol.OsCheck.OSType;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;

import static com.github.ocraft.s2client.protocol.Preconditions.between;

public final class Color implements Sc2ApiSerializable<Debug.Color> {

    private static final long serialVersionUID = 7724713021780585305L;

    public static final Color WHITE = new Color(255, 255, 255);
    public static final Color RED = new Color(255, 0, 0);
    public static final Color GREEN = new Color(0, 255, 0);
    public static final Color YELLOW = new Color(255, 255, 0);
    public static final Color BLUE = new Color(0, 0, 255);
    public static final Color TEAL = new Color(0, 255, 255);
    public static final Color PURPLE = new Color(255, 0, 255);
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color GRAY = new Color(128, 128, 128);

    private final int r;
    private final int g;
    private final int b;

    private Color(int r, int g, int b) {
        between("color [r]", r, 0, 255);
        between("color [g]", g, 0, 255);
        between("color [b]", b, 0, 255);

        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static Color of(int r, int g, int b) {
        return new Color(r, g, b);
    }

    @Override
    public Debug.Color toSc2Api() {
        if(OsCheck.getOSType() == OSType.MacOS) {
            // MacOS has BGR channels instead of a RGB.
            return Debug.Color.newBuilder().setR(b).setG(g).setB(r).build();
        }
        // All other OS types
        return Debug.Color.newBuilder().setR(r).setG(g).setB(b).build();
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Color color = (Color) o;

        return r == color.r && g == color.g && b == color.b;
    }

    @Override
    public int hashCode() {
        int result = r;
        result = 31 * result + g;
        result = 31 * result + b;
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
