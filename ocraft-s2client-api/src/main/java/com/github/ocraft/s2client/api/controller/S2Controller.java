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

import com.github.ocraft.s2client.protocol.game.GameStatus;
import com.github.ocraft.s2client.protocol.response.Response;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.reactivex.subscribers.DefaultSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.github.ocraft.s2client.api.OcraftApiConfig.*;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static java.lang.String.format;

public class S2Controller extends DefaultSubscriber<Response> {

    private static Logger log = LoggerFactory.getLogger(S2Controller.class);

    private static final int CONNECTION_TIMEOUT_IN_MILLIS = cfg().getInt(GAME_NET_TIMEOUT);
    private static final int MAX_CONNECTION_TRY_COUNT = cfg().getInt(GAME_NET_RETRY_COUNT);
    private static final int STOP_PROCESS_TIMEOUT_IN_MILLIS = 4000;

    private Config cfg;

    private Process s2Process;
    private int tryCount;
    private GameStatus lastGameStatus;

    private S2Controller(Config config) {
        this.cfg = config;
    }

    public static class Builder {

        private static int instanceCount = 0;

        private Map<String, Object> builderConfig = new HashMap<>();

        private Builder() {
            Config referenceConfig = cfg();
            prepareWindowPosition(referenceConfig);
            instanceCount++;
        }

        // TODO p.picheta move this outside?
        private void prepareWindowPosition(Config referenceConfig) {
            int w = referenceConfig.getInt(GAME_WINDOW_W);
            int h = referenceConfig.getInt(GAME_WINDOW_H);
            int x = referenceConfig.getInt(GAME_WINDOW_X);
            int y = referenceConfig.getInt(GAME_WINDOW_Y);

            int columnIsEven = instanceCount % 2;
            int rowIsEven = (int) Math.floor(instanceCount / 2.0) % 2;

            builderConfig.put(GAME_WINDOW_X, x + w * columnIsEven);
            builderConfig.put(GAME_WINDOW_Y, y + h * rowIsEven);
        }

        public Builder withExecutablePath(Path executablePath) {
            if (isSet(executablePath)) {
                builderConfig.put(GAME_EXE_PATH, executablePath.toString());
            }
            return this;
        }

        public Builder withListenIp(String gameListenIp) {
            if (isSet(gameListenIp)) builderConfig.put(GAME_NET_IP, gameListenIp);
            return this;
        }

        public Builder withPort(Integer gameListenPort) {
            if (isSet(gameListenPort)) builderConfig.put(GAME_NET_PORT, gameListenPort);
            return this;
        }

        public Builder withWindowSize(Integer w, Integer h) {
            if (isSet(w)) builderConfig.put(GAME_WINDOW_W, w);
            if (isSet(h)) builderConfig.put(GAME_WINDOW_H, h);
            return this;
        }

        public Builder withWindowPosition(Integer x, Integer y) {
            if (isSet(x)) builderConfig.put(GAME_WINDOW_X, x);
            if (isSet(y)) builderConfig.put(GAME_WINDOW_Y, y);
            return this;
        }

        public Builder withDataVersion(String dataVersion) {
            if (isSet(dataVersion)) builderConfig.put(GAME_EXE_DATA_VER, dataVersion);
            return this;
        }

        public Builder withBaseBuild(Integer baseBuild) {
            // TODO p.picheta to test
            if (isSet(baseBuild)) builderConfig.put(GAME_EXE_BUILD, ExecutableParser.BUILD_PREFIX + baseBuild);
            return this;
        }

        public Builder verbose(Boolean value) {
            if (isSet(value)) builderConfig.put(GAME_CLI_VERBOSE, String.valueOf(value));
            return this;
        }

        public Builder needsSupportDir(Boolean value) {
            if (isSet(value)) builderConfig.put(GAME_CLI_NEEDS_SUPPORT_DIR, String.valueOf(value));
            return this;
        }

        public Builder withTmpDir(Path tmpDir) {
            if (isSet(tmpDir)) builderConfig.put(GAME_CLI_TEMP_DIR, tmpDir.toString());
            return this;
        }

