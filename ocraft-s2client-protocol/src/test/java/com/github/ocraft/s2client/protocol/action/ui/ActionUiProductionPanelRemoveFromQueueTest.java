package com.github.ocraft.s2client.protocol.action.ui;

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
import com.github.ocraft.s2client.protocol.syntax.action.ui.ActionUiProductionPanelRemoveFromQueueBuilder;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiProductionPanelRemoveFromQueue.removeFromQueue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionUiProductionPanelRemoveFromQueueTest {
    @Test
    void throwsExceptionWhenSc2ApiActionUiProductionPanelRemoveFromQueueIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiProductionPanelRemoveFromQueue.from(nothing()))
                .withMessage("sc2api action ui production panel remove from queue is required");
    }

    @Test
    void convertsSc2ApiActionUiProductionPanelRemoveFromQueueToActionUiProductionPanelRemoveFromQueue() {
        assertThatAllFieldsAreConverted(ActionUiProductionPanelRemoveFromQueue.from(
                sc2ApiActionUiProductionPanelRemoveFromQueue()));
    }

    private void assertThatAllFieldsAreConverted(ActionUiProductionPanelRemoveFromQueue productionPanel) {
        assertThat(productionPanel.getUnitIndex())
                .as("action ui production panel remove from queue: unit index")
                .isEqualTo(UNIT_INDEX);
    }

    @Test
    void throwsExceptionWhenUnitIndexIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiProductionPanelRemoveFromQueue.from(without(
                        () -> sc2ApiActionUiProductionPanelRemoveFromQueue().toBuilder(),
                        Ui.ActionProductionPanelRemoveFromQueue.Builder::clearUnitIndex).build()))
                .withMessage("unit index is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((ActionUiProductionPanelRemoveFromQueueBuilder) removeFromQueue()).build())
                .withMessage("unit index is required");
    }

    @Test
    void serializesToSc2ApiActionUiProductionPanelRemoveFromQueue() {
        assertThatAllFieldsInRequestAreSerialized(removeFromQueue().of(UNIT_INDEX).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(
            Ui.ActionProductionPanelRemoveFromQueue sc2ApiUiProductionPanelRemoveFromQueue) {
        assertThat(sc2ApiUiProductionPanelRemoveFromQueue.getUnitIndex()).as("sc2api ui production panel: index")
                .isEqualTo(UNIT_INDEX);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionUiProductionPanelRemoveFromQueue.class).verify();
    }
}
