package com.github.ocraft.s2client.protocol.query;

import SC2APIProtocol.Query;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class Pathing implements Serializable {

    private static final long serialVersionUID = 2302282027991564714L;

    private final Float distance;

    private Pathing(Query.ResponseQueryPathing sc2ApiResponseQueryPathing) {
        distance = tryGet(Query.ResponseQueryPathing::getDistance, Query.ResponseQueryPathing::hasDistance)
                .apply(sc2ApiResponseQueryPathing).orElse(nothing());
    }

    public static Pathing from(Query.ResponseQueryPathing sc2ApiResponseQueryPathing) {
        require("sc2api response query pathing", sc2ApiResponseQueryPathing);
        return new Pathing(sc2ApiResponseQueryPathing);
    }

    public Optional<Float> getDistance() {
        return Optional.ofNullable(distance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pathing pathing = (Pathing) o;

        return distance != null ? distance.equals(pathing.distance) : pathing.distance == null;
    }

    @Override
    public int hashCode() {
        return distance != null ? distance.hashCode() : 0;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