        public Builder withDataDir(Path dataDir) {
            if (isSet(dataDir)) builderConfig.put(GAME_CLI_DATA_DIR, dataDir.toString());
            return this;
        }

        public Builder withOsMesaPath(Path osMesaPath) {
            if (isSet(osMesaPath)) builderConfig.put(GAME_CLI_OS_MESA_PATH, osMesaPath.toString());
            return this;
        }

        public Builder withEglPath(Path eglPath) {
            if (isSet(eglPath)) builderConfig.put(GAME_CLI_EGL_PATH, eglPath.toString());
            return this;
        }

        public S2Controller launch() {
            return new S2Controller(getGameConfiguration()).launch();
        }

        Config getGameConfiguration() {

            Config referenceConfig = cfg();

            Config customConfig = builderConfig();
            String customPath = customConfig.hasPath(GAME_EXE_PATH) ? customConfig.getString(GAME_EXE_PATH) : null;
            String customDataVersion = customConfig.hasPath(GAME_EXE_DATA_VER)
                    ? customConfig.getString(GAME_EXE_DATA_VER)
                    : null;
            String customBaseBuild = customConfig.hasPath(GAME_EXE_BUILD)
                    ? customConfig.getString(GAME_EXE_BUILD)
                    : null;

            Map<String, Object> executableConfig = ExecutableParser.loadSettings(
                    customPath, customDataVersion, customBaseBuild);

            Config gameConfig = ConfigFactory.parseMap(executableConfig).withFallback(customConfig);
            gameConfig.checkValid(referenceConfig);

            return gameConfig;
        }

        public Config builderConfig() {
            return ConfigFactory.parseMap(builderConfig).withFallback(cfg());
        }

        static void reset() {
            instanceCount = 0;
        }

    }

    public static Builder starcraft2Game() {
        return new Builder();
    }

    private S2Controller launch() {
        log.info("Launching Starcraft II with configuration: {}.", cfg);
        try {
            Path gameRoot = Paths.get(cfg.getString(GAME_EXE_ROOT));
            String exeFile = gameRoot
                    .resolve(Paths.get(
                            ExecutableParser.VERSIONS_DIR,
                            cfg.getString(GAME_EXE_BUILD),
                            cfg.getString(GAME_EXE_FILE)))
                    .toString();

            List<String> args = new ArrayList<>();
            args.add(exeFile);
            command(args, "-listen", GAME_NET_IP);
            command(args, "-port", GAME_NET_PORT);
            command(args, "-displayMode", GAME_WINDOW_MODE);
            command(args, "-dataVersion", GAME_EXE_DATA_VER);
            command(args, "-windowwidth", GAME_WINDOW_W);
            command(args, "-windowheight", GAME_WINDOW_H);
            command(args, "-windowx", GAME_WINDOW_X);
            command(args, "-windowy", GAME_WINDOW_Y);
            command(args, "-verbose", GAME_CLI_VERBOSE);
            command(args, "-tempDir", GAME_CLI_TEMP_DIR);
            command(args, "-dataDir", GAME_CLI_DATA_DIR);
            command(args, "-osmesapath", GAME_CLI_OS_MESA_PATH);
            command(args, "-eglpath", GAME_CLI_EGL_PATH);

            s2Process = new ProcessBuilder(args)
                    .redirectErrorStream(true)
                    .directory(cfg.hasPath(GAME_CLI_NEEDS_SUPPORT_DIR) && cfg.getBoolean(GAME_CLI_NEEDS_SUPPORT_DIR)
                            ? gameRoot.resolve(getSupportDirPath()).toFile() : null)
                    .start();

            log.info("Launched SC2 ({}), PID: {}", exeFile, s2Process.pid());

            return this;
        } catch (IOException e) {
            log.error("StarCraft 2 launching process error.", e);
            throw new StarCraft2ControllerException(
                    ControllerError.PROCESS_START_FAILED, "StarCraft 2 launching process error.", e);
        }
    }

