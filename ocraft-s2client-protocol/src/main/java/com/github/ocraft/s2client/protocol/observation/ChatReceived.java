package com.github.ocraft.s2client.protocol.observation;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ChatReceived implements Serializable {

    private static final long serialVersionUID = -7267540467743676644L;

    private final int playerId;
    private final String message;

    private ChatReceived(Sc2Api.ChatReceived sc2ApiChatReceived) {
        this.playerId = tryGet(
                Sc2Api.ChatReceived::getPlayerId, Sc2Api.ChatReceived::hasPlayerId
        ).apply(sc2ApiChatReceived).orElseThrow(required("player id"));

        this.message = tryGet(
                Sc2Api.ChatReceived::getMessage, Sc2Api.ChatReceived::hasMessage
        ).apply(sc2ApiChatReceived).orElseThrow(required("message"));
    }

    public static ChatReceived from(Sc2Api.ChatReceived sc2ApiChatReceived) {
        require("sc2api chat received", sc2ApiChatReceived);
        return new ChatReceived(sc2ApiChatReceived);
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatReceived that = (ChatReceived) o;

        return playerId == that.playerId && message.equals(that.message);
    }

    @Override
    public int hashCode() {
        int result = playerId;
        result = 31 * result + message.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
