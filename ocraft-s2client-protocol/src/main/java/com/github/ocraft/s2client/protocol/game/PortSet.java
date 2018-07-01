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

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;

public final class PortSet implements Sc2ApiSerializable<Sc2Api.PortSet> {

    private static final long serialVersionUID = -1934268334937302438L;

    private final int gamePort;
    private final int basePort;

    private PortSet(int gamePort, int basePort) {
        this.gamePort = gamePort;
        this.basePort = basePort;
    }

    public static PortSet of(int gamePort, int basePort) {
        if (!portIsValid(gamePort) || !portIsValid(basePort))
            throw new IllegalArgumentException("port must be greater than 0");
        return new PortSet(gamePort, basePort);
    }

    private static boolean portIsValid(int portValue) {
        return portValue > 0;
    }

    @Override
    public Sc2Api.PortSet toSc2Api() {
        return Sc2Api.PortSet.newBuilder()
                .setGamePort(gamePort)
                .setBasePort(basePort)
                .build();
    }

    public int getGamePort() {
        return gamePort;
    }

    public int getBasePort() {
        return basePort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PortSet portSet = (PortSet) o;

        return gamePort == portSet.gamePort && basePort == portSet.basePort;
    }

    @Override
    public int hashCode() {
        int result = gamePort;
        result = 31 * result + basePort;
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
