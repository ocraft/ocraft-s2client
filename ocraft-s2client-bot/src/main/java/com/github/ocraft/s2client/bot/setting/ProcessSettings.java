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

import com.github.ocraft.s2client.api.controller.PortSetup;
import com.github.ocraft.s2client.bot.OcraftBotConfig;

import java.nio.file.Path;
import java.util.Objects;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

public class ProcessSettings {

    private String ip;
    private Integer port;
    private Integer portStart;
    private Boolean multithreaded = OcraftBotConfig.cfg().getBoolean(OcraftBotConfig.BOT_MULTITHREADED);
    private Boolean realtime = OcraftBotConfig.cfg().getBoolean(OcraftBotConfig.BOT_REALTIME);
    private Integer stepSize = OcraftBotConfig.cfg().getInt(OcraftBotConfig.BOT_STEP_SIZE);
    private Path processPath;
    private String dataVersion;
    private Integer requestTimeoutInMillis;
    private Integer connectionTimeoutInMillis;
    private Integer windowWidth;
    private Integer windowHeight;
    private Integer windowX;
    private Integer windowY;
    private Boolean withGameController;
    private Boolean verbose;
    private Boolean needsSupportDir;
    private Path tmpDir;
    private Path dataDir;
    private Path osMesaPath;
    private Path eglPath;
    private PortSetup portSetup;
    private Path rootPath;
    private Path actualProcessPath;
    private Integer baseBuild;
    private Boolean traced = OcraftBotConfig.cfg().getBoolean(OcraftBotConfig.BOT_TRACED);
    private boolean ladderGame;

