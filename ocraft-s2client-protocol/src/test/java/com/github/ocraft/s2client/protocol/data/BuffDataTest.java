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
package com.github.ocraft.s2client.protocol.data;

import SC2APIProtocol.Data;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BuffDataTest {
    @Test
    void throwsExceptionWhenSc2ApiBuffDataIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> BuffData.from(nothing()))
                .withMessage("sc2api buff data is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiUnit() {
        assertThatAllFieldsAreConverted(BuffData.from(sc2ApiBuffData()));
    }

    private void assertThatAllFieldsAreConverted(BuffData buff) {
        assertThat(buff.getBuff()).as("buff: id").isNotNull();
        assertThat(buff.getName()).as("buff: name").isEqualTo(BUFF_NAME);
    }

    @Test
    void throwsExceptionWhenBuffIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> BuffData.from(without(
                        () -> sc2ApiBuffData().toBuilder(),
                        Data.BuffData.Builder::clearBuffId).build()))
                .withMessage("buff is required");
    }

    @Test
    void throwsExceptionWhenNameIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> BuffData.from(without(
                        () -> sc2ApiBuffData().toBuilder(),
                        Data.BuffData.Builder::clearName).build()))
                .withMessage("name is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(BuffData.class).withNonnullFields("buff", "name").verify();
    }

}