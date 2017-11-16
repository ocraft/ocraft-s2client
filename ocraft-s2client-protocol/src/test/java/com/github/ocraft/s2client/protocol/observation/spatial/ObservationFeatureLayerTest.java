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
package com.github.ocraft.s2client.protocol.observation.spatial;

import SC2APIProtocol.Spatial;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiObservationFeatureLayer;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ObservationFeatureLayerTest {
    @Test
    void throwsExceptionWhenSc2ApiObservationFeatureLayerIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ObservationFeatureLayer.from(nothing()))
                .withMessage("sc2api observation feature layer is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiObservationFeatureLayer() {
        assertThatAllFieldsAreConverted(ObservationFeatureLayer.from(sc2ApiObservationFeatureLayer()));
    }

    private void assertThatAllFieldsAreConverted(ObservationFeatureLayer observationFeatureLayer) {
        assertThat(observationFeatureLayer.getRenders()).as("observation feature layer: renders").isNotNull();
        assertThat(observationFeatureLayer.getMinimapRenders()).as("observation feature layer: minimap renders")
                .isNotNull();
    }

    @Test
    void throwsExceptionWhenRendersIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ObservationFeatureLayer.from(without(
                        () -> sc2ApiObservationFeatureLayer().toBuilder(),
                        Spatial.ObservationFeatureLayer.Builder::clearRenders).build()))
                .withMessage("renders is required");
    }

    @Test
    void throwsExceptionWhenMinimapRendersIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ObservationFeatureLayer.from(without(
                        () -> sc2ApiObservationFeatureLayer().toBuilder(),
                        Spatial.ObservationFeatureLayer.Builder::clearMinimapRenders).build()))
                .withMessage("minimap renders is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ObservationFeatureLayer.class).withNonnullFields("renders", "minimapRenders").verify();
    }
}