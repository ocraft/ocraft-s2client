package com.github.ocraft.s2client.protocol.game;

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
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PortSetTest {

    @Test
    void throwsExceptionWhenPortIsLowerThanOne() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PortSet.of(0, 1))
                .withMessage("port must be greater than 0");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PortSet.of(1, 0))
                .withMessage("port must be greater than 0");
    }

    @Test
    void serializesToSc2ApiPortSet() {
        Sc2Api.PortSet sc2ApiPortSet = PortSet.of(1, 2).toSc2Api();

        assertThat(sc2ApiPortSet.getGamePort()).as("game port is serialized").isEqualTo(1);
        assertThat(sc2ApiPortSet.getBasePort()).as("base port is serialized").isEqualTo(2);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(PortSet.class).verify();
    }


}
