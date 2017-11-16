package com.github.ocraft.s2client.protocol.syntax.action;

public interface ChannelSyntax {
    ActionChatBuilder toAll();

    ActionChatBuilder toTeam();
}
