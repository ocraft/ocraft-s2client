package com.github.ocraft.s2client.protocol.spatial;

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

import SC2APIProtocol.Common;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class Size2dITest {
    @Test
    void throwsExceptionWhenSc2ApiSize2dIIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Size2dI.from(nothing()))
                .withMessage("sc2api size2dI is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiSize2dI() {
        assertThatAllFieldsAreConverted(Size2dI.from(sc2ApiSize2dI()));
    }

    private void assertThatAllFieldsAreConverted(Size2dI size2dI) {
        assertThat(size2dI.getX()).as("size2dI: x").isEqualTo(SCREEN_SIZE_X);
        assertThat(size2dI.getY()).as("size2dI: y").isEqualTo(SCREEN_SIZE_Y);
    }

    @Test
    void throwsExceptionWhenXIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Size2dI.from(without(
                        () -> sc2ApiSize2dI().toBuilder(),
                        Common.Size2DI.Builder::clearX).build()))
                .withMessage("x is required");
    }

    @Test
    void throwsExceptionWhenYIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Size2dI.from(without(
                        () -> sc2ApiSize2dI().toBuilder(),
                        Common.Size2DI.Builder::clearY).build()))
                .withMessage("y is required");
    }

    @Test
    void serializesToSc2ApiSize2dI() {
        Common.Size2DI sc2ApiSize2dI = Size2dI.of(10, 20).toSc2Api();
        assertThat(sc2ApiSize2dI.getX()).as("sc2api size2di: x").isEqualTo(10);
        assertThat(sc2ApiSize2dI.getY()).as("sc2api size2di: Y").isEqualTo(20);
    }

    @Test
    void throwsExceptionWhenSize2dIIsNotInValidRange() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Size2dI.of(-1, 1))
                .withMessage("size2di [x] has value -1 and is lower than 0");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Size2dI.of(1, -1))
                .withMessage("size2di [y] has value -1 and is lower than 0");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(Size2dI.class).verify();
    }
}
