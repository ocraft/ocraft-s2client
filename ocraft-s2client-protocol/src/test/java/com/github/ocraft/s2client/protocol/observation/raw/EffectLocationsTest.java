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
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiEffect;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class EffectLocationsTest {
    @Test
    void throwsExceptionWhenSc2ApiEffectIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> EffectLocations.from(nothing()))
                .withMessage("sc2api effect is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiEffect() {
        assertThatAllFieldsAreConverted(EffectLocations.from(sc2ApiEffect()));
    }

    private void assertThatAllFieldsAreConverted(EffectLocations effect) {
        assertThat(effect.getEffect()).as("effect: effect").isNotNull();
        assertThat(effect.getPositions()).as("effect: positions").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenEffectIdIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> EffectLocations.from(without(
                        () -> sc2ApiEffect().toBuilder(),
                        Raw.Effect.Builder::clearEffectId).build()))
                .withMessage("effect is required");
    }

    @Test
    void hasEmptySetOfPositionsWhenNotProvided() {
        assertThat(EffectLocations.from(
                without(() -> sc2ApiEffect().toBuilder(), Raw.Effect.Builder::clearPos).build()
        ).getPositions()).as("effect: empty positions set").isEmpty();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(EffectLocations.class).withNonnullFields("effect", "positions").verify();
    }
}
