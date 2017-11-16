package com.github.ocraft.s2client.api.controller;

import com.github.ocraft.s2client.protocol.GameVersion;
import com.github.ocraft.s2client.protocol.Versions;
import com.github.ocraft.s2client.protocol.game.GameStatus;
import com.github.ocraft.s2client.protocol.response.Response;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.reactivex.subscribers.DefaultSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.github.ocraft.s2client.api.OcraftConfig.*;
import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static java.lang.String.format;
import static java.util.Comparator.reverseOrder;
import static java.util.Optional.ofNullable;


public class S2Controller extends DefaultSubscriber<Response> {

    private static Logger log = LoggerFactory.getLogger(S2Controller.class);

    private static final int CONNECTION_TIMEOUT_IN_MILLIS = cfg().getInt(GAME_NET_TIMEOUT);
    private static final int MAX_CONNECTION_TRY_COUNT = cfg().getInt(GAME_NET_RETRY_COUNT);
    private static final String BUILD_PREFIX = "Base";

    private Config cfg;

    private Process s2Process;
    private int tryCount;
    private GameStatus lastGameStatus;

    private S2Controller(Config config) {
        this.cfg = config;
    }

    public static class Builder {

        private static final String EXECUTE_INFO = "ExecuteInfo.txt";
        private static final Path CFG_PATH = Paths.get("Starcraft II", EXECUTE_INFO);
        private static final Path WIN_CFG = Paths.get("Documents").resolve(CFG_PATH);
        private static final Path LINUX_CFG = CFG_PATH;
        private static final Path MAC_CFG = Paths.get("Library", "Application Support", "Blizzard").resolve(CFG_PATH);
        private static final String VERSIONS_DIR = "Versions";
        private static final String X64_SUFFIX = "_x64";

        private static int instanceCount = 0;

        private Map<String, Object> builderConfig = new HashMap<>();

        private Builder() {
            Config referenceConfig = cfg();
            preparePort();
            prepareWindowPosition(referenceConfig);
            instanceCount++;
        }

        private void preparePort() {
            builderConfig.put(GAME_NET_PORT, PortSetup.fetchPort());
        }

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
            builderConfig.put(GAME_EXE_PATH, executablePath.toString());
            return this;
        }

        public Builder withListenIp(String gameListenIp) {
            builderConfig.put(GAME_NET_IP, gameListenIp);
            return this;
        }

        public Builder withPort(int gameListenPort) {
            builderConfig.put(GAME_NET_PORT, gameListenPort);
            return this;
        }

        public Builder withWindowSize(int w, int h) {
            builderConfig.put(GAME_WINDOW_W, w);
            builderConfig.put(GAME_WINDOW_H, h);
            return this;
        }

        public Builder withWindowPosition(int x, int y) {
            builderConfig.put(GAME_WINDOW_X, x);
            builderConfig.put(GAME_WINDOW_Y, y);
            return this;
        }

        public Builder withDataVersion(String dataVersion) {
            builderConfig.put(GAME_EXE_DATA_VER, dataVersion);
            return this;
        }

        public S2Controller launch() {
            return new S2Controller(getGameConfiguration()).launch();
        }

        Config getGameConfiguration() {

            Config referenceConfig = cfg();

            Config customConfig = ConfigFactory.parseMap(builderConfig).withFallback(referenceConfig);
            Map<String, Object> executableConfig = new HashMap<>();


            if (!customConfig.hasPath(GAME_EXE_PATH)) {
                executableConfig.put(GAME_EXE_PATH, findExecutablePath().toString());
            } else {
                executableConfig.put(GAME_EXE_PATH, customConfig.getString(GAME_EXE_PATH));
            }

            Path executablePath = Paths.get((String) executableConfig.get(GAME_EXE_PATH));
            String baseBuild = toNewestBaseBuild().apply(executablePath.resolve(VERSIONS_DIR));
            Path buildPath = executablePath.resolve(Paths.get(VERSIONS_DIR, baseBuild));
            String exeFile = toNewestExeFile().apply(buildPath);

            executableConfig.put(GAME_EXE_BUILD, baseBuild);
            executableConfig.put(GAME_EXE_FILE, exeFile);
            executableConfig.put(GAME_EXE_IS_64, exeFile.contains(X64_SUFFIX));

            if (!customConfig.hasPath(GAME_EXE_DATA_VER)) {
                Optional<GameVersion> gameVersion = Versions.versionFor(
                        Integer.parseInt(baseBuild.replaceFirst(BUILD_PREFIX, "")));
                gameVersion.ifPresent(ver -> executableConfig.put(GAME_EXE_DATA_VER, ver.getDataHash()));
            }

            Config gameConfig = ConfigFactory.parseMap(executableConfig).withFallback(customConfig);
            gameConfig.checkValid(referenceConfig);

            return gameConfig;
        }

