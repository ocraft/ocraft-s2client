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
package com.github.ocraft.s2client.protocol.query;

import SC2APIProtocol.Query;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiAvailableAbilities;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class AvailableAbilitiesTest {
    @Test
    void throwsExceptionWhenSc2ApiAvailableAbilitiesIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AvailableAbilities.from(nothing()))
                .withMessage("sc2api response query available abilities is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiAvailableAbilities() {
        assertThatAllFieldsAreConverted(AvailableAbilities.from(sc2ApiAvailableAbilities()));
    }

    private void assertThatAllFieldsAreConverted(AvailableAbilities availableAbilities) {
        assertThat(availableAbilities.getAbilities()).as("available abilities: abilities").isNotEmpty();
        assertThat(availableAbilities.getUnitType()).as("available abilities: unit type").isNotNull();
        assertThat(availableAbilities.getUnitTag()).as("available abilities: unit tag").isNotNull();
    }

    @Test
    void throwsExceptionWhenUnitTagIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AvailableAbilities.from(without(
                        () -> sc2ApiAvailableAbilities().toBuilder(),
                        Query.ResponseQueryAvailableAbilities.Builder::clearUnitTag).build()))
                .withMessage("unit tag is required");
    }

    @Test
    void throwsExceptionWhenUnitTypeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AvailableAbilities.from(without(
                        () -> sc2ApiAvailableAbilities().toBuilder(),
                        Query.ResponseQueryAvailableAbilities.Builder::clearUnitTypeId).build()))
                .withMessage("unit type is required");
    }

    @Test
    void hasEmptySetOfAbilitiesIfNotProvided() {
        assertThat(AvailableAbilities.from(without(
                () -> sc2ApiAvailableAbilities().toBuilder(),
                Query.ResponseQueryAvailableAbilities.Builder::clearAbilities).build()).getAbilities())
                .as("available abilities: default abilities set").isEmpty();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(AvailableAbilities.class)
                .withNonnullFields("abilities", "unitTag", "unitType").verify();
    }
}