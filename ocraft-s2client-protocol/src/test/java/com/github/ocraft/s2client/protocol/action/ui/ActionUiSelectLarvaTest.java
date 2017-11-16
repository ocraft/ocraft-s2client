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
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiActionUiSelectLarva;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiSelectLarva.selectLarva;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionUiSelectLarvaTest {
    @Test
    void throwsExceptionWhenSc2ApiActionUiSelectLarvaIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiSelectLarva.from(nothing()))
                .withMessage("sc2api action ui select larva is required");
    }

    @Test
    void convertsSc2ApiActionUiSelectLarvaToActionUiSelectLarva() {
        assertThat(ActionUiSelectLarva.from(sc2ApiActionUiSelectLarva())).as("action ui select larva").isNotNull();
    }

    @Test
    void serializesToSc2ApiActionUiSelectLarva() {
        assertThatAllFieldsInRequestAreSerialized(selectLarva().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Ui.ActionSelectLarva sc2ApiUiSelectLarva) {
        assertThat(sc2ApiUiSelectLarva).as("sc2api ui select larva").isNotNull();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionUiSelectLarva.class).verify();
    }
}