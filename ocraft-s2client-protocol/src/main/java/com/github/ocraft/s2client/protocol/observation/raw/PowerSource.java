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
package com.github.ocraft.s2client.protocol.observation.raw;

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.unit.Tag;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class PowerSource implements Serializable {

    private static final long serialVersionUID = 2443272338593956630L;

    private final Point position;
    private final float radius;
    private final Tag tag;

    private PowerSource(Raw.PowerSource sc2ApiPowerSource) {
        position = tryGet(
                Raw.PowerSource::getPos, Raw.PowerSource::hasPos
        ).apply(sc2ApiPowerSource).map(Point::from).orElseThrow(required("position"));

        radius = tryGet(
                Raw.PowerSource::getRadius, Raw.PowerSource::hasRadius
        ).apply(sc2ApiPowerSource).orElseThrow(required("radius"));

        tag = tryGet(
                Raw.PowerSource::getTag, Raw.PowerSource::hasTag
        ).apply(sc2ApiPowerSource).map(Tag::from).orElseThrow(required("tag"));
    }

    public static PowerSource from(Raw.PowerSource sc2ApiPowerSource) {
        require("sc2api power source", sc2ApiPowerSource);
        return new PowerSource(sc2ApiPowerSource);
    }

    public Point getPosition() {
        return position;
    }

    public float getRadius() {
        return radius;
    }

    public Tag getTag() {
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PowerSource that = (PowerSource) o;

        return Float.compare(that.radius, radius) == 0 && position.equals(that.position) && tag.equals(that.tag);
    }

    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + (radius != +0.0f ? Float.floatToIntBits(radius) : 0);
        result = 31 * result + tag.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
