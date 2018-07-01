package com.github.ocraft.s2client.protocol.game;

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

import SC2APIProtocol.Sc2Api;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Defaults.defaultSpatialSetup;
import static com.github.ocraft.s2client.protocol.Defaults.defaultSpatialSetupForRender;
import static com.github.ocraft.s2client.protocol.Fixtures.aSpatialSetup;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiInterfaceOptions;
import static com.github.ocraft.s2client.protocol.game.InterfaceOptions.interfaces;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class InterfaceOptionsTest {

    @Test
    void serializesToSc2ApiInterfaceOptions() {
        assertThatAllFieldsAreSerialized(
                interfaces()
                        .raw().score()
                        .featureLayer(defaultSpatialSetup())
                        .render(defaultSpatialSetupForRender())
                        .build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Sc2Api.InterfaceOptions sc2ApiInterfaceOptions) {
        assertThat(sc2ApiInterfaceOptions.getRaw()).as("raw option is set").isTrue();
        assertThat(sc2ApiInterfaceOptions.getScore()).as("score option is set").isTrue();
        assertThat(sc2ApiInterfaceOptions.hasFeatureLayer()).as("feature layer is set").isTrue();
        assertThat(sc2ApiInterfaceOptions.hasRender()).as("render is set").isTrue();
    }


    @Test
    void serializesToSc2ApiInterfaceOptionsUsingBuilders() {
        assertThatAllFieldsAreSerialized(
                interfaces()
                        .raw().score()
                        .featureLayer(aSpatialSetup())
                        .render(aSpatialSetup())
                        .build().toSc2Api());
    }

    @Test
    void serializesDefaultValueForRawOptionIfNotSet() {
        assertThat(interfaces().score().build().toSc2Api().getRaw()).as("raw option has default value").isFalse();
    }

    @Test
    void serializesDefaultValueForScoreOptionIfNotSet() {
        assertThat(interfaces().raw().build().toSc2Api().getScore()).as("score option has default value").isFalse();
    }

    @Test
    void doesNotSerializeFeatureLayerIfNotSet() {
        assertThat(interfaces().raw().build().toSc2Api().hasFeatureLayer()).as("feature layer is not set").isFalse();
    }

    @Test
    void doesNotSerializeRenderIfNotSet() {
        assertThat(interfaces().raw().build().toSc2Api().hasRender()).as("render is not set").isFalse();
    }

    @Test
    void providesDefaultSpatialSetupForFeatureLayers() {
        assertThat(interfaces().featureLayer().build().getFeatureLayer()).as("default spatial camera setup")
                .hasValue(defaultSpatialSetup());
    }

    @Test
    void providesDefaultSpatialSetupForRender() {
        assertThat(interfaces().render().build().getRender()).as("default spatial camera setup for render")
                .hasValue(defaultSpatialSetupForRender());
    }

    @Test
    void convertsSc2ApiInterfaceOptions() {
        InterfaceOptions interfaceOptions = InterfaceOptions.from(sc2ApiInterfaceOptions());

        assertThat(interfaceOptions.isRaw()).as("interface options: raw").isTrue();
        assertThat(interfaceOptions.isScore()).as("interface options: score").isTrue();
        assertThat(interfaceOptions.getFeatureLayer()).as("interface options: feature layer").isNotEmpty();
        assertThat(interfaceOptions.getRender()).as("interface options: render").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenAnyOptionIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(interfaces()).build())
                .withMessage("one of interface options is required");
    }

    private InterfaceOptions.Builder fullAccessTo(Object obj) {
        return (InterfaceOptions.Builder) obj;
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(InterfaceOptions.class).verify();
    }


}
