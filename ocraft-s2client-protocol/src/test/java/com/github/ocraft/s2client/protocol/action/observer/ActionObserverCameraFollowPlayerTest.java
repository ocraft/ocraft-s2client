package com.github.ocraft.s2client.protocol.action.observer;

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

import static com.github.ocraft.s2client.protocol.Fixtures.PLAYER_ID;
import static com.github.ocraft.s2client.protocol.action.Actions.Observer.cameraFollowPlayer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionObserverCameraFollowPlayerTest {
    @Test
    void serializesToSc2ApiActionObserverCameraFollowPlayer() {
        assertThatAllFieldsInRequestAreSerialized(
                cameraFollowPlayer().withId(PLAYER_ID).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(
            Sc2Api.ActionObserverCameraFollowPlayer sc2ApiObserverCameraFollowPlayer) {
        assertThat(sc2ApiObserverCameraFollowPlayer.getPlayerId()).as("sc2api observer camera follow player: player id")
                .isEqualTo(PLAYER_ID);
    }

    @Test
    void throwsExceptionWhenPlayerIdIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((ActionObserverCameraFollowPlayer.Builder) cameraFollowPlayer()).build())
                .withMessage("player id is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionObserverCameraFollowPlayer.class).verify();
    }
}
