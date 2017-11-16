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
package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import com.github.ocraft.s2client.protocol.data.Units;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.debug.DebugCreateUnit.createUnit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DebugCreateUnitTest {

    @Test
    void serializesToSc2ApiDebugCreateUnit() {
        assertThatAllFieldsAreSerialized(debugCreateUnit().build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Debug.DebugCreateUnit sc2ApiCreateUnit) {
        assertThat(sc2ApiCreateUnit.getUnitType()).as("sc2api debug create unit: unit type").isEqualTo(TYPE_ID_ARCHON);
        assertThat(sc2ApiCreateUnit.getOwner()).as("sc2api debug create unit: owner").isEqualTo(PLAYER_ID);
        assertThat(sc2ApiCreateUnit.getQuantity()).as("sc2api debug create unit: quantity").isEqualTo(QUANTITY);
        assertThat(sc2ApiCreateUnit.hasPos()).as("sc2api debug create unit: has position").isTrue();
    }

    @Test
    void throwsExceptionWhenTypeIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(createUnit()).forPlayer(PLAYER_ID).on(POS).build())
                .withMessage("type is required");
    }

    private DebugCreateUnit.Builder fullAccessTo(Object obj) {
        return (DebugCreateUnit.Builder) obj;
    }

    @Test
    void throwsExceptionWhenOwnerIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(createUnit().ofType(Units.PROTOSS_STALKER)).on(POS).build())
                .withMessage("owner is required");
    }

    @Test
    void throwsExceptionWhenPositionIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(createUnit().ofType(Units.PROTOSS_STALKER).forPlayer(PLAYER_ID)).build())
                .withMessage("position is required");
    }

    @Test
    void serializesDefaultValueForQuantityIfNotProvided() {
        assertThat(defaultCreateUnit().getQuantity()).as("sc2api debug create unit: default quantity").isEqualTo(1);
    }

    private Debug.DebugCreateUnit defaultCreateUnit() {
        return createUnit().ofType(Units.PROTOSS_STALKER).forPlayer(PLAYER_ID).on(POS).build().toSc2Api();
    }

    @Test
    void throwsExceptionWhenQuantityIsLowerThanOne() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> createUnit()
                        .ofType(Units.PROTOSS_STALKER).forPlayer(PLAYER_ID).on(POS).withQuantity(0).build())
                .withMessage("quantity has value 0 and is lower than 1");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(DebugCreateUnit.class).withNonnullFields("type", "position").verify();
    }

}