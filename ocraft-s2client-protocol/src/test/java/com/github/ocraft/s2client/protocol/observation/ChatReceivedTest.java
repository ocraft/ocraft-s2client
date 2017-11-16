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
package com.github.ocraft.s2client.protocol.observation;

import SC2APIProtocol.Sc2Api;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ChatReceivedTest {

    @Test
    void throwsExceptionWhenSc2ApiChatReceivedIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ChatReceived.from(nothing()))
                .withMessage("sc2api chat received is required");
    }

    @Test
    void convertsSc2ApiChatReceivedToChatReceived() {
        assertThatAllFieldsAreConverted(ChatReceived.from(sc2ApiChatReceived()));
    }

    private void assertThatAllFieldsAreConverted(ChatReceived chatReceived) {
        assertThat(chatReceived.getPlayerId()).as("chat received: player id").isEqualTo(PLAYER_ID);
        assertThat(chatReceived.getMessage()).as("chat received: message").isEqualTo(CHAT_MESSAGE);
    }

    @Test
    void throwsExceptionWhenPlayerIdInNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ChatReceived.from(sc2ApiChatReceivedWithoutPlayerId()))
                .withMessage("player id is required");
    }

    private Sc2Api.ChatReceived sc2ApiChatReceivedWithoutPlayerId() {
        return without(
                () -> sc2ApiChatReceived().toBuilder(),
                Sc2Api.ChatReceived.Builder::clearPlayerId).build();
    }

    @Test
    void throwsExceptionWhenMessageInNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ChatReceived.from(sc2ApiChatReceivedWithoutMessage()))
                .withMessage("message is required");
    }

    private Sc2Api.ChatReceived sc2ApiChatReceivedWithoutMessage() {
        return without(
                () -> sc2ApiChatReceived().toBuilder(),
                Sc2Api.ChatReceived.Builder::clearMessage).build();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ChatReceived.class).withNonnullFields("message").verify();
    }
}