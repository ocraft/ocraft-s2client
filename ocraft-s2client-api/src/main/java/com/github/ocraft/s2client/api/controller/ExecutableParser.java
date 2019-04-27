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

import com.github.ocraft.s2client.protocol.GameVersion;
import com.github.ocraft.s2client.protocol.Versions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.github.ocraft.s2client.api.OcraftApiConfig.*;
import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static java.lang.String.format;
import static java.util.Comparator.reverseOrder;
import static java.util.Optional.ofNullable;

public final class ExecutableParser {

    private static Logger log = LoggerFactory.getLogger(ExecutableParser.class);

    public static final String BUILD_PREFIX = "Base";
    public static final String VERSIONS_DIR = "Versions";
    public static final String EXECUTE_INFO = "ExecuteInfo.txt";
    public static final String X64_SUFFIX = "_x64";
    public static final Path CFG_PATH = Paths.get("Starcraft II", EXECUTE_INFO);
    public static final Path WIN_CFG = Paths.get("Documents").resolve(CFG_PATH);
    public static final Path LINUX_CFG = CFG_PATH;
    public static final Path MAC_CFG = Paths.get("Library", "Application Support", "Blizzard").resolve(CFG_PATH);

    private ExecutableParser() {
        throw new AssertionError("private constructor");
    }

    public static Map<String, Object> loadSettings(
            String customPath, String customDataVersion, String customBaseBuild) {
        try {
            Map<String, Object> executableConfig = new HashMap<>();

            if (customPath == null) {
                executableConfig.put(GAME_EXE_PATH, findExecutablePath().toString());
            } else {
                executableConfig.put(GAME_EXE_PATH, customPath);
            }

            Path executablePath = Paths.get((String) executableConfig.get(GAME_EXE_PATH));

            if (executablePath.getNameCount() < 3) {
                log.error("Invalid path to the executable file: {}", executablePath);
                throw new StarCraft2ControllerException(
                        ControllerError.INVALID_EXECUTABLE,
                        format("Invalid path to the executable file: %s", executablePath));
            }

            if (Files.notExists(executablePath)) {
                String errorMsg = format("File %s does not exist.", executablePath);
                log.error(errorMsg);
                throw new StarCraft2ControllerException(ControllerError.EXECUTABLE_NOT_FOUND, errorMsg);
            }

            Path gamePath = toGameRootPath().apply(executablePath);
            executableConfig.put(
                    GAME_EXE_FILE,
                    executablePath.subpath(gamePath.getNameCount() + 2, executablePath.getNameCount()).toString());


            executableConfig.put(GAME_EXE_ROOT, gamePath.toString());
            String exeFile = (String) executableConfig.get(GAME_EXE_FILE);

            String baseBuild = customBaseBuild;

            if (!isSet(baseBuild)) {
                baseBuild = toNewestBaseBuild(exeFile).apply(gamePath.resolve(VERSIONS_DIR));
            }

            executableConfig.put(GAME_EXE_BUILD, baseBuild);
            executableConfig.put(GAME_EXE_IS_64, exeFile.contains(X64_SUFFIX));

            if (!isSet(customDataVersion)) {
                Optional<GameVersion> gameVersion = Versions.versionFor(
                        Integer.parseInt(baseBuild.replaceFirst(BUILD_PREFIX, "")));
                gameVersion.ifPresent(ver -> executableConfig.put(GAME_EXE_DATA_VER, ver.getDataHash()));
            }

            return executableConfig;
        } catch (IllegalArgumentException e) {
            log.error("Invalid argument was provided.", e);
            throw new StarCraft2ControllerException(
                    ControllerError.INVALID_EXECUTABLE, "Invalid argument was provided", e);
        }
    }

    private static Path findExecutablePath() {
        Path executeInfoPath = resolveExecuteInfoPath().filter(Files::exists).orElseThrow(required(EXECUTE_INFO));
        try (Stream<String> lines = Files.lines(executeInfoPath)) {
            return lines.findFirst()
                    .map(splitToKeyValue())
                    .filter(correctProperty())
                    .filter(notEmptyValue())
                    .map(toPropertyValue())
                    .map(Paths::get)
                    .filter(Files::exists)
                    .orElseThrow(required("executable path"));
        } catch (IOException e) {
            log.error("Finding executable path error.", e);
            throw new StarCraft2ControllerException(
                    ControllerError.INVALID_EXECUTABLE, "Finding executable path error.", e);
        }
    }

    private static Optional<Path> resolveExecuteInfoPath() {
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

    private static boolean isWindows(String os) {
        return os.contains("win");
    }

    private static boolean isMac(String os) {
        return os.contains("mac");
    }

    private static boolean isUnix(String os) {
        return os.contains("nix") || os.contains("nux") || os.contains("aix");
    }

    private static Function<String, String[]> splitToKeyValue() {
        return property -> property.split("=");
    }

    private static Predicate<String[]> correctProperty() {
        return property -> property.length == 2;
    }

    private static Predicate<String[]> notEmptyValue() {
        return property -> property[1] != nothing() && !property[1].isEmpty();
    }

    private static Function<String[], String> toPropertyValue() {
        return property -> property[1].trim();
    }

    private static Function<Path, Path> toGameRootPath() {
        if (!isMac(System.getProperty("os.name").toLowerCase())) {
            return exePath -> exePath.resolve(Paths.get("..", "..", "..")).normalize();
        } else {
            return exePath -> exePath.resolve(Paths.get("..", "..", "..", "..", "..", "..")).normalize();
        }
    }

    private static Function<Path, String> toNewestBaseBuild(String exeFile) {
        return versionPath -> {
            try (Stream<Path> builds = Files.list(
                    ofNullable(versionPath)
                            .filter(Files::exists)
                            .filter(file -> file.getFileName().toString().startsWith(BUILD_PREFIX))
                            .orElseThrow(required("version directory")))) {
                return builds.min(reverseOrder())
                        .filter(path -> Files.exists(path.resolve(exeFile)))
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .orElseThrow(required("base build"));
            } catch (IOException e) {
                log.error("Newest base build not found.", e);
                throw new StarCraft2ControllerException(
                        ControllerError.INVALID_EXECUTABLE, "Newest base build not found.", e);
            }
        };
    }

    public static boolean findBaseExe(Path rootPath, int baseBuild, String exeFile) {
        // TODO p.picheta to test
        return Files.exists(rootPath.resolve(Paths.get(VERSIONS_DIR, BUILD_PREFIX + baseBuild, exeFile)));
    }
}
