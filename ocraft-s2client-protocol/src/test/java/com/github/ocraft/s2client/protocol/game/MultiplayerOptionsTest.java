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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.game.MultiplayerOptions.multiplayerSetup;
import static com.github.ocraft.s2client.protocol.game.MultiplayerOptions.multiplayerSetupFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class MultiplayerOptionsTest {

    @Test
    void throwsExceptionWhenSharedPortIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(multiplayerSetup()).build())
                .withMessage("shared port is required");
    }

    private MultiplayerOptions.Builder fullAccessTo(Object obj) {
        return (MultiplayerOptions.Builder) obj;
    }

    @Test
    void throwsExceptionWhenServerPortIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(multiplayerSetup().sharedPort(1)).build())
                .withMessage("server port is required");
    }

    @Test
    void throwsExceptionWhenClientPortsAreEmpty() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(multiplayerSetup().sharedPort(1).serverPort(PortSet.of(1, 2))).build())
                .withMessage("client port list is required");
    }

    @Test
    void throwsExceptionWhenSharedPortLowerThanOne() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> multiplayerSetup().sharedPort(0))
                .withMessage("shared port must be greater than 0");
    }

    @Test
    void createsDefaultPortSetupFromGivenPortAndPlayerCount() {
        MultiplayerOptions multiplayerOptions = multiplayerSetupFor(5000, 2);

        assertThat(multiplayerOptions.getSharedPort()).as("multiplayer port setup: shared port").isEqualTo(5001);
        assertThat(multiplayerOptions.getServerPort()).as("multiplayer port setup: server port")
                .isEqualTo(PortSet.of(5002, 5003));
        assertThat(multiplayerOptions.getClientPorts()).as("multiplayer port setup: client ports")
                .containsExactly(PortSet.of(5004, 5005), PortSet.of(5006, 5007));
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(MultiplayerOptions.class).withNonnullFields("serverPort", "clientPorts").verify();
    }
}
