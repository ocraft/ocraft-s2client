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
package com.github.ocraft.s2client.protocol.action.ui;

import SC2APIProtocol.Ui;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiCargoPanelUnload.cargoPanelUnload;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionUiCargoPanelUnloadTest {
    @Test
    void throwsExceptionWhenSc2ApiActionUiCargoPanelUnloadIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiCargoPanelUnload.from(nothing()))
                .withMessage("sc2api action ui cargo panel unload is required");
    }

    @Test
    void convertsSc2ApiActionUiCargoPanelUnloadToActionUiCargoPanelUnload() {
        assertThatAllFieldsAreConverted(ActionUiCargoPanelUnload.from(sc2ApiActionUiCargoPanelUnload()));
    }

    private void assertThatAllFieldsAreConverted(ActionUiCargoPanelUnload cargoPanelUnload) {
        assertThat(cargoPanelUnload.getUnitIndex()).as("action ui cargo panel unload: unit index").
                isEqualTo(UNIT_INDEX);
    }

    @Test
    void throwsExceptionWhenUnitIndexIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiCargoPanelUnload.from(without(
                        () -> sc2ApiActionUiCargoPanelUnload().toBuilder(),
                        Ui.ActionCargoPanelUnload.Builder::clearUnitIndex).build()))
                .withMessage("unit index is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((ActionUiCargoPanelUnload.Builder) cargoPanelUnload()).build())
                .withMessage("unit index is required");
    }

    @Test
    void serializesToSc2ApiActionUiCargoPanelUnload() {
        assertThatAllFieldsInRequestAreSerialized(cargoPanelUnload().of(UNIT_INDEX).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Ui.ActionCargoPanelUnload sc2ApiUiCargoPanelUnload) {
        assertThat(sc2ApiUiCargoPanelUnload.getUnitIndex()).as("sc2api ui cargo panel unload: index")
                .isEqualTo(UNIT_INDEX);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionUiCargoPanelUnload.class).verify();
    }
}