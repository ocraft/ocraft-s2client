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
package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import com.github.ocraft.s2client.protocol.Defaults;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.debug.DebugBox.box;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DebugBoxTest {

    @Test
    void serializesToSc2ApiDebugBox() {
        assertThatAllFieldsAreSerialized(debugBox().build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Debug.DebugBox sc2ApiBox) {
        assertThat(sc2ApiBox.hasColor()).as("sc2api box: has color").isTrue();
        assertThat(sc2ApiBox.hasMin()).as("sc2api box: has min").isTrue();
        assertThat(sc2ApiBox.hasMax()).as("sc2api box: has max").isTrue();
    }

    @Test
    void throwsExceptionWhenColorIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> box().of(P0, P1).withColor(nothing()).build())
                .withMessage("color is required");
    }

    @Test
    void throwsExceptionWhenMinimumIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> box().of(nothing(), P1).withColor(SAMPLE_COLOR).build())
                .withMessage("min is required");
    }

    @Test
    void throwsExceptionWhenMaximumIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> box().of(P0, nothing()).withColor(SAMPLE_COLOR).build())
                .withMessage("max is required");
    }

    @Test
    void hasDefaultColorIfNotSet() {
        assertThat(box().of(P0, P1).build().getColor()).as("default color").isEqualTo(Defaults.COLOR);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(DebugBox.class).withNonnullFields("color", "min", "max").verify();
    }

}