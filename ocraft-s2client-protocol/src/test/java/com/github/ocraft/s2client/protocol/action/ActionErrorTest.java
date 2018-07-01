package com.github.ocraft.s2client.protocol.action;

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

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.unit.Tag;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionErrorTest {
    @Test
    void throwsExceptionWhenSc2ApiActionErrorIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionError.from(nothing()))
                .withMessage("sc2api action error is required");
    }

    @Test
    void convertsSc2ApiActionErrorToActionError() {
        assertThatAllFieldsAreConverted(ActionError.from(sc2ApiActionError()));
    }

    private void assertThatAllFieldsAreConverted(ActionError actionError) {
        assertThat(actionError.getUnitTag()).as("action error: unit tag").hasValue(Tag.from(UNIT_TAG));
        assertThat(actionError.getAbility()).as("action error: ability").hasValue(Abilities.EFFECT_PSI_STORM);
        assertThat(actionError.getActionResult()).as("action error: action result")
                .isEqualTo(ActionResult.COULDNT_REACH_TARGET);
    }

    @Test
    void throwsExceptionWhenActionResultIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionError.from(without(
                        () -> sc2ApiActionError().toBuilder(),
                        Sc2Api.ActionError.Builder::clearResult).build()))
                .withMessage("action result is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionError.class).withNonnullFields("actionResult").verify();
    }
}
