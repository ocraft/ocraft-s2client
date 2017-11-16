package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.debug.DebugCommand;
import com.github.ocraft.s2client.protocol.syntax.request.RequestDebugSyntax;

import java.util.ArrayList;
import java.util.List;

import static com.github.ocraft.s2client.protocol.Preconditions.requireNotEmpty;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public final class RequestDebug extends Request {

    private static final long serialVersionUID = -7758280351468974091L;

    private final List<DebugCommand> commands;

    public static final class Builder implements BuilderSyntax<RequestDebug>, RequestDebugSyntax {

        private List<DebugCommand> commands = new ArrayList<>();

        @Override
        public BuilderSyntax<RequestDebug> with(DebugCommand... commands) {
            this.commands.addAll(asList(commands));
            return this;
        }

        @Override
        public RequestDebug build() {
            requireNotEmpty("command list", commands);
            return new RequestDebug(this);
        }
    }

    private RequestDebug(Builder builder) {
        commands = builder.commands;
    }

    public static RequestDebugSyntax debug() {
        return new Builder();
    }

    @Override
    public Sc2Api.Request toSc2Api() {
        return Sc2Api.Request.newBuilder()
                .setDebug(Sc2Api.RequestDebug.newBuilder()
                        .addAllDebug(commands.stream().map(DebugCommand::toSc2Api).collect(toList()))
                        .build())
                .build();
    }

    public List<DebugCommand> getCommands() {
        return new ArrayList<>(commands);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestDebug that = (RequestDebug) o;

        return commands.equals(that.commands);
    }

    @Override
    public int hashCode() {
        return commands.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
