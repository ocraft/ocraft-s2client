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
import com.github.ocraft.s2client.protocol.data.Units;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ControlGroupTest {

    @Test
    void throwsExceptionWhenSc2ApiControlGroupIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ControlGroup.from(nothing()))
                .withMessage("sc2api control group is required");
    }

    @Test
    void convertsSc2ApiControlGroupToControlGroup() {
        assertThatAllFieldsAreConverted(ControlGroup.from(sc2ApiControlGroup()));
    }

    private void assertThatAllFieldsAreConverted(ControlGroup controlGroup) {
        assertThat(controlGroup.getIndex()).as("control group: index").isEqualTo(CONTROL_GROUP_INDEX);
        assertThat(controlGroup.getLeaderUnitType()).as("control group: leader unit type")
                .isEqualTo(Units.PROTOSS_COLOSSUS);
        assertThat(controlGroup.getCount()).as("control group: count").isEqualTo(UNIT_COUNT);
    }

    @Test
    void throwsExceptionWhenIndexIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ControlGroup.from(without(
                        () -> sc2ApiControlGroup().toBuilder(),
                        Ui.ControlGroup.Builder::clearControlGroupIndex).build()))
                .withMessage("index is required");
    }

    @Test
    void throwsExceptionWhenLeaderUnitTypeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ControlGroup.from(without(
                        () -> sc2ApiControlGroup().toBuilder(),
                        Ui.ControlGroup.Builder::clearLeaderUnitType).build()))
                .withMessage("leader unit type is required");
    }

    @Test
    void throwsExceptionWhenCountIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ControlGroup.from(without(
                        () -> sc2ApiControlGroup().toBuilder(),
                        Ui.ControlGroup.Builder::clearCount).build()))
                .withMessage("count is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ControlGroup.class).withNonnullFields("leaderUnitType").verify();
    }
}
