package com.github.ocraft.s2client.protocol.data;

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

import SC2APIProtocol.Data;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class AbilityDataTest {

    @Test
    void throwsExceptionWhenSc2ApiAbilityDataIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AbilityData.from(nothing()))
                .withMessage("sc2api ability data is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiUnit() {
        assertThatAllFieldsAreConverted(AbilityData.from(sc2ApiAbilityData()));
    }

    private void assertThatAllFieldsAreConverted(AbilityData ability) {
        assertThat(ability.getAbility()).as("ability: id").isNotNull();
        assertThat(ability.getLinkName()).as("ability: link name").isEqualTo(ABILITY_LINK_NAME);
        assertThat(ability.getLinkIndex()).as("ability: link index").isEqualTo(ABILITY_LINK_INDEX);
        assertThat(ability.getButtonName()).as("ability: button name").hasValue(ABILITY_BUTTON_NAME);
        assertThat(ability.getFriendlyName()).as("ability: friendly name").hasValue(ABILITY_FRIENDLY_NAME);
        assertThat(ability.getHotkey()).as("ability: hotkey").hasValue(ABILITY_HOTKEY);
        assertThat(ability.getRemapsToAbility()).as("ability: remaps to ability").hasValue(ABILITY_REMAPS_TO_ID);
        assertThat(ability.isAvailable()).as("ability: available").isEqualTo(ABILITY_AVAILABLE);
        assertThat(ability.getTarget()).as("ability: target").isNotEmpty();
        assertThat(ability.isAllowMinimap()).as("ability: available").isEqualTo(ABILITY_ALLOW_MINIMAP);
        assertThat(ability.isAllowAutocast()).as("ability: autocast").isEqualTo(ABILITY_ALLOW_AUTOCAST);
        assertThat(ability.isBuilding()).as("ability: building").isEqualTo(ABILITY_IS_BUILDING);
        assertThat(ability.getFootprintRadius()).as("ability: footprint radius").hasValue(ABILITY_FOOTPRINT_RADIUS);
        assertThat(ability.isInstantPlacement()).as("ability: instant placement").isEqualTo(ABILITY_INSTANT_PLACEMENT);
        assertThat(ability.getCastRange()).as("ability: cast range").hasValue(ABILITY_CAST_RANGE);
    }

    @Test
    void throwsExceptionWhenAbilityIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AbilityData.from(without(
                        () -> sc2ApiAbilityData().toBuilder(),
                        Data.AbilityData.Builder::clearAbilityId).build()))
                .withMessage("ability is required");
    }

    @Test
    void throwsExceptionWhenLinkNameIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AbilityData.from(without(
                        () -> sc2ApiAbilityData().toBuilder(),
                        Data.AbilityData.Builder::clearLinkName).build()))
                .withMessage("link name is required");
    }

    @Test
    void throwsExceptionWhenLinkIndexIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AbilityData.from(without(
                        () -> sc2ApiAbilityData().toBuilder(),
                        Data.AbilityData.Builder::clearLinkIndex).build()))
                .withMessage("link index is required");
    }

    @Test
    void hasDefaultValueForAvailableIfNotProvided() {
        AbilityData ability = AbilityData.from(
                without(() -> sc2ApiAbilityData().toBuilder(), Data.AbilityData.Builder::clearAvailable).build());
        assertThat(ability.isAvailable()).as("ability: default available").isFalse();
    }

    @Test
    void hasDefaultValueForAllowMinimapIfNotProvided() {
        AbilityData ability = AbilityData.from(
                without(() -> sc2ApiAbilityData().toBuilder(), Data.AbilityData.Builder::clearAllowMinimap).build());
        assertThat(ability.isAllowMinimap()).as("ability: default allow minimap").isFalse();
    }

    @Test
    void hasDefaultValueForAllowAutocastIfNotProvided() {
        AbilityData ability = AbilityData.from(
                without(() -> sc2ApiAbilityData().toBuilder(), Data.AbilityData.Builder::clearAllowAutocast).build());
        assertThat(ability.isAllowAutocast()).as("ability: default allow autocast").isFalse();
    }

    @Test
    void hasDefaultValueForBuildingIfNotProvided() {
        AbilityData ability = AbilityData.from(
                without(() -> sc2ApiAbilityData().toBuilder(), Data.AbilityData.Builder::clearIsBuilding).build());
        assertThat(ability.isBuilding()).as("ability: default building").isFalse();
    }

    @Test
    void hasDefaultValueForInstantPlacementIfNotProvided() {
        AbilityData ability = AbilityData.from(without(
                () -> sc2ApiAbilityData().toBuilder(),
                Data.AbilityData.Builder::clearIsInstantPlacement).build());
        assertThat(ability.isInstantPlacement()).as("ability: default instant placement").isFalse();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(AbilityData.class).withNonnullFields("ability", "linkName", "linkIndex").verify();
    }

}