    public ProcessSettings setConnection(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
        if (!isSet(portStart)) {
            setPortStart(port);
        }
        return this;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public ProcessSettings setMultithreaded(Boolean value) {
        this.multithreaded = value;
        return this;
    }

    public Boolean getMultithreaded() {
        return multithreaded;
    }

    public ProcessSettings setRealtime(Boolean value) {
        this.realtime = value;
        return this;
    }

    public Boolean getRealtime() {
        return realtime;
    }

    public ProcessSettings setStepSize(Integer stepSize) {
        this.stepSize = stepSize;
        return this;
    }

    public Integer getStepSize() {
        return stepSize;
    }

    public ProcessSettings setProcessPath(Path path) {
        this.processPath = path;
        return this;
    }

    public Path getProcessPath() {
        return processPath;
    }

    public ProcessSettings setDataVersion(String version) {
        this.dataVersion = version;
        return this;
    }

    public String getDataVersion() {
        return dataVersion;
    }

    public ProcessSettings setRequestTimeoutMS(Integer timeoutInMillis) {
        this.requestTimeoutInMillis = timeoutInMillis;
        return this;
    }

    public Integer getRequestTimeoutMS() {
        return requestTimeoutInMillis;
    }

    public Integer getConnectionTimeoutMS() {
        return connectionTimeoutInMillis;
    }

    public ProcessSettings setConnectionTimeoutMS(Integer timeoutInMillis) {
        this.connectionTimeoutInMillis = timeoutInMillis;
        return this;
    }

    public ProcessSettings setPortStart(Integer portStart) {
        portSetup = PortSetup.init(portStart);
        this.portStart = portStart;
        return this;
    }

    public Integer getPortStart() {
        return portStart;
    }

    public PortSetup getPortSetup() {
        return portSetup;
    }

    public ProcessSettings setWindowSize(Integer width, Integer height) {
        this.windowWidth = width;
        this.windowHeight = height;
        return this;
    }

    public Integer getWindowWidth() {
        return windowWidth;
    }

    public Integer getWindowHeight() {
        return windowHeight;
    }

    public ProcessSettings setWindowLocation(Integer x, Integer y) {
        this.windowX = x;
        this.windowY = y;
        return this;
    }

    public Integer getWindowX() {
        return windowX;
    }

    public Integer getWindowY() {
        return windowY;
    }

    public ProcessSettings setWithGameController(Boolean withGameController) {
        this.withGameController = withGameController;
        return this;
    }

    public boolean withGameController() {
        return withGameController != null ? withGameController : false;
    }

    public ProcessSettings setVerbose(Boolean value) {
        this.verbose = true;
        return this;
    }

    public Boolean getVerbose() {
        return verbose;
    }

    public ProcessSettings setNeedsSupportDir(Boolean needsSupportDir) {
        this.needsSupportDir = needsSupportDir;
        return this;
    }

    public Boolean getNeedsSupportDir() {
        return needsSupportDir;
    }

    public ProcessSettings setTmpDir(Path tmpDir) {
        this.tmpDir = tmpDir;
        return this;
    }

    public Path getTmpDir() {
        return tmpDir;
    }

    public ProcessSettings setDataDir(Path dataDir) {
        this.dataDir = dataDir;
        return this;
    }

    public Path getDataDir() {
        return dataDir;
    }

    public ProcessSettings setOsMesaPath(Path osMesaPath) {
        this.osMesaPath = osMesaPath;
        return this;
    }

    public Path getOsMesaPath() {
        return osMesaPath;
    }

    public ProcessSettings setEglPath(Path eglPath) {
        this.eglPath = eglPath;
        return this;
    }

    public Path getEglPath() {
        return eglPath;
    }

    public ProcessSettings setRootPath(Path path) {
        this.rootPath = path;
        return this;
    }

    public Path getRootPath() {
        return rootPath;
    }

    public Path getActualProcessPath() {
        return actualProcessPath;
    }

    public ProcessSettings setActualProcessPath(Path actualProcessPath) {
        this.actualProcessPath = actualProcessPath;
        return this;
    }

    public ProcessSettings setBaseBuild(int baseBuild) {
        this.baseBuild = baseBuild;
        return this;
    }

    public Integer getBaseBuild() {
        return baseBuild;
    }

    public ProcessSettings setTraced(Boolean traced) {
        this.traced = traced;
        return this;
    }

    public Boolean getTraced() {
        return traced;
    }

    public boolean isLadderGame() {
        return ladderGame;
    }

    public ProcessSettings setLadderGame(boolean ladderGame) {
        this.ladderGame = ladderGame;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProcessSettings that = (ProcessSettings) o;

        if (ladderGame != that.ladderGame) return false;
        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
        if (port != null ? !port.equals(that.port) : that.port != null) return false;
        if (portStart != null ? !portStart.equals(that.portStart) : that.portStart != null) return false;
        if (multithreaded != null ? !multithreaded.equals(that.multithreaded) : that.multithreaded != null)
            return false;
        if (realtime != null ? !realtime.equals(that.realtime) : that.realtime != null) return false;
        if (stepSize != null ? !stepSize.equals(that.stepSize) : that.stepSize != null) return false;
        if (processPath != null ? !processPath.equals(that.processPath) : that.processPath != null) return false;
        if (dataVersion != null ? !dataVersion.equals(that.dataVersion) : that.dataVersion != null) return false;
        if (requestTimeoutInMillis != null ? !requestTimeoutInMillis.equals(that.requestTimeoutInMillis) : that.requestTimeoutInMillis != null)
            return false;
        if (connectionTimeoutInMillis != null ? !connectionTimeoutInMillis.equals(that.connectionTimeoutInMillis) : that.connectionTimeoutInMillis != null)
            return false;
        if (windowWidth != null ? !windowWidth.equals(that.windowWidth) : that.windowWidth != null) return false;
        if (windowHeight != null ? !windowHeight.equals(that.windowHeight) : that.windowHeight != null) return false;
        if (windowX != null ? !windowX.equals(that.windowX) : that.windowX != null) return false;
        if (windowY != null ? !windowY.equals(that.windowY) : that.windowY != null) return false;
        if (withGameController != null ? !withGameController.equals(that.withGameController) : that.withGameController != null)
            return false;
        if (verbose != null ? !verbose.equals(that.verbose) : that.verbose != null) return false;
        if (!Objects.equals(needsSupportDir, that.needsSupportDir)) return false;
        if (tmpDir != null ? !tmpDir.equals(that.tmpDir) : that.tmpDir != null) return false;
        if (dataDir != null ? !dataDir.equals(that.dataDir) : that.dataDir != null) return false;
        if (osMesaPath != null ? !osMesaPath.equals(that.osMesaPath) : that.osMesaPath != null) return false;
        if (eglPath != null ? !eglPath.equals(that.eglPath) : that.eglPath != null) return false;
        if (portSetup != null ? !portSetup.equals(that.portSetup) : that.portSetup != null) return false;
        if (rootPath != null ? !rootPath.equals(that.rootPath) : that.rootPath != null) return false;
        if (actualProcessPath != null ? !actualProcessPath.equals(that.actualProcessPath) : that.actualProcessPath != null)
            return false;
        if (baseBuild != null ? !baseBuild.equals(that.baseBuild) : that.baseBuild != null) return false;
        return traced != null ? traced.equals(that.traced) : that.traced == null;
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (portStart != null ? portStart.hashCode() : 0);
        result = 31 * result + (multithreaded != null ? multithreaded.hashCode() : 0);
        result = 31 * result + (realtime != null ? realtime.hashCode() : 0);
        result = 31 * result + (stepSize != null ? stepSize.hashCode() : 0);
        result = 31 * result + (processPath != null ? processPath.hashCode() : 0);
        result = 31 * result + (dataVersion != null ? dataVersion.hashCode() : 0);
        result = 31 * result + (requestTimeoutInMillis != null ? requestTimeoutInMillis.hashCode() : 0);
        result = 31 * result + (connectionTimeoutInMillis != null ? connectionTimeoutInMillis.hashCode() : 0);
        result = 31 * result + (windowWidth != null ? windowWidth.hashCode() : 0);
        result = 31 * result + (windowHeight != null ? windowHeight.hashCode() : 0);
        result = 31 * result + (windowX != null ? windowX.hashCode() : 0);
        result = 31 * result + (windowY != null ? windowY.hashCode() : 0);
        result = 31 * result + (withGameController != null ? withGameController.hashCode() : 0);
        result = 31 * result + (verbose != null ? verbose.hashCode() : 0);
        result = 31 * result + (needsSupportDir != null ? needsSupportDir.hashCode() : 0);
        result = 31 * result + (tmpDir != null ? tmpDir.hashCode() : 0);
        result = 31 * result + (dataDir != null ? dataDir.hashCode() : 0);
        result = 31 * result + (osMesaPath != null ? osMesaPath.hashCode() : 0);
        result = 31 * result + (eglPath != null ? eglPath.hashCode() : 0);
        result = 31 * result + (portSetup != null ? portSetup.hashCode() : 0);
        result = 31 * result + (rootPath != null ? rootPath.hashCode() : 0);
        result = 31 * result + (actualProcessPath != null ? actualProcessPath.hashCode() : 0);
        result = 31 * result + (baseBuild != null ? baseBuild.hashCode() : 0);
        result = 31 * result + (traced != null ? traced.hashCode() : 0);
        result = 31 * result + (ladderGame ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProcessSettings{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", portStart=" + portStart +
                ", multithreaded=" + multithreaded +
                ", realtime=" + realtime +
                ", stepSize=" + stepSize +
                ", processPath=" + processPath +
                ", dataVersion='" + dataVersion + '\'' +
                ", requestTimeoutInMillis=" + requestTimeoutInMillis +
                ", connectionTimeoutInMillis=" + connectionTimeoutInMillis +
                ", windowWidth=" + windowWidth +
                ", windowHeight=" + windowHeight +
                ", windowX=" + windowX +
                ", windowY=" + windowY +
                ", withGameController=" + withGameController +
                ", verbose=" + verbose +
                ", needsSupportDir=" + needsSupportDir +
                ", tmpDir=" + tmpDir +
                ", dataDir=" + dataDir +
                ", osMesaPath=" + osMesaPath +
                ", eglPath=" + eglPath +
                ", portSetup=" + portSetup +
                ", rootPath=" + rootPath +
                ", actualProcessPath=" + actualProcessPath +
                ", baseBuild=" + baseBuild +
                ", traced=" + traced +
                ", ladderGame=" + ladderGame +
                '}';
    }
}
