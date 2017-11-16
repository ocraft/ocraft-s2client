package com.github.ocraft.s2client.protocol.query;

import SC2APIProtocol.Query;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.observation.AvailableAbility;
import com.github.ocraft.s2client.protocol.unit.Tag;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.toSet;

public final class AvailableAbilities implements Serializable {

    private static final long serialVersionUID = 366573550138386426L;

    private final Set<AvailableAbility> abilities;
    private final Tag unitTag;
    private final UnitType unitType;

    private AvailableAbilities(Query.ResponseQueryAvailableAbilities sc2ApiResponseQueryAvailableAbilities) {
        abilities = sc2ApiResponseQueryAvailableAbilities.getAbilitiesList().stream()
                .map(AvailableAbility::from).collect(toSet());

        unitTag = tryGet(
                Query.ResponseQueryAvailableAbilities::getUnitTag, Query.ResponseQueryAvailableAbilities::hasUnitTag
        ).apply(sc2ApiResponseQueryAvailableAbilities).map(Tag::from).orElseThrow(required("unit tag"));

        unitType = tryGet(
                Query.ResponseQueryAvailableAbilities::getUnitTypeId,
                Query.ResponseQueryAvailableAbilities::hasUnitTypeId
        ).apply(sc2ApiResponseQueryAvailableAbilities).map(Units::from).orElseThrow(required("unit type"));
    }

    public static AvailableAbilities from(Query.ResponseQueryAvailableAbilities sc2ApiResponseQueryAvailableAbilities) {
        require("sc2api response query available abilities", sc2ApiResponseQueryAvailableAbilities);
        return new AvailableAbilities(sc2ApiResponseQueryAvailableAbilities);
    }

    public Set<AvailableAbility> getAbilities() {
        return new HashSet<>(abilities);
    }

    public Tag getUnitTag() {
        return unitTag;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AvailableAbilities that = (AvailableAbilities) o;

        return abilities.equals(that.abilities) && unitTag.equals(that.unitTag) && unitType == that.unitType;
    }

    @Override
    public int hashCode() {
        int result = abilities.hashCode();
        result = 31 * result + unitTag.hashCode();
        result = 31 * result + unitType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
