package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;

import static com.github.ocraft.s2client.protocol.Preconditions.between;

public final class Color implements Sc2ApiSerializable<Debug.Color> {

    private static final long serialVersionUID = 7724713021780585305L;

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