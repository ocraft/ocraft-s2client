package com.github.ocraft.s2client.protocol.observation.spatial;

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

import SC2APIProtocol.Spatial;
import com.google.protobuf.ByteString;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiObservationRender;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ObservationRenderTest {
    @Test
    void throwsExceptionWhenSc2ApiObservationRenderIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ObservationRender.from(nothing()))
                .withMessage("sc2api observation render is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiObservationRender() {
        assertThatAllFieldsAreConverted(ObservationRender.from(sc2ApiObservationRender()));
    }

    private void assertThatAllFieldsAreConverted(ObservationRender observationRender) {
        assertThat(observationRender.getMap()).as("observation render: map").isNotNull();
        assertThat(observationRender.getMinimap()).as("observation render: minimap").isNotNull();
    }

    @Test
    void throwsExceptionWhenMapIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ObservationRender.from(without(
                        () -> sc2ApiObservationRender().toBuilder(),
                        Spatial.ObservationRender.Builder::clearMap).build()))
                .withMessage("map is required");
    }

    @Test
    void throwsExceptionWhenMinimapIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ObservationRender.from(without(
                        () -> sc2ApiObservationRender().toBuilder(),
                        Spatial.ObservationRender.Builder::clearMinimap).build()))
                .withMessage("minimap is required");
    }

    @Test
    void fulfillsEqualsContract() throws UnsupportedEncodingException {
        EqualsVerifier.forClass(ObservationRender.class)
                .withNonnullFields("map", "minimap")
                .withPrefabValues(
                        ByteString.class,
                        ByteString.copyFrom("test", "UTF-8"),
                        ByteString.copyFrom("test2", "UTF-8"))
                .verify();
    }
}
