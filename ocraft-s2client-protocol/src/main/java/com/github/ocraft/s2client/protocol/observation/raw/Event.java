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
