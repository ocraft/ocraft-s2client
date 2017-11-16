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
package com.github.ocraft.s2client.protocol.observation;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.unit.UnitInfo;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ObservationTest {

    @Test
    void throwsExceptionWhenSc2ApiObservationIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Observation.from(nothing()))
                .withMessage("sc2api observation is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiObservation() {
        assertThatAllFieldsAreConverted(Observation.from(sc2ApiObservation()));
    }

    private void assertThatAllFieldsAreConverted(Observation observation) {
        assertThat(observation.getGameLoop()).as("observation: game loop").isEqualTo(GAME_LOOP);
        assertThat(observation.getPlayerCommon()).as("observation: player common").isNotNull();
        assertThat(observation.getAlerts()).as("observation: alerts").containsOnlyElementsOf(ALERTS);
        assertThat(observation.getAvailableAbilities()).as("observation: available abilities").isNotEmpty();
        assertThat(observation.getScore()).as("observation: score").isNotNull();
        assertThat(observation.getRaw()).as("observation: raw").isNotEmpty();
        assertThat(observation.getFeatureLayer()).as("observation: feature layer").isNotEmpty();
        assertThat(observation.getRender()).as("observation: render").isNotEmpty();
        assertThat(observation.getUi()).as("observation: ui").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenGameLoopIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Observation.from(without(
                        () -> sc2ApiObservation().toBuilder(),
                        Sc2Api.Observation.Builder::clearGameLoop).build()))
                .withMessage("game loop is required");
    }

    @Test
    void throwsExceptionWhenPlayerCommonIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Observation.from(without(
                        () -> sc2ApiObservation().toBuilder(),
                        Sc2Api.Observation.Builder::clearPlayerCommon).build()))
                .withMessage("player common is required");
    }

    @Test
    void hasEmptySetOfAlertsWhenNotProvided() {
        assertThat(Observation.from(
                without(() -> sc2ApiObservation().toBuilder(), Sc2Api.Observation.Builder::clearAlerts).build()
        ).getAlerts()).as("observation: empty alert set").isEmpty();
    }

    @Test
    void hasEmptySetOfAvailableAbilityWhenNotProvided() {
        assertThat(Observation.from(
                without(() -> sc2ApiObservation().toBuilder(), Sc2Api.Observation.Builder::clearAbilities).build()
        ).getAvailableAbilities()).as("observation: empty available ability set").isEmpty();
    }

    @Test
    void throwsExceptionWhenOneOfInterfacesNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Observation.from(without(
                        () -> sc2ApiObservation().toBuilder(),
                        Sc2Api.Observation.Builder::clearRawData,
                        Sc2Api.Observation.Builder::clearFeatureLayerData,
                        Sc2Api.Observation.Builder::clearRenderData).build()))
                .withMessage("one of interfaces is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(Observation.class)
                .withNonnullFields("playerCommon", "alerts", "availableAbilities")
                .withPrefabValues(UnitInfo.class, UnitInfo.from(sc2ApiUnitInfoAddOn()), UnitInfo.from(sc2ApiUnitInfo()))
                .verify();
    }
}