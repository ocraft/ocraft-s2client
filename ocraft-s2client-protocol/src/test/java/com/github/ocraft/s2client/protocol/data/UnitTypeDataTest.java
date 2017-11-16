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
package com.github.ocraft.s2client.protocol.data;

import SC2APIProtocol.Data;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class UnitTypeDataTest {
    @Test
    void throwsExceptionWhenSc2ApiUnitTypeDataIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> UnitTypeData.from(nothing()))
                .withMessage("sc2api unit type data is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiUnit() {
        assertThatAllFieldsAreConverted(UnitTypeData.from(sc2ApiUnitTypeData()));
    }

    private void assertThatAllFieldsAreConverted(UnitTypeData unit) {
        assertThat(unit.getUnitType()).as("unit: type").isNotNull();
        assertThat(unit.getName()).as("unit: name").isEqualTo(UNIT_NAME);
        assertThat(unit.isAvailable()).as("unit: available").isEqualTo(UNIT_AVAILABLE);
        assertThat(unit.getCargoSize()).as("unit: cargo space").hasValue(UNIT_CARGO_SPACE_TAKEN);
        assertThat(unit.getMineralCost()).as("unit: mineral cost").hasValue(UNIT_MINERAL_COST);
        assertThat(unit.getVespeneCost()).as("unit: vespene cost").hasValue(UNIT_VESPENE_COST);
        assertThat(unit.getFoodRequired()).as("unit: food required").hasValue(UNIT_FOOD_REQUIRED);
        assertThat(unit.getFoodProvided()).as("unit: food provided").hasValue(UNIT_FOOD_PROVIDED);
        assertThat(unit.getAbility()).as("unit: ability").isNotEmpty();
        assertThat(unit.getRace()).as("unit: race").isNotEmpty();
        assertThat(unit.getBuildTime()).as("unit: build time").hasValue(UNIT_BUILD_TIME);
        assertThat(unit.isHasVespene()).as("unit: has vespene").isEqualTo(UNIT_HAS_VESPENE);
        assertThat(unit.isHasMinerals()).as("unit: has minerals").isEqualTo(UNIT_HAS_MINERALS);
        assertThat(unit.getSightRange()).as("unit: sight range").hasValue(UNIT_SIGHT_RANGE);
        assertThat(unit.getTechAliases()).as("unit: tech aliases").isNotEmpty();
        assertThat(unit.getUnitAlias()).as("unit: unit alias").isNotEmpty();
        assertThat(unit.getTechRequirement()).as("unit: tech requirement").isNotEmpty();
        assertThat(unit.isRequireAttached()).as("unit: require attached").isEqualTo(UNIT_REQUIRE_ATTACHED);
        assertThat(unit.getAttributes()).as("unit: attributes").isNotEmpty();
        assertThat(unit.getMovementSpeed()).as("unit: movement speed").hasValue(UNIT_SPEED);
        assertThat(unit.getArmor()).as("unit: armor").hasValue(UNIT_ARMOR);
        assertThat(unit.getWeapons()).as("unit: weapons").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenUnitTypeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> UnitTypeData.from(without(
                        () -> sc2ApiUnitTypeData().toBuilder(),
                        Data.UnitTypeData.Builder::clearUnitId).build()))
                .withMessage("unit type is required");
    }

    @Test
    void throwsExceptionWhenNameIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> UnitTypeData.from(without(
                        () -> sc2ApiUnitTypeData().toBuilder(),
                        Data.UnitTypeData.Builder::clearName).build()))
                .withMessage("name is required");
    }

    @Test
    void hasDefaultValueForAvailableIfNotProvided() {
        UnitTypeData unit = UnitTypeData.from(
                without(() -> sc2ApiUnitTypeData().toBuilder(), Data.UnitTypeData.Builder::clearAvailable).build());
        assertThat(unit.isAvailable()).as("unit: default available").isFalse();
    }

    @Test
    void hasDefaultValueForHasVespeneIfNotProvided() {
        UnitTypeData unit = UnitTypeData.from(
                without(() -> sc2ApiUnitTypeData().toBuilder(), Data.UnitTypeData.Builder::clearHasVespene).build());
        assertThat(unit.isHasVespene()).as("unit: default has vespene").isFalse();
    }

    @Test
    void hasDefaultValueForHasMineralsIfNotProvided() {
        UnitTypeData unit = UnitTypeData.from(
                without(() -> sc2ApiUnitTypeData().toBuilder(), Data.UnitTypeData.Builder::clearHasMinerals).build());
        assertThat(unit.isHasMinerals()).as("unit: default has minerals").isFalse();
    }

    @Test
    void hasDefaultValueForRequireAttachedIfNotProvided() {
        UnitTypeData unit = UnitTypeData.from(without(
                () -> sc2ApiUnitTypeData().toBuilder(),
                Data.UnitTypeData.Builder::clearRequireAttached).build());
        assertThat(unit.isRequireAttached()).as("unit: default require attached").isFalse();
    }

    @Test
    void hasEmptySetWhenTechAliasesAreNotProvided() {
        UnitTypeData unit = UnitTypeData.from(
                without(() -> sc2ApiUnitTypeData().toBuilder(), Data.UnitTypeData.Builder::clearTechAlias).build());
        assertThat(unit.getTechAliases()).as("unit: default tech aliases").isEmpty();
    }

    @Test
    void hasEmptySetWhenAttributesAreNotProvided() {
        UnitTypeData unit = UnitTypeData.from(
                without(() -> sc2ApiUnitTypeData().toBuilder(), Data.UnitTypeData.Builder::clearAttributes).build());
        assertThat(unit.getAttributes()).as("unit: default attributes").isEmpty();
    }

    @Test
    void hasEmptySetWhenWeaponsAreNotProvided() {
        UnitTypeData unit = UnitTypeData.from(
                without(() -> sc2ApiUnitTypeData().toBuilder(), Data.UnitTypeData.Builder::clearWeapons).build());
        assertThat(unit.getWeapons()).as("unit: default weapons").isEmpty();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(UnitTypeData.class)
                .withNonnullFields("unitType", "name", "techAliases", "attributes", "weapons")
                .verify();
    }

}