package com.github.ocraft.s2client.protocol.observation.raw;

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
import com.github.ocraft.s2client.protocol.unit.Tag;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PowerSourceTest {

    @Test
    void throwsExceptionWhenSc2ApiPowerSourceIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PowerSource.from(nothing()))
                .withMessage("sc2api power source is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiPowerSource() {
        assertThatAllFieldsAreConverted(PowerSource.from(sc2ApiPowerSource()));
    }

    private void assertThatAllFieldsAreConverted(PowerSource powerSource) {
        assertThat(powerSource.getPosition()).as("power source: position").isNotNull();
        assertThat(powerSource.getRadius()).as("power source: radius").isEqualTo(POWER_SOURCE_RADIUS);
        assertThat(powerSource.getTag()).as("power source: tags").isEqualTo(Tag.from(UNIT_TAG));
    }


    @Test
    void throwsExceptionWhenPositionIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PowerSource.from(without(
                        () -> sc2ApiPowerSource().toBuilder(),
                        Raw.PowerSource.Builder::clearPos).build()))
                .withMessage("position is required");
    }

    @Test
    void throwsExceptionWhenRadiusIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PowerSource.from(without(
                        () -> sc2ApiPowerSource().toBuilder(),
                        Raw.PowerSource.Builder::clearRadius).build()))
                .withMessage("radius is required");
    }

    @Test
    void throwsExceptionWhenTagIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PowerSource.from(without(
                        () -> sc2ApiPowerSource().toBuilder(),
                        Raw.PowerSource.Builder::clearTag).build()))
                .withMessage("tag is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(PowerSource.class).withNonnullFields("position", "tag").verify();
    }
}
