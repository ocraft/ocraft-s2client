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

import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.debug.DebugDraw.draw;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DebugDrawTest {

    @Test
    void serializesToSc2ApiDebugDraw() {
        assertThatAllFieldsAreSerialized(draw()
                .texts(debugText().build())
                .lines(debugLine().build())
                .spheres(debugSphere().build())
                .boxes(debugBox().build()).build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Debug.DebugDraw sc2ApiDebugDraw) {
        assertThat(sc2ApiDebugDraw.getTextList()).as("sc2api debug draw: texts").isNotEmpty();
        assertThat(sc2ApiDebugDraw.getLinesList()).as("sc2api debug draw: lines").isNotEmpty();
        assertThat(sc2ApiDebugDraw.getBoxesList()).as("sc2api debug draw: boxes").isNotEmpty();
        assertThat(sc2ApiDebugDraw.getSpheresList()).as("sc2api debug draw: spheres").isNotEmpty();
    }

    @Test
    void serializesToSc2ApiDebugDrawWithBuilder() {
        assertThatAllFieldsAreSerialized(draw()
                .texts(debugText())
                .lines(debugLine())
                .spheres(debugSphere())
                .boxes(debugBox()).build().toSc2Api());
    }

    @Test
    void throwsExceptionWhenOneOfDrawElementsIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((DebugDraw.Builder) draw()).build())
                .withMessage("one of draw elements is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(DebugDraw.class).withNonnullFields("texts", "lines", "boxes", "spheres").verify();
    }
}
