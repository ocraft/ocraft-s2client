package com.github.ocraft.s2client.protocol.unit;

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
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Units;
import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class UnitTest {

    @Test
    void throwsExceptionWhenSc2ApiUnitIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Unit.from(nothing()))
                .withMessage("sc2api unit is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiUnit() {
        assertThatAllFieldsAreConverted(Unit.from(sc2ApiUnit()));
    }

    private void assertThatAllFieldsAreConverted(Unit unit) {
        assertThat(unit.getDisplayType()).as("unit: display type").isEqualTo(DisplayType.VISIBLE);
        assertThat(unit.getAlliance()).as("unit: alliance").isEqualTo(Alliance.SELF);
        assertThat(unit.getTag()).as("unit: tag").isEqualTo(Tag.from(UNIT_TAG));
        assertThat(unit.getType()).as("unit: type").isEqualTo(Units.PROTOSS_NEXUS);
        assertThat(unit.getOwner()).as("unit: owner").isEqualTo(PLAYER_ID);
        assertThat(unit.getPosition()).as("unit: position").isNotNull();
        assertThat(unit.getFacing()).as("unit: facing").isEqualTo(UNIT_FACING);
        assertThat(unit.getRadius()).as("unit: radius").isEqualTo(UNIT_RADIUS);
        assertThat(unit.getBuildProgress()).as("unit: build progress").isEqualTo(UNIT_BUILD_PROGRESS);
        assertThat(unit.getCloakState()).as("unit: cloak state").hasValue(CloakState.NOT_CLOAKED);
        assertThat(unit.getDetectRange()).as("unit: detect range").hasValue(UNIT_DETECT_RANGE);
        assertThat(unit.getRadarRange()).as("unit: radar range").hasValue(UNIT_RADAR_RANGE);
        assertThat(unit.getSelected()).as("unit: selected").hasValue(UNIT_SELECTED);
        assertThat(unit.isOnScreen()).as("unit: on screen").isTrue();
        assertThat(unit.isBlip()).as("unit: blip").isTrue();
        assertThat(unit.getPowered()).as("unit: powered").hasValue(UNIT_POWERED);
        assertThat(unit.getHealth()).as("unit: health").hasValue(UNIT_HEALTH);
        assertThat(unit.getHealthMax()).as("unit: health max").hasValue(UNIT_HEALTH_MAX);
        assertThat(unit.getShield()).as("unit: shield").hasValue(UNIT_SHIELD);
        assertThat(unit.getShieldMax()).as("unit: shield max").hasValue(UNIT_SHIELD_MAX);
        assertThat(unit.getEnergy()).as("unit: energy").hasValue(UNIT_ENERGY);
        assertThat(unit.getEnergyMax()).as("unit: energy max").hasValue(UNIT_ENERGY_MAX);
        assertThat(unit.getMineralContents()).as("unit: mineral contents").hasValue(UNIT_MINERAL_CONTENTS);
        assertThat(unit.getVespeneContents()).as("unit: vespene contents").hasValue(UNIT_VESPENE_CONTENTS);
        assertThat(unit.getFlying()).as("unit: flying").hasValue(UNIT_FLYING);
        assertThat(unit.getBurrowed()).as("unit: burrowed").hasValue(UNIT_BURROWED);
        assertThat(unit.getOrders()).as("unit: orders").isNotEmpty();
        assertThat(unit.getAddOnTag()).as("unit: addon tag").hasValue(Tag.from(UNIT_ADDON_TAG));
        assertThat(unit.getPassengers()).as("unit: passengers").isNotEmpty();
        assertThat(unit.getCargoSpaceTaken()).as("unit: cargo space taken").hasValue(UNIT_CARGO_SPACE_TAKEN);
        assertThat(unit.getCargoSpaceMax()).as("unit: cargo space max").hasValue(UNIT_CARGO_SPACE_MAX);
        assertThat(unit.getBuffs()).as("unit: buffs").containsOnlyElementsOf(UNIT_BUFFS);
        assertThat(unit.getAssignedHarvesters()).as("unit: assigned harvesters").hasValue(UNIT_ASSIGNED_HARVESTERS);
        assertThat(unit.getIdealHarvesters()).as("unit: ideal harvesters").hasValue(UNIT_IDEAL_HARVESTERS);
        assertThat(unit.getWeaponCooldown()).as("unit: weapon cooldown").hasValue(UNIT_WEAPON_COOLDOWN);
        assertThat(unit.getEngagedTargetTag()).as("unit: engaged target tag")
                .hasValue(Tag.from(UNIT_ENGAGED_TARGET_TAG));
    }

    @Test
    void throwsExceptionWhenDisplayTypeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Unit.from(
                        without(() -> sc2ApiUnit().toBuilder(), Raw.Unit.Builder::clearDisplayType).build()))
                .withMessage("display type is required");
    }

    @Test
    void throwsExceptionWhenAllianceIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Unit.from(
                        without(() -> sc2ApiUnit().toBuilder(), Raw.Unit.Builder::clearAlliance).build()))
                .withMessage("alliance is required");
    }

    @Test
    void throwsExceptionWhenTagIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Unit.from(
                        without(() -> sc2ApiUnit().toBuilder(), Raw.Unit.Builder::clearTag).build()))
                .withMessage("tag is required");
    }

    @Test
    void throwsExceptionWhenTypeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Unit.from(
                        without(() -> sc2ApiUnit().toBuilder(), Raw.Unit.Builder::clearUnitType).build()))
                .withMessage("unit type is required");
    }

    @Test
    void throwsExceptionWhenOwnerIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Unit.from(
                        without(() -> sc2ApiUnit().toBuilder(), Raw.Unit.Builder::clearOwner).build()))
                .withMessage("owner is required");
    }

    @Test
    void throwsExceptionWhenPositionIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Unit.from(
                        without(() -> sc2ApiUnit().toBuilder(), Raw.Unit.Builder::clearPos).build()))
                .withMessage("position is required");
    }

    @Test
    void throwsExceptionWhenFacingIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Unit.from(
                        without(() -> sc2ApiUnit().toBuilder(), Raw.Unit.Builder::clearFacing).build()))
                .withMessage("facing is required");
    }

    @Test
    void throwsExceptionWhenRadiusIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Unit.from(
                        without(() -> sc2ApiUnit().toBuilder(), Raw.Unit.Builder::clearRadius).build()))
                .withMessage("radius is required");
    }

    @Test
    void throwsExceptionWhenBuildProgressIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Unit.from(
                        without(() -> sc2ApiUnit().toBuilder(), Raw.Unit.Builder::clearBuildProgress).build()))
                .withMessage("build progress is required");
    }

    @Test
    void hasDefaultValueForOnScreenFieldIfNotProvided() {
        Unit unit = Unit.from(without(() -> sc2ApiUnit().toBuilder(), Raw.Unit.Builder::clearIsOnScreen).build());
        assertThat(unit.isOnScreen()).as("unit: default on screen").isFalse();
    }

    @Test
    void hasDefaultValueForBlipFieldIfNotProvided() {
        Unit unit = Unit.from(without(() -> sc2ApiUnit().toBuilder(), Raw.Unit.Builder::clearIsBlip).build());
        assertThat(unit.isBlip()).as("unit: default blip").isFalse();
    }

    @Test
    void hasEmptyListOfOrdersWhenNotProvided() {
        assertThat(Unit.from(
                without(() -> sc2ApiUnit().toBuilder(), Raw.Unit.Builder::clearOrders).build()
        ).getOrders()).as("unit: empty orders list").isEmpty();
    }

    @Test
    void hasEmptyListOfPassengersWhenNotProvided() {
        assertThat(Unit.from(
                without(() -> sc2ApiUnit().toBuilder(), Raw.Unit.Builder::clearPassengers).build()
        ).getPassengers()).as("unit: empty passengers list").isEmpty();
    }

    @Test
    void hasEmptySetOfBuffsWhenNotProvided() {
        assertThat(Unit.from(
                without(() -> sc2ApiUnit().toBuilder(), Raw.Unit.Builder::clearBuffIds).build()
        ).getBuffs()).as("unit: empty buff set").isEmpty();
    }

    @Test
    void createsCopyWithGeneralizedAbility() {
        Unit specific = Unit.from(sc2ApiUnit());
        Abilities generalizedAbility = Abilities.EFFECT_CHRONO_BOOST;
        Unit generalized = specific.generalizeAbility(ability -> generalizedAbility);

        DiffNode diffNode = ObjectDifferBuilder.buildDefault().compare(specific, generalized);

        assertThat(diffNode.hasChanges()).as("generalized unit changes").isTrue();
        assertThat(diffNode.childCount()).as("generalized unit changes count").isEqualTo(1);
        assertThat(generalized.getOrders().get(0).getAbility()).as("generalized ability").isSameAs(generalizedAbility);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(Unit.class)
                .withRedefinedSuperclass()
                .withNonnullFields("displayType", "alliance", "tag", "type", "position", "orders", "passengers",
                        "buffs")
                .verify();
    }
}
