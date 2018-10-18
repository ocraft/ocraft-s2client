package com.github.ocraft.s2client.protocol.observation.ui;

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

import SC2APIProtocol.Ui;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiBuildItem;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BuildItemTest {
    @Test
    void throwsExceptionWhenSc2ApiBuildItemIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> BuildItem.from(nothing()))
                .withMessage("sc2api build item is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiBuildItem() {
        assertThatAllFieldsAreConverted(BuildItem.from(sc2ApiBuildItem()));
    }

    private void assertThatAllFieldsAreConverted(BuildItem buildItem) {
        assertThat(buildItem.getAbility()).as("build item: ability").isNotNull();
        assertThat(buildItem.getBuildProgress()).as("build item: build progress").isGreaterThan(0.0f);
    }

    @Test
    void throwsExceptionWhenAbilityIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> BuildItem.from(
                        without(() -> sc2ApiBuildItem().toBuilder(), Ui.BuildItem.Builder::clearAbilityId).build()))
                .withMessage("ability is required");
    }

    @Test
    void throwsExceptionWhenBuildProgressIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> BuildItem.from(
                        without(() -> sc2ApiBuildItem().toBuilder(), Ui.BuildItem.Builder::clearBuildProgress).build()))
                .withMessage("build progress is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(BuildItem.class).withNonnullFields("ability").verify();
    }
}
