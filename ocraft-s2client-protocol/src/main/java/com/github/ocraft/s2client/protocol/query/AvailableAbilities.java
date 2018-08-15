package com.github.ocraft.s2client.protocol.query;

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

import SC2APIProtocol.Query;
import com.github.ocraft.s2client.protocol.GeneralizableAbility;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.observation.AvailableAbility;
import com.github.ocraft.s2client.protocol.unit.Tag;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.UnaryOperator;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.toSet;

public final class AvailableAbilities implements Serializable, GeneralizableAbility<AvailableAbilities> {

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

    private AvailableAbilities(Set<AvailableAbility> abilities, Tag unitTag, UnitType unitType) {
        this.abilities = abilities;
        this.unitTag = unitTag;
        this.unitType = unitType;
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
    public AvailableAbilities generalizeAbility(UnaryOperator<Ability> generalize) {
        return new AvailableAbilities(
                abilities.stream()
                        .map(availableAbility -> availableAbility.generalizeAbility(generalize))
                        .collect(toSet()),
                unitTag,
                unitType);
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
