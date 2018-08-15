package com.github.ocraft.s2client.api.controller;

/*-
 * #%L
 * ocraft-s2client-api
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

public final class PortSetup {
    private int portCounter = 0;
    private int lastPort;
    private int portStart;

    private PortSetup(int portStart) {
        reset();
        this.portStart = portStart;
    }

    public static synchronized PortSetup init(int portStart) {
        return new PortSetup(portStart);
    }

    public synchronized int fetchPort() {
        lastPort = portStart + portCounter++;
        return lastPort;
    }

    public synchronized void reset() {
        portCounter = 0;
    }

    public int lastPort() {
        return lastPort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PortSetup portSetup = (PortSetup) o;

        if (portCounter != portSetup.portCounter) return false;
        if (lastPort != portSetup.lastPort) return false;
        return portStart == portSetup.portStart;
    }

    @Override
    public int hashCode() {
        int result = portCounter;
        result = 31 * result + lastPort;
        result = 31 * result + portStart;
        return result;
    }

    @Override
    public String toString() {
        return "PortSetup{" +
                "portCounter=" + portCounter +
                ", lastPort=" + lastPort +
                ", portStart=" + portStart +
                '}';
    }
}
