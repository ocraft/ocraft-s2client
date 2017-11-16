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