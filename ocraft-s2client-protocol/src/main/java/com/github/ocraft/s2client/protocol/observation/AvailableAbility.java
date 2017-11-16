package com.github.ocraft.s2client.protocol.observation;

import SC2APIProtocol.Common;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Ability;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class AvailableAbility implements Serializable {

    private static final long serialVersionUID = 4523885840022128043L;

    private static final boolean DEFAULT_REQUIRES_POINT = false;

    private final Ability ability;
    private final boolean requiresPoint;

    private AvailableAbility(Common.AvailableAbility sc2ApiAvailableAbility) {
        ability = tryGet(
                Common.AvailableAbility::getAbilityId, Common.AvailableAbility::hasAbilityId
        ).apply(sc2ApiAvailableAbility).map(Abilities::from).orElseThrow(required("ability"));

        requiresPoint = tryGet(
                Common.AvailableAbility::getRequiresPoint, Common.AvailableAbility::hasRequiresPoint
        ).apply(sc2ApiAvailableAbility).orElse(DEFAULT_REQUIRES_POINT);
    }

    public static AvailableAbility from(Common.AvailableAbility sc2ApiAvailableAbility) {
        require("sc2api available ability", sc2ApiAvailableAbility);
        return new AvailableAbility(sc2ApiAvailableAbility);
    }

    public Ability getAbility() {
        return ability;
    }

    public boolean isRequiresPoint() {
        return requiresPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AvailableAbility that = (AvailableAbility) o;

        return requiresPoint == that.requiresPoint && ability == that.ability;
    }

    @Override
    public int hashCode() {
        int result = ability.hashCode();
        result = 31 * result + (requiresPoint ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
