package com.github.ocraft.s2client.protocol.action;

import SC2APIProtocol.Sc2Api;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.action.ActionChat.Channel.BROADCAST;
import static com.github.ocraft.s2client.protocol.action.ActionChat.Channel.TEAM;
import static com.github.ocraft.s2client.protocol.action.ActionChat.message;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.of;

class ActionChatTest {

    @Test
    void throwsExceptionWhenSc2ApiActionChatIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionChat.from(nothing()))
                .withMessage("sc2api action chat is required");
    }

    @Test
    void convertsSc2ApiActionChatToActionChat() {
        assertThatAllFieldsAreConverted(ActionChat.from(sc2ApiActionWithChat()));
    }

    private void assertThatAllFieldsAreConverted(ActionChat chat) {
        assertThat(chat.getChannel()).as("action chat: channel").isEqualTo(BROADCAST);
        assertThat(chat.getMessage()).as("action chat: message").isEqualTo(CHAT_MESSAGE);
    }

    @Test
    void throwsExceptionWhenChannelIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionChat.from(without(
                        () -> sc2ApiActionWithChat().toBuilder(),
                        Sc2Api.ActionChat.Builder::clearChannel).build()))
                .withMessage("channel is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(message().of(CHAT_MESSAGE)).build())
                .withMessage("channel is required");
    }

    private ActionChat.Builder fullAccessTo(Object obj) {
        return (ActionChat.Builder) obj;
    }

    @Test
    void throwsExceptionWhenMessageIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionChat.from(without(
                        () -> sc2ApiActionWithChat().toBuilder(),
                        Sc2Api.ActionChat.Builder::clearMessage).build()))
                .withMessage("message is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(message()).toTeam().build())
                .withMessage("message is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(message().of("")).toTeam().build())
                .withMessage("message is required");
    }

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "chatChannelMappings")
    void mapsSc2ApiChatChannel(Sc2Api.ActionChat.Channel sc2ApiChatChannel, ActionChat.Channel expectedChatChannel) {
        assertThat(ActionChat.Channel.from(sc2ApiChatChannel)).isEqualTo(expectedChatChannel);
    }

    private static Stream<Arguments> chatChannelMappings() {
        return Stream.of(
                of(Sc2Api.ActionChat.Channel.Broadcast, BROADCAST),
                of(Sc2Api.ActionChat.Channel.Team, TEAM));
    }

    @ParameterizedTest(name = "\"{0}\" is serialization of {1}")
    @MethodSource(value = "chatChannelMappings")
    void serializesToSc2ApiChatChannel(
            Sc2Api.ActionChat.Channel expectedSc2ApiChatChannel, ActionChat.Channel chatChannel) {
        assertThat(chatChannel.toSc2Api()).isEqualTo(expectedSc2ApiChatChannel);
    }

    @Test
    void throwsExceptionWhenSc2ApiChatChannelIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionChat.Channel.from(nothing()))
                .withMessage("sc2api action chat channel is required");
    }

    @Test
    void serializesToSc2ApiActionChat() {
        assertThatAllFieldsInRequestAreSerialized(message().of(CHAT_MESSAGE).toTeam().build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Sc2Api.ActionChat sc2ApiChat) {
        assertThat(sc2ApiChat.getMessage()).as("sc2api chat: message").isEqualTo(CHAT_MESSAGE);
        assertThat(sc2ApiChat.getChannel()).as("sc2api chat: channel").isEqualTo(Sc2Api.ActionChat.Channel.Team);
    }

    @Test
    void serializesBroadcastChannelIfRequested() {
        assertThat(message().of(CHAT_MESSAGE).toAll().build().toSc2Api().getChannel()).as("sc2api chat: channel")
                .isEqualTo(Sc2Api.ActionChat.Channel.Broadcast);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionChat.class).withNonnullFields("channel", "message").verify();
    }
}