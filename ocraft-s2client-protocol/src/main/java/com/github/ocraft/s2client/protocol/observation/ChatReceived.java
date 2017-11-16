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