    private void command(List<String> args, String name, String value) {
        if (cfg.hasPath(value)) {
            args.add(name);
            args.add(cfg.getString(value));
        }
    }

    private Path getSupportDirPath() {
        return Paths.get(cfg.getBoolean(GAME_EXE_IS_64) ? "Support64" : "Support");
    }

    public S2Controller untilReady() {
        reachTheGame(new InetSocketAddress(cfg.getString(GAME_NET_IP), cfg.getInt(GAME_NET_PORT)));
        return this;
    }

    private void reachTheGame(InetSocketAddress endpoint) {
        if (!processIsAlive()) {
            throw new IllegalStateException("game is not running");
        }
        try (Socket socket = new Socket()) {
            socket.connect(endpoint, CONNECTION_TIMEOUT_IN_MILLIS);
            tryCount = 0;
        } catch (IOException e) {
            tryReachAgain(endpoint);
        }
    }

    private void tryReachAgain(InetSocketAddress endpoint) {
        if (tryCount > MAX_CONNECTION_TRY_COUNT) {
            log.error("Game process in unreachable.");
            throw new StarCraft2ControllerException(ControllerError.GAME_UNREACHABLE, "Game is unreachable.");
        } else {
            tryCount++;
            reachTheGame(endpoint);
        }
    }

    private boolean processIsAlive() {
        return isSet(s2Process) && s2Process.isAlive();
    }

    public void stop() {
        if (isSet(s2Process)) {
            s2Process.destroy();
        }
    }

    public boolean stopAndWait() {
        return stopAndWait(STOP_PROCESS_TIMEOUT_IN_MILLIS);
    }

    public boolean stopAndWait(long timeoutInMillis) {
        try {
            if (isSet(s2Process)) {
                stop();
                BufferedReader reader = new BufferedReader(new InputStreamReader(s2Process.getInputStream()));
                reader.lines().forEach(log::debug);

                return s2Process.waitFor(timeoutInMillis, TimeUnit.MILLISECONDS);
            }
            return true;
        } catch (InterruptedException e) {
            log.debug("Thread was interrupted.", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public void isInState(GameStatus expectedStatus) {
        if (!expectedStatus.equals(lastGameStatus)) {
            throw new AssertionError(
                    format("Game status: expected [%s] but was [%s].", expectedStatus, lastGameStatus));
        }
    }

    public boolean inState(GameStatus expectedStatus) {
        return expectedStatus.equals(lastGameStatus);
    }

    public Config getConfig() {
        return cfg;
    }

    @Override
    public void onNext(Response response) {
        lastGameStatus = response.getStatus();
    }

    @Override
    public void onComplete() {
        // Nothing to do here, client stops the game if needed.
    }

    @Override
    public void onError(Throwable e) {
        log.error("S2Controller.onError", e);
    }

    public S2Controller relaunchIfNeeded(int baseBuild, String dataVersion) {
        String currentBaseBuild = cfg.getString(GAME_EXE_BUILD);
        String currentDataVersion = cfg.hasPath(GAME_EXE_DATA_VER) ? cfg.getString(GAME_EXE_DATA_VER) : "";
        String baseBuildName = ExecutableParser.BUILD_PREFIX + baseBuild;
        if (!currentBaseBuild.equals(baseBuildName) || !currentDataVersion.equals(dataVersion)) {

            log.warn("Expected base build: {} and data version: {}. " +
                            "Actual base build: {} and data version: {}. " +
                            "Relaunching to expected version...",
                    baseBuildName, dataVersion, currentBaseBuild, currentDataVersion);

            if (!stopAndWait()) {
                throw new IllegalStateException("Failed to stop previous game instance.");
            }

            cfg = ConfigFactory.parseMap(
                    Map.of(GAME_EXE_BUILD, baseBuildName, GAME_EXE_DATA_VER, dataVersion)
            ).withFallback(cfg);

            return launch();
        }
        return this;
    }

    public void restart() {
        stopAndWait();
        launch();
    }

    public Process getS2Process() {
        return s2Process;
    }
}
