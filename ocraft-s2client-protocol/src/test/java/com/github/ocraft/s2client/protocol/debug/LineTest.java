package com.github.ocraft.s2client.protocol.debug;

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

import SC2APIProtocol.Debug;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.P0;
import static com.github.ocraft.s2client.protocol.Fixtures.P1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class LineTest {

    @Test
    void serializesToSc2ApiLine() {
        assertThatAllFieldsInRequestAreSerialized(Line.of(P0, P1).toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Debug.Line sc2ApiLine) {
        assertThat(sc2ApiLine.hasP0()).as("sc2api line: p0").isTrue();
        assertThat(sc2ApiLine.hasP1()).as("sc2api line: p1").isTrue();
    }

    @Test
    void throwsExceptionWhenPointsAreNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Line.of(nothing(), P1))
                .withMessage("p0 is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Line.of(P0, nothing()))
                .withMessage("p1 is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(Line.class).withNonnullFields("p0", "p1").verify();
    }
}
