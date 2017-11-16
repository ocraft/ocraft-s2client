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
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiActionUiSelectWarpGates;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiSelectWarpGates.selectWarpGates;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionUiSelectWarpGatesTest {

    @Test
    void throwsExceptionWhenSc2ApiActionUiSelectWarpGatesIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiSelectWarpGates.from(nothing()))
                .withMessage("sc2api action ui select warp gates is required");
    }

    @Test
    void convertsSc2ApiActionUiSelectWarpGatesToActionUiSelectWarpGates() {
        assertThatAllFieldsAreConverted(ActionUiSelectWarpGates.from(sc2ApiActionUiSelectWarpGates()));
    }

    private void assertThatAllFieldsAreConverted(ActionUiSelectWarpGates selectWarpGates) {
        assertThat(selectWarpGates.isSelectionAdd()).as("action ui select warp gates: selection add").isTrue();
    }

    @Test
    void hasDefaultValueForSelectionAddFieldIfNotProvided() {
        ActionUiSelectWarpGates unitSelectionRect = ActionUiSelectWarpGates.from(without(
                () -> sc2ApiActionUiSelectWarpGates().toBuilder(),
                Ui.ActionSelectWarpGates.Builder::clearSelectionAdd).build());

        assertThat(unitSelectionRect.isSelectionAdd())
                .as("action ui select warp gates: default selection add value")
                .isFalse();
    }

    @Test
    void serializesToSc2ApiActionUiSelectWarpGates() {
        assertThatAllFieldsInRequestAreSerialized(selectWarpGates().add().build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Ui.ActionSelectWarpGates sc2ApiUiSelectWarpGates) {
        assertThat(sc2ApiUiSelectWarpGates.getSelectionAdd()).as("sc2api ui select warp gates: selection add").isTrue();
    }

    @Test
    void serializesDefaultValueForSelectionAddFieldsIfNotProvided() {
        assertThat(selectWarpGates().build().toSc2Api().getSelectionAdd())
                .as("sc2api action ui select WarpGates: default selection add value").isFalse();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionUiSelectWarpGates.class).verify();
    }
}