package com.github.ocraft.s2client.protocol.data;

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

import SC2APIProtocol.Data;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DamageBonusTest {
    @Test
    void throwsExceptionWhenSc2ApiDamageBonusIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DamageBonus.from(nothing()))
                .withMessage("sc2api damage bonus is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiDamageBonus() {
        assertThatAllFieldsAreConverted(DamageBonus.from(sc2ApiDamageBonus()));
    }

    private void assertThatAllFieldsAreConverted(DamageBonus damageBonus) {
        assertThat(damageBonus.getAttribute()).as("damage bonus: attribute").isNotNull();
        assertThat(damageBonus.getBonus()).as("damage bonus: bonus").isEqualTo(WEAPON_DAMAGE_BONUS);
    }

    @Test
    void throwsExceptionWhenAttributeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DamageBonus.from(without(
                        () -> sc2ApiDamageBonus().toBuilder(),
                        Data.DamageBonus.Builder::clearAttribute).build()))
                .withMessage("attribute is required");
    }

    @Test
    void throwsExceptionWhenBonusIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DamageBonus.from(without(
                        () -> sc2ApiDamageBonus().toBuilder(),
                        Data.DamageBonus.Builder::clearBonus).build()))
                .withMessage("bonus is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(DamageBonus.class).withNonnullFields("attribute").verify();
    }
}
