package com.github.ocraft.s2client.protocol.unit;

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

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.spatial.Point;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class RallyTarget implements Serializable {

    private static final long serialVersionUID = -1401597859772600576L;

    // Will always be filled.
    private final Point point;
    // Only if it's targeting a unit.
    private final Tag tag;

    private RallyTarget(Raw.RallyTarget sc2ApiRallyTarget) {
        point = tryGet(Raw.RallyTarget::getPoint, Raw.RallyTarget::hasPoint)
                .apply(sc2ApiRallyTarget).map(Point::from).orElseThrow(required("point"));
        tag = tryGet(Raw.RallyTarget::getTag, Raw.RallyTarget::hasTag)
                .apply(sc2ApiRallyTarget).map(Tag::from).orElse(nothing());
    }

    public static RallyTarget from(Raw.RallyTarget sc2ApiRallyTarget) {
        require("sc2api rally target", sc2ApiRallyTarget);
        return new RallyTarget(sc2ApiRallyTarget);
    }

    public Point getPoint() { return point; }

    public Optional<Tag> getTag() {
        return Optional.ofNullable(tag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RallyTarget that = (RallyTarget) o;

        if (!point.equals(that.point)) return false;
        return Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        int result = point != null ? point.hashCode() : 0;
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
