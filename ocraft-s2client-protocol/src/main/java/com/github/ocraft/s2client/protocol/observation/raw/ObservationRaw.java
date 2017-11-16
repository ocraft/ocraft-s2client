package com.github.ocraft.s2client.protocol.observation.raw;

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.toSet;

public final class ObservationRaw implements Serializable {

    private static final long serialVersionUID = -2220809398131692734L;

    private final PlayerRaw player;
    private final Set<Unit> units;
    private final MapState mapState;
    private final Event event;
    private final Set<EffectLocations> effects;

    private ObservationRaw(Raw.ObservationRaw sc2ApiObservationRaw) {
        player = tryGet(
                Raw.ObservationRaw::getPlayer, Raw.ObservationRaw::hasPlayer
        ).apply(sc2ApiObservationRaw).map(PlayerRaw::from).orElseThrow(required("player"));

        units = sc2ApiObservationRaw.getUnitsList().stream().map(Unit::from).collect(toSet());

        mapState = tryGet(
                Raw.ObservationRaw::getMapState, Raw.ObservationRaw::hasMapState
        ).apply(sc2ApiObservationRaw).map(MapState::from).orElseThrow(required("map state"));

        event = tryGet(
                Raw.ObservationRaw::getEvent, Raw.ObservationRaw::hasEvent
        ).apply(sc2ApiObservationRaw).map(Event::from).orElse(nothing());

        effects = sc2ApiObservationRaw.getEffectsList().stream().map(EffectLocations::from).collect(toSet());
    }

    public static ObservationRaw from(Raw.ObservationRaw sc2ApiObservationRaw) {
        require("sc2api observation raw", sc2ApiObservationRaw);
        return new ObservationRaw(sc2ApiObservationRaw);
    }

    public PlayerRaw getPlayer() {
        return player;
    }

    public Set<Unit> getUnits() {
        return new HashSet<>(units);
    }

    public MapState getMapState() {
        return mapState;
    }

    public Optional<Event> getEvent() {
        return Optional.ofNullable(event);
    }

    public Set<EffectLocations> getEffects() {
        return new HashSet<>(effects);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObservationRaw that = (ObservationRaw) o;

        return player.equals(that.player) &&
                units.equals(that.units) &&
                mapState.equals(that.mapState) &&
                (event != null ? event.equals(that.event) : that.event == null) &&
                effects.equals(that.effects);
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + units.hashCode();
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
