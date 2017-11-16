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
package com.github.ocraft.s2client.protocol.observation.raw;

import SC2APIProtocol.Raw;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiEvent;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class EventTest {
    @Test
    void throwsExceptionWhenSc2ApiEventIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Event.from(nothing()))
                .withMessage("sc2api event is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiEvent() {
        assertThatAllFieldsAreConverted(Event.from(sc2ApiEvent()));
    }

    private void assertThatAllFieldsAreConverted(Event event) {
        assertThat(event.getDeadUnits()).as("event: dead units").isNotEmpty();
    }

    @Test
    void hasEmptySetOfEffectsWhenNotProvided() {
        assertThat(Event.from(
                without(() -> sc2ApiEvent().toBuilder(), Raw.Event.Builder::clearDeadUnits).build()
        ).getDeadUnits()).as("event: empty dead units set").isEmpty();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(Event.class).withNonnullFields("deadUnits").verify();
    }
}