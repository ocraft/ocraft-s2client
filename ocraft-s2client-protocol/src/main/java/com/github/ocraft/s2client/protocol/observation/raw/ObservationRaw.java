package com.github.ocraft.s2client.protocol.observation.raw;

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
import com.github.ocraft.s2client.protocol.unit.Unit;
import com.github.ocraft.s2client.protocol.unit.UnitSnapshot;

import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

public final class ObservationRaw implements Serializable {

    private static final long serialVersionUID = -2220809398131692734L;

    private final PlayerRaw player;
    private final Set<Unit> units;
    private final Set<UnitSnapshot> unitSnapshots;
    private final MapState mapState;
    private final Event event;
    private final Set<EffectLocations> effects;

    private ObservationRaw(Raw.ObservationRaw sc2ApiObservationRaw) {
        player = tryGet(
                Raw.ObservationRaw::getPlayer, Raw.ObservationRaw::hasPlayer
        ).apply(sc2ApiObservationRaw).map(PlayerRaw::from).orElseThrow(required("player"));

        units = sc2ApiObservationRaw.getUnitsList().stream()
                .filter(Raw.Unit::hasTag)
                .map(Unit::from)
                .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));

        unitSnapshots = sc2ApiObservationRaw.getUnitsList().stream()
                .filter(rawUnit -> !rawUnit.hasTag())
                .map(UnitSnapshot::from)
                .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));

        mapState = tryGet(
                Raw.ObservationRaw::getMapState, Raw.ObservationRaw::hasMapState
        ).apply(sc2ApiObservationRaw).map(MapState::from).orElseThrow(required("map state"));

        event = tryGet(
                Raw.ObservationRaw::getEvent, Raw.ObservationRaw::hasEvent
        ).apply(sc2ApiObservationRaw).map(Event::from).orElse(nothing());

        effects = sc2ApiObservationRaw.getEffectsList().stream().map(EffectLocations::from)
                .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
    }

    public static ObservationRaw from(Raw.ObservationRaw sc2ApiObservationRaw) {
        require("sc2api observation raw", sc2ApiObservationRaw);
        return new ObservationRaw(sc2ApiObservationRaw);
    }

    public PlayerRaw getPlayer() {
        return player;
    }

    public Set<Unit> getUnits() {
        return units;
    }

    public Set<UnitSnapshot> getUnitSnapshots() {
        return unitSnapshots;
    }

    public MapState getMapState() {
        return mapState;
    }

    public Optional<Event> getEvent() {
        return Optional.ofNullable(event);
    }

    public Set<EffectLocations> getEffects() {
        return effects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObservationRaw that = (ObservationRaw) o;

        if (!player.equals(that.player)) return false;
        if (!units.equals(that.units)) return false;
        if (!unitSnapshots.equals(that.unitSnapshots)) return false;
        if (!mapState.equals(that.mapState)) return false;
        if (!Objects.equals(event, that.event)) return false;
        return effects.equals(that.effects);
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + units.hashCode();
        result = 31 * result + unitSnapshots.hashCode();
        result = 31 * result + mapState.hashCode();
        result = 31 * result + (event != null ? event.hashCode() : 0);
        result = 31 * result + effects.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
