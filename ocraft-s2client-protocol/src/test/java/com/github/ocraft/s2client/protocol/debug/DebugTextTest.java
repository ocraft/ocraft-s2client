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
import com.github.ocraft.s2client.protocol.spatial.Point;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.debug.DebugText.text;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DebugTextTest {

    @Test
    void serializesToSc2ApiDebugText() {
        assertThatAllFieldsAreSerialized(debugText().build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Debug.DebugText sc2ApiText) {
        assertThat(sc2ApiText.hasColor()).as("sc2api text: has color").isTrue();
        assertThat(sc2ApiText.getText()).as("sc2api text: has text").isEqualTo(DEBUG_TEXT);
        assertThat(sc2ApiText.getSize()).as("sc2api text: size").isEqualTo(DEBUG_TEXT_SIZE);
        assertThat(sc2ApiText.hasVirtualPos()).as("sc2api text: has virtual pos").isTrue();
        assertThat(sc2ApiText.hasWorldPos()).as("sc2api text: has world pos").isFalse();
    }

    @Test
    void serializesOnlyRecentlyAddedPosition() {
        assertCorrectPositionCase(
                fullAccessTo(text().of(DEBUG_TEXT).withColor(SAMPLE_COLOR).onMap(Point.of(0.5f, 0.5f, 0.5f)))
                        .on(Point.of(0.5f, 0.5f))
                        .build().toSc2Api());
        assertCorrectPositionCaseAfterOrderChange(
                fullAccessTo(text().of(DEBUG_TEXT).withColor(SAMPLE_COLOR).on(Point.of(0.5f, 0.5f)))
                        .onMap(Point.of(0.5f, 0.5f, 0.5f))
                        .build().toSc2Api());
    }

    private DebugText.Builder fullAccessTo(Object obj) {
        return (DebugText.Builder) obj;
    }

    private void assertCorrectPositionCase(Debug.DebugText sc2ApiText) {
        assertThat(sc2ApiText.hasVirtualPos()).as("sc2api text: has virtual pos").isTrue();
        assertThat(sc2ApiText.hasWorldPos()).as("sc2api text: has world pos").isFalse();
    }

    private void assertCorrectPositionCaseAfterOrderChange(Debug.DebugText sc2ApiText) {
        assertThat(sc2ApiText.hasVirtualPos()).as("sc2api text: has virtual pos").isFalse();
        assertThat(sc2ApiText.hasWorldPos()).as("sc2api text: has world pos").isTrue();
    }

    @Test
    void throwsExceptionWhenColorIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(text().of(DEBUG_TEXT)).withColor(nothing()).build())
                .withMessage("color is required");
    }

    @Test
    void throwsExceptionWhenTextIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(text()).withColor(SAMPLE_COLOR).build())
                .withMessage("text is required");
    }

    @Test
    void hasDefaultColorIfNotSet() {
        assertThat(text().of(DEBUG_TEXT).build().getColor()).as("default color").isEqualTo(Defaults.COLOR);
    }

    @Test
    void throwsExceptionWhenPointIsNotInValidRange() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> text().of(DEBUG_TEXT).on(Point.of(-1, 1)).build())
                .withMessage("point [x] has value -1.0 and is lower than 0.0");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> text().of(DEBUG_TEXT).on(Point.of(1.1f, 1)).build())
                .withMessage("virtualized point 2d [x] has value 1.1 and is greater than 1.0");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> text().of(DEBUG_TEXT).on(Point.of(1.0f, -1)).build())
                .withMessage("point [y] has value -1.0 and is lower than 0.0");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> text().of(DEBUG_TEXT).on(Point.of(1.0f, 1.1f)).build())
                .withMessage("virtualized point 2d [y] has value 1.1 and is greater than 1.0");
    }


    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(DebugText.class).withNonnullFields("color", "text").verify();
    }

}