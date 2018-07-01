package com.github.ocraft.s2client.protocol.action;

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
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.syntax.action.ActionChatBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.ActionChatSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.ChannelSyntax;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static com.github.ocraft.s2client.protocol.Preconditions.requireNotEmpty;

public final class ActionChat implements Sc2ApiSerializable<Sc2Api.ActionChat> {

    private static final long serialVersionUID = -912217612082560126L;

    private final Channel channel;
    private final String message;

    public enum Channel implements Sc2ApiSerializable<Sc2Api.ActionChat.Channel> {
        BROADCAST,
        TEAM;

        public static Channel from(Sc2Api.ActionChat.Channel sc2ApiActionChatChannel) {
            require("sc2api action chat channel", sc2ApiActionChatChannel);
            switch (sc2ApiActionChatChannel) {
                case Broadcast:
                    return BROADCAST;
                case Team:
                    return TEAM;
                default:
                    throw new AssertionError("unknown sc2api chat channel: " + sc2ApiActionChatChannel);
            }
        }

        @Override
        public Sc2Api.ActionChat.Channel toSc2Api() {
            switch (this) {
                case BROADCAST:
                    return Sc2Api.ActionChat.Channel.Broadcast;
                case TEAM:
                    return Sc2Api.ActionChat.Channel.Team;
                default:
                    throw new AssertionError("unknown chat channel: " + this);
            }
        }
    }

    public static final class Builder implements ActionChatSyntax, ChannelSyntax, ActionChatBuilder {
        private Channel channel;
        private String message;

        @Override
        public ChannelSyntax of(String message) {
            this.message = message;
            return this;
        }

        @Override
        public ActionChatBuilder toAll() {
            channel = Channel.BROADCAST;
            return this;
        }

        @Override
        public ActionChatBuilder toTeam() {
            channel = Channel.TEAM;
            return this;
        }

        @Override
        public ActionChat build() {
            requireNotEmpty("message", message);
            require("channel", channel);
            return new ActionChat(this);
        }
    }

    public static ActionChatSyntax message() {
        return new Builder();
    }

    private ActionChat(Builder builder) {
        channel = builder.channel;
        message = builder.message;
    }

    private ActionChat(Sc2Api.ActionChat sc2ApiActionChat) {
        this.channel = tryGet(
                Sc2Api.ActionChat::getChannel, Sc2Api.ActionChat::hasChannel
        ).apply(sc2ApiActionChat).map(Channel::from).orElseThrow(required("channel"));

        this.message = tryGet(
                Sc2Api.ActionChat::getMessage, Sc2Api.ActionChat::hasMessage
        ).apply(sc2ApiActionChat).orElseThrow(required("message"));
    }

    public static ActionChat from(Sc2Api.ActionChat sc2ApiActionChat) {
        require("sc2api action chat", sc2ApiActionChat);
        return new ActionChat(sc2ApiActionChat);
    }

    @Override
    public Sc2Api.ActionChat toSc2Api() {
        return Sc2Api.ActionChat.newBuilder().setMessage(message).setChannel(channel.toSc2Api()).build();
    }

    public Channel getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionChat that = (ActionChat) o;

        return channel == that.channel && message.equals(that.message);
    }

    @Override
    public int hashCode() {
        int result = channel.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
