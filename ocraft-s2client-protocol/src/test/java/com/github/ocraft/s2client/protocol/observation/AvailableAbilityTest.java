package com.github.ocraft.s2client.protocol.observation;

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

import SC2APIProtocol.Common;
import com.github.ocraft.s2client.protocol.data.Abilities;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiAvailableAbility;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class AvailableAbilityTest {

    @Test
    void throwsExceptionWhenSc2ApiAvailableAbilityIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AvailableAbility.from(nothing()))
                .withMessage("sc2api available ability is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiAvailableAbility() {
        assertThatAllFieldsAreConverted(AvailableAbility.from(sc2ApiAvailableAbility()));
    }

    private void assertThatAllFieldsAreConverted(AvailableAbility availableAbility) {
        assertThat(availableAbility.getAbility()).as("available ability: ability").isEqualTo(Abilities.EFFECT_PSI_STORM);
        assertThat(availableAbility.isRequiresPoint()).as("available ability: required point").isTrue();
    }

    @Test
    void throwsExceptionWhenAbilityIdIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AvailableAbility.from(without(
                        () -> sc2ApiAvailableAbility().toBuilder(),
                        Common.AvailableAbility.Builder::clearAbilityId).build()))
                .withMessage("ability is required");
    }

    @Test
    void hasDefaultValueForRequiresPointFieldIfNotProvided() {
        AvailableAbility availableAbility = AvailableAbility.from(without(
                () -> sc2ApiAvailableAbility().toBuilder(),
                Common.AvailableAbility.Builder::clearRequiresPoint).build());
        assertThat(availableAbility.isRequiresPoint()).as("available ability: requires point").isFalse();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(AvailableAbility.class).withNonnullFields("ability").verify();
    }
}
