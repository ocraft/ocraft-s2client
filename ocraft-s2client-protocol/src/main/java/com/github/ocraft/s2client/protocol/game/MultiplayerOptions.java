package com.github.ocraft.s2client.protocol.game;

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

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.syntax.request.ClientPortsSyntax;
import com.github.ocraft.s2client.protocol.syntax.request.MultiplayerOptionsSyntax;
import com.github.ocraft.s2client.protocol.syntax.request.ServerPortSyntax;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static com.github.ocraft.s2client.protocol.Preconditions.requireNotEmpty;
import static java.util.Arrays.asList;

public final class MultiplayerOptions implements Serializable {

    private static final long serialVersionUID = 2030343854676677658L;
    private final int sharedPort;
    private final PortSet serverPort;
    private final Set<PortSet> clientPorts;

    private MultiplayerOptions(Builder builder) {
        this.sharedPort = builder.sharedPort;
        this.serverPort = builder.serverPort;
        this.clientPorts = builder.clientPorts;
    }

    public static final class Builder implements MultiplayerOptionsSyntax, ServerPortSyntax, ClientPortsSyntax,
            BuilderSyntax<MultiplayerOptions> {

        private Integer sharedPort;
        private PortSet serverPort;
        private Set<PortSet> clientPorts = new HashSet<>();

        @Override
        public ServerPortSyntax sharedPort(int sharedPort) {
            if (sharedPort < 1) throw new IllegalArgumentException("shared port must be greater than 0");
            this.sharedPort = sharedPort;
            return this;
        }

        @Override
        public ClientPortsSyntax serverPort(PortSet serverPort) {
            this.serverPort = serverPort;
            return this;
        }

        @Override
        public BuilderSyntax<MultiplayerOptions> clientPorts(PortSet... clientPorts) {
            this.clientPorts.addAll(asList(clientPorts));
            return this;
        }

        @Override
        public BuilderSyntax<MultiplayerOptions> clientPorts(Collection<PortSet> clientPorts) {
            this.clientPorts.addAll(clientPorts);
            return this;
        }

        @Override
        public MultiplayerOptions build() {
            require("shared port", sharedPort);
            require("server port", serverPort);
            requireNotEmpty("client port list", clientPorts);
            return new MultiplayerOptions(this);
        }
    }

    public static MultiplayerOptionsSyntax multiplayerSetup() {
        return new Builder();
    }

    public static MultiplayerOptions multiplayerSetupFor(int portStart, int playerCount) {
        int sharedPort = ++portStart;
        PortSet serverPort = PortSet.of(++portStart, ++portStart);
        Set<PortSet> clientPorts = new HashSet<>(playerCount);
        for (int i = 0; i < playerCount; i++) {
            clientPorts.add(PortSet.of(++portStart, ++portStart));
        }
        return multiplayerSetup().sharedPort(sharedPort).serverPort(serverPort).clientPorts(clientPorts).build();
    }

    public int getSharedPort() {
        return sharedPort;
    }

    public PortSet getServerPort() {
        return serverPort;
    }

    public Set<PortSet> getClientPorts() {
        return new HashSet<>(clientPorts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MultiplayerOptions that = (MultiplayerOptions) o;

        return sharedPort == that.sharedPort &&
                serverPort.equals(that.serverPort) &&
                clientPorts.equals(that.clientPorts);
    }

    @Override
    public int hashCode() {
        int result = sharedPort;
        result = 31 * result + serverPort.hashCode();
        result = 31 * result + clientPorts.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
