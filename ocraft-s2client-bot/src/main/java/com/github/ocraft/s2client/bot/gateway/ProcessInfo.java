package com.github.ocraft.s2client.bot.gateway;

/*-
 * #%L
 * ocraft-s2client-bot
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

import java.nio.file.Path;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ProcessInfo {

    private final Path processPath;
    private final Long processId;
    private final Integer port;

    private ProcessInfo(Path processPath, Long processId, Integer port) {
        require("port", port);
        this.processPath = processPath;
        this.processId = processId;
        this.port = port;
    }

    public static ProcessInfo from(Path processPath, Long processId, Integer port) {
        return new ProcessInfo(processPath, processId, port);
    }

    public Optional<Path> getProcessPath() {
        return Optional.ofNullable(processPath);
    }

    public Optional<Long> getProcessId() {
        return Optional.ofNullable(processId);
    }

    public Integer getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProcessInfo that = (ProcessInfo) o;

        if (processPath != null ? !processPath.equals(that.processPath) : that.processPath != null) return false;
        if (processId != null ? !processId.equals(that.processId) : that.processId != null) return false;
        return port != null ? port.equals(that.port) : that.port == null;
    }

    @Override
    public int hashCode() {
        int result = processPath != null ? processPath.hashCode() : 0;
        result = 31 * result + (processId != null ? processId.hashCode() : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProcessInfo{" +
                "processPath=" + processPath +
                ", processId=" + processId +
                ", port=" + port +
                '}';
    }
}
