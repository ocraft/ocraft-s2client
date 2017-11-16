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
package com.github.ocraft.s2client.protocol.action.observer;

import SC2APIProtocol.Sc2Api;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Fixtures.PLAYER_ID;
import static com.github.ocraft.s2client.protocol.action.Actions.Observer.playerPerspective;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionObserverPlayerPerspectiveTest {

    @Test
    void serializesToSc2ApiActionObserverPlayerPerspective() {
        assertThatAllFieldsInRequestAreSerialized(
                playerPerspective().ofPlayer(PLAYER_ID).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(
            Sc2Api.ActionObserverPlayerPerspective sc2ApiObserverPlayerPerspective) {
        assertThat(sc2ApiObserverPlayerPerspective.getPlayerId()).as("sc2api observer player perspective: player id")
                .isEqualTo(PLAYER_ID);
    }

    @Test
    void throwsExceptionWhenPlayerIdIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((ActionObserverPlayerPerspective.Builder) playerPerspective()).build())
                .withMessage("player id is required");
    }

    @Test
    void serializesZeroAsValueForAllPlayers() {
        assertThat(playerPerspective().ofAll().build().toSc2Api().getPlayerId())
                .as("sc2api observer player perspective: all player").isEqualTo(0);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionObserverPlayerPerspective.class).verify();
    }
}