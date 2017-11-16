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
package com.github.ocraft.s2client.protocol.observation.raw;

import SC2APIProtocol.Raw;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiObservationRaw;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ObservationRawTest {

    @Test
    void throwsExceptionWhenSc2ApiObservationRawIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ObservationRaw.from(nothing()))
                .withMessage("sc2api observation raw is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiObservationRaw() {
        assertThatAllFieldsAreConverted(ObservationRaw.from(sc2ApiObservationRaw()));
    }

    private void assertThatAllFieldsAreConverted(ObservationRaw observation) {
        assertThat(observation.getPlayer()).as("observation raw: player").isNotNull();
        assertThat(observation.getUnits()).as("observation raw: units").isNotEmpty();
        assertThat(observation.getMapState()).as("observation raw: map state").isNotNull();
        assertThat(observation.getEvent()).as("observation raw: event").isNotEmpty();
        assertThat(observation.getEffects()).as("observation raw: effects").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenPlayerIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ObservationRaw.from(without(
                        () -> sc2ApiObservationRaw().toBuilder(),
                        Raw.ObservationRaw.Builder::clearPlayer).build()))
                .withMessage("player is required");
    }

    @Test
    void hasEmptySetOfUnitsWhenNotProvided() {
        assertThat(ObservationRaw.from(
                without(() -> sc2ApiObservationRaw().toBuilder(), Raw.ObservationRaw.Builder::clearUnits).build()
        ).getUnits()).as("observation raw: empty unit set").isEmpty();
    }

    @Test
    void throwsExceptionWhenMapStateIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ObservationRaw.from(without(
                        () -> sc2ApiObservationRaw().toBuilder(),
                        Raw.ObservationRaw.Builder::clearMapState).build()))
                .withMessage("map state is required");
    }

    @Test
    void hasEmptySetOfEffectsWhenNotProvided() {
        assertThat(ObservationRaw.from(
                without(() -> sc2ApiObservationRaw().toBuilder(), Raw.ObservationRaw.Builder::clearEffects).build()
        ).getEffects()).as("observation raw: empty effect set").isEmpty();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(ObservationRaw.class)
                .withNonnullFields("player", "units", "mapState", "effects")
                .verify();
    }
}