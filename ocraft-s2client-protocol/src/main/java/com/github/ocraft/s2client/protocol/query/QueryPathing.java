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
package com.github.ocraft.s2client.protocol.query;

import SC2APIProtocol.Query;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.syntax.query.ToSyntax;
import com.github.ocraft.s2client.protocol.unit.Tag;

import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Preconditions.oneOfIsSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class QueryPathing implements Sc2ApiSerializable<Query.RequestQueryPathing> {

    private static final long serialVersionUID = 6728445967183980125L;

    private final Point2d start;
    private final Tag unitTag;
    private final Point2d end;

    public static final class Builder implements QueryPathingSyntax, ToSyntax, QueryPathingBuilder {

        private Point2d start;
        private Tag unitTag;
        private Point2d end;

        @Override
        public ToSyntax from(Point2d start) {
            this.start = start;
            this.unitTag = nothing();
            return this;
        }

        @Override
        public ToSyntax from(Tag unitTag) {
            this.unitTag = unitTag;
            this.start = nothing();
            return this;
        }

        @Override
        public QueryPathingBuilder to(Point2d end) {
            this.end = end;
            return this;
        }

        @Override
        public QueryPathing build() {
            require("end", end);
            oneOfIsSet("start case", start, unitTag);
            return new QueryPathing(this);
        }
    }

    private QueryPathing(QueryPathing.Builder builder) {
        this.start = builder.start;
        this.unitTag = builder.unitTag;
        this.end = builder.end;
    }

    public static QueryPathingSyntax path() {
        return new QueryPathing.Builder();
    }

    @Override
    public Query.RequestQueryPathing toSc2Api() {
        Query.RequestQueryPathing.Builder aSc2ApiPathing = Query.RequestQueryPathing.newBuilder()
                .setEndPos(end.toSc2Api());

        getStart().map(Point2d::toSc2Api).ifPresent(aSc2ApiPathing::setStartPos);
        getUnitTag().map(Tag::toSc2Api).ifPresent(aSc2ApiPathing::setUnitTag);

        return aSc2ApiPathing.build();
    }

    public Optional<Point2d> getStart() {
        return Optional.ofNullable(start);
    }

    public Optional<Tag> getUnitTag() {
        return Optional.ofNullable(unitTag);
    }

    public Point2d getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryPathing that = (QueryPathing) o;

        return (start != null ? start.equals(that.start) : that.start == null) &&
                (unitTag != null ? unitTag.equals(that.unitTag) : that.unitTag == null) &&
                end.equals(that.end);
    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (unitTag != null ? unitTag.hashCode() : 0);
        result = 31 * result + end.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
