package com.github.ocraft.s2client.bot.setting;

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

import picocli.CommandLine;

import java.nio.file.Path;
import java.util.Optional;

// TODO p.picheta add more fancy usage print
public final class CliSettings {

    @CommandLine.Option(names = {"-p", "--port"}, description = "The port to make StarCraft II listen on.")
    private Integer port;

    @CommandLine.Option(names = {"-r", "--realtime"}, arity = "0..1",
            description = "Whether to run StarCraft II in  real time or not.")
    private Boolean realtime;

    @CommandLine.Option(names = {"-s", "--step_size"}, description = "How many steps to take per call.")
    private Integer stepSize;

    @CommandLine.Option(names = {"-e", "--executable"}, description = "The path to StarCraft II.")
    private Path processPath;

    @CommandLine.Option(names = {"-m", "--map"}, description = "Which map to run.")
    private String mapName;

    @CommandLine.Option(names = {"-t", "--timeout"},
            description = "Timeout for how long the library will block for a response.")
    private Integer timeout;

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "Display this help message.")
    private boolean usageHelpRequested;

    public Optional<Integer> getPort() {
        return Optional.ofNullable(port);
    }

    public Optional<Boolean> getRealtime() {
        return Optional.ofNullable(realtime);
    }

    public Optional<Integer> getStepSize() {
        return Optional.ofNullable(stepSize);
    }

    public Optional<Path> getProcessPath() {
        return Optional.ofNullable(processPath);
    }

    public Optional<String> getMapName() {
        return Optional.ofNullable(mapName);
    }

    public Optional<Integer> getTimeout() {
        return Optional.ofNullable(timeout);
    }

    public boolean isUsageHelpRequested() {
        return usageHelpRequested;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CliSettings that = (CliSettings) o;

        if (usageHelpRequested != that.usageHelpRequested) return false;
        if (port != null ? !port.equals(that.port) : that.port != null) return false;
        if (realtime != null ? !realtime.equals(that.realtime) : that.realtime != null) return false;
        if (stepSize != null ? !stepSize.equals(that.stepSize) : that.stepSize != null) return false;
        if (processPath != null ? !processPath.equals(that.processPath) : that.processPath != null) return false;
        if (mapName != null ? !mapName.equals(that.mapName) : that.mapName != null) return false;
        return timeout != null ? timeout.equals(that.timeout) : that.timeout == null;
    }

    @Override
    public int hashCode() {
        int result = port != null ? port.hashCode() : 0;
        result = 31 * result + (realtime != null ? realtime.hashCode() : 0);
        result = 31 * result + (stepSize != null ? stepSize.hashCode() : 0);
        result = 31 * result + (processPath != null ? processPath.hashCode() : 0);
        result = 31 * result + (mapName != null ? mapName.hashCode() : 0);
        result = 31 * result + (timeout != null ? timeout.hashCode() : 0);
        result = 31 * result + (usageHelpRequested ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CliSettings{" +
                "port=" + port +
                ", realtime=" + realtime +
                ", stepSize=" + stepSize +
                ", processPath=" + processPath +
                ", mapName='" + mapName + '\'' +
                ", timeout=" + timeout +
                ", usageHelpRequested=" + usageHelpRequested +
                '}';
    }
}
