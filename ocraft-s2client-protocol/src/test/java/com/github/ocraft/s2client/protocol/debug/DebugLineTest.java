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
import com.github.ocraft.s2client.protocol.Defaults;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.debug.DebugLine.line;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DebugLineTest {

    @Test
    void serializesToSc2ApiDebugLine() {
        assertThatAllFieldsAreSerialized(debugLine().build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Debug.DebugLine sc2ApiLine) {
        assertThat(sc2ApiLine.hasColor()).as("sc2api line: has color").isTrue();
        assertThat(sc2ApiLine.hasLine()).as("sc2api line: has line").isTrue();
    }

    @Test
    void throwsExceptionWhenColorIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(line().of(P0, P1).withColor(nothing())).build())
                .withMessage("color is required");
    }

    private DebugLine.Builder fullAccessTo(Object obj) {
        return (DebugLine.Builder) obj;
    }

    @Test
    void throwsExceptionWhenLineIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(line()).withColor(SAMPLE_COLOR).build())
                .withMessage("line is required");
    }

    @Test
    void hasDefaultColorIfNotSet() {
        assertThat(line().of(P0, P1).build().getColor()).as("default color").isEqualTo(Defaults.COLOR);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(DebugLine.class).withNonnullFields("color", "line").verify();
    }

}