        private Path findExecutablePath() {
            Path executeInfoPath = resolveExecuteInfoPath().filter(Files::exists).orElseThrow(required(EXECUTE_INFO));
            try (Stream<String> lines = Files.lines(executeInfoPath)) {
                return lines.findFirst()
                        .map(splitToKeyValue())
                        .filter(correctProperty())
                        .filter(notEmptyValue())
                        .map(toPropertyValue())
                        .map(toGameRootPath())
                        .filter(Files::exists)
                        .orElseThrow(required("executable path"));
            } catch (IOException e) {
                log.debug("Finding executable path error.", e);
                throw new StarCraft2LaunchException("Finding executable path error.", e);
            }
        }

        private Optional<Path> resolveExecuteInfoPath() {
            String os = System.getProperty("os.name").toLowerCase();
            String userHome = System.getProperty("user.home");
            if (isWindows(os)) {
                return Optional.of(Paths.get(userHome).resolve(WIN_CFG));
            } else if (isUnix(os)) {
                return Optional.of(Paths.get(userHome).resolve(LINUX_CFG));
            } else if (isMac(os)) {
                return Optional.of(Paths.get(userHome).resolve(MAC_CFG));
            } else {
                return Optional.empty();
            }
        }

        private boolean isWindows(String os) {
            return os.contains("win");
        }

        private boolean isMac(String os) {
            return os.contains("mac");
        }

        private boolean isUnix(String os) {
            return os.contains("nix") || os.contains("nux") || os.contains("aix");
        }

        private Function<String, String[]> splitToKeyValue() {
            return property -> property.split("=");
        }

        private Predicate<String[]> correctProperty() {
            return property -> property.length == 2;
        }

        private Predicate<String[]> notEmptyValue() {
            return property -> property[1] != nothing() && !property[1].isEmpty();
        }

        private Function<String[], String> toPropertyValue() {
            return property -> property[1].trim();
        }

        private Function<String, Path> toGameRootPath() {
            return exePath -> Paths.get(exePath).resolve(Paths.get("..", "..", "..")).normalize();
        }

        private Function<Path, String> toNewestBaseBuild() {
            return versionPath -> {
                try (Stream<Path> builds = Files.list(
                        ofNullable(versionPath).filter(Files::exists).orElseThrow(required("version directory")))) {
                    return builds.sorted(reverseOrder()).findFirst()
                            .map(Path::getFileName)
                            .map(Path::toString)
                            .orElseThrow(required("base build"));
                } catch (IOException e) {
                    log.debug("Newest base build not found.", e);
                    throw new StarCraft2LaunchException("Newest base build not found.", e);
                }
            };
        }

        private Function<Path, String> toNewestExeFile() {
            return path -> {
                try (Stream<Path> exes = Files.list(path)) {
                    return exes.sorted(reverseOrder()).findFirst()
                            .map(Path::getFileName)
                            .map(Path::toString)
                            .orElseThrow(required("exe file name"));
                } catch (IOException e) {
                    log.debug("Newest exe file not found.", e);
                    throw new StarCraft2LaunchException("Newest exe file not found.", e);
                }
            };
        }

        static void reset() {
            PortSetup.reset();
            instanceCount = 0;
        }

    }

    public static Builder starcraft2Game() {
        return new Builder();
    }

    public static int lastPort() {
        return PortSetup.getLastPort();
    }

    private S2Controller launch() {
        log.info("Launching Starcraft II with configuration: {}.", cfg);
        try {
            Path executablePath = Paths.get(cfg.getString(GAME_EXE_PATH));
            String exeFile = executablePath
                    .resolve(Paths.get(
                            Builder.VERSIONS_DIR,
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
                    .directory(executablePath.resolve(getSupportDirPath()).toFile())
                    .start();

            return this;
        } catch (IOException e) {
            log.error("StarCraft 2 launching process error.", e);
            throw new StarCraft2LaunchException("StarCraft 2 launching process error.", e);
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
            throw new StarCraft2LaunchException("Game is unreachable.");
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

    public void stopAndWait() {
        try {
            if (isSet(s2Process)) {
                stop();
                s2Process.waitFor();
            }
        } catch (InterruptedException e) {
            log.debug("Thread was interrupted.", e);
            Thread.currentThread().interrupt();
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
        String baseBuildName = BUILD_PREFIX + baseBuild;
        if (!currentBaseBuild.equals(baseBuildName) || !currentDataVersion.equals(dataVersion)) {

            log.warn("Expected base build: {} and data version: {}. " +
                            "Actual base build: {} and data version: {}. " +
                            "Relaunching to expected version...",
                    baseBuildName, dataVersion, currentBaseBuild, currentDataVersion);

            stopAndWait();

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


}
