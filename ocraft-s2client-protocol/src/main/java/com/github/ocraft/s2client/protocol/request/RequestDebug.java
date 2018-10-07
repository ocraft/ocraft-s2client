package com.github.ocraft.s2client.protocol.request;

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
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.debug.DebugCommand;
import com.github.ocraft.s2client.protocol.response.ResponseType;
import com.github.ocraft.s2client.protocol.syntax.request.RequestDebugSyntax;

import java.util.ArrayList;
import java.util.Collections;
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
        commands = Collections.unmodifiableList(builder.commands);
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

    @Override
    public ResponseType responseType() {
        return ResponseType.DEBUG;
    }

    public List<DebugCommand> getCommands() {
        return commands;
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
