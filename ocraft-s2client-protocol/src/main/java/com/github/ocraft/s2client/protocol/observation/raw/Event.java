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
import com.github.ocraft.s2client.protocol.unit.Tag;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.toSet;

public final class Event implements Serializable {

    private static final long serialVersionUID = 6072651943852318775L;

    private final Set<Tag> deadUnits;

    private Event(Raw.Event sc2ApiEvent) {
        deadUnits = sc2ApiEvent.getDeadUnitsList().stream().map(Tag::from).collect(toSet());
    }

    public static Event from(Raw.Event sc2ApiEvent) {
        require("sc2api event", sc2ApiEvent);
        return new Event(sc2ApiEvent);
    }

    public Set<Tag> getDeadUnits() {
        return new HashSet<>(deadUnits);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return deadUnits.equals(event.deadUnits);
    }

    @Override
    public int hashCode() {
        return deadUnits.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
