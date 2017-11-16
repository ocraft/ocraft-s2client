package com.github.ocraft.s2client.protocol.observation.raw;

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.Effect;
import com.github.ocraft.s2client.protocol.data.Effects;
import com.github.ocraft.s2client.protocol.spatial.Point2d;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.toSet;

public final class EffectLocations implements Serializable {

    private static final long serialVersionUID = -8655852529691765532L;

    private final Effect effect;
    private final Set<Point2d> positions;

    private EffectLocations(Raw.Effect sc2ApiEffect) {
        effect = tryGet(
                Raw.Effect::getEffectId, Raw.Effect::hasEffectId
        ).apply(sc2ApiEffect).map(Effects::from).orElseThrow(required("effect"));

        positions = sc2ApiEffect.getPosList().stream().map(Point2d::from).collect(toSet());
    }

    public static EffectLocations from(Raw.Effect sc2ApiEffect) {
        require("sc2api effect", sc2ApiEffect);
        return new EffectLocations(sc2ApiEffect);
    }

    public Effect getEffect() {
        return effect;
    }

    public Set<Point2d> getPositions() {
        return new HashSet<>(positions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EffectLocations that = (EffectLocations) o;

        return effect == that.effect && positions.equals(that.positions);
    }

    @Override
    public int hashCode() {
        int result = effect.hashCode();
        result = 31 * result + positions.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
