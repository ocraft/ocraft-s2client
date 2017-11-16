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
package com.github.ocraft.s2client.protocol.action.observer;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverCameraFollowUnitsBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverCameraFollowUnitsSyntax;
import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.util.HashSet;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.Preconditions.requireNotEmpty;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

// since 4.0
public final class ActionObserverCameraFollowUnits
        implements Sc2ApiSerializable<Sc2Api.ActionObserverCameraFollowUnits> {

    private static final long serialVersionUID = 920184923321364185L;

    private final Set<Tag> units;

    public static final class Builder
            implements ActionObserverCameraFollowUnitsSyntax, ActionObserverCameraFollowUnitsBuilder {

        private Set<Tag> units = new HashSet<>();

        @Override
        public ActionObserverCameraFollowUnitsBuilder withTags(Tag... unitTag) {
            this.units.addAll(asList(unitTag));
            return this;
        }

        @Override
        public ActionObserverCameraFollowUnitsBuilder of(Unit... units) {
            this.units.addAll(stream(units).map(Unit::getTag).collect(toSet()));
            return this;
        }

        @Override
        public ActionObserverCameraFollowUnits build() {
            requireNotEmpty("units", units);
            return new ActionObserverCameraFollowUnits(this);
        }
    }

    private ActionObserverCameraFollowUnits(Builder builder) {
        units = builder.units;
    }

    public static ActionObserverCameraFollowUnitsSyntax cameraFollowUnits() {
        return new Builder();
    }

    @Override
    public Sc2Api.ActionObserverCameraFollowUnits toSc2Api() {
        return Sc2Api.ActionObserverCameraFollowUnits.newBuilder()
                .addAllUnitTags(units.stream().map(Tag::toSc2Api).collect(toSet()))
                .build();
    }

    public Set<Tag> getUnits() {
        return units;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionObserverCameraFollowUnits that = (ActionObserverCameraFollowUnits) o;

        return units.equals(that.units);
    }

    @Override
    public int hashCode() {
        return units.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
