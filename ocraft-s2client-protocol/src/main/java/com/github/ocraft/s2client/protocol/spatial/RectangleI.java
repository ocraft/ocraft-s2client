package com.github.ocraft.s2client.protocol.spatial;

import SC2APIProtocol.Common;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class RectangleI implements Sc2ApiSerializable<Common.RectangleI> {

    private static final long serialVersionUID = -195431053688073972L;

    private final PointI p0;
    private final PointI p1;

    private RectangleI(Common.RectangleI sc2ApiRectangleI) {
        p0 = tryGet(Common.RectangleI::getP0, Common.RectangleI::hasP0)
                .apply(sc2ApiRectangleI).map(PointI::from).orElseThrow(required("p0"));
        p1 = tryGet(Common.RectangleI::getP1, Common.RectangleI::hasP1)
                .apply(sc2ApiRectangleI).map(PointI::from).orElseThrow(required("p1"));
    }

    private RectangleI(PointI p0, PointI p1) {
        this.p0 = p0;
        this.p1 = p1;
    }

    public static RectangleI from(Common.RectangleI sc2ApiRectangleI) {
        require("sc2api rectanglei", sc2ApiRectangleI);
        return new RectangleI(sc2ApiRectangleI);
    }

    public static RectangleI of(PointI p0, PointI p1) {
        require("p0", p0);
        require("p1", p1);
        return new RectangleI(p0, p1);
    }

    @Override
    public Common.RectangleI toSc2Api() {
        return Common.RectangleI.newBuilder().setP0(p0.toSc2Api()).setP1(p1.toSc2Api()).build();
    }

    public PointI getP0() {
        return p0;
    }

    public PointI getP1() {
        return p1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RectangleI that = (RectangleI) o;

        return p0.equals(that.p0) && p1.equals(that.p1);
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
