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

import com.github.ocraft.s2client.api.OcraftApiConfig;
import com.github.ocraft.s2client.test.TemporaryFolder;
import com.github.ocraft.s2client.test.TemporaryFolderExtension;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static com.github.ocraft.s2client.api.Fixtures.*;
import static com.github.ocraft.s2client.api.OcraftApiConfig.*;
import static com.github.ocraft.s2client.api.controller.S2Controller.starcraft2Game;
import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static java.util.Collections.singletonList;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(TemporaryFolderExtension.class)
class S2ControllerTest {

    private TemporaryFolder userHome;

    @BeforeEach
    void setUp() {
        System.setProperty("user.home", userHome.getRootFolder().toAbsolutePath().toString());
        S2Controller.Builder.reset();
    }

    @Test
    void preparesGameConfigurationIfNotProvidedOnWindows() throws IOException {
        assertThatConfigurationIsExtractedOnWindows();
    }

    private void assertThatConfigurationIsExtractedOnWindows() throws IOException {
        asWindows();
        initStarcraft2Executable(WIN_USER_DIR);

        assertThatGameConfigurationIsCorrect(starcraft2Game().getGameConfiguration());
    }

    private void asWindows() {
        System.setProperty("os.name", "WINDOWS");
    }

    private void assertThatGameConfigurationIsCorrect(Config cfg) {
        assertThat(cfg.getConfig(OcraftApiConfig.GAME)).as("game cfg")
                .isEqualTo(expectedConfiguration().getConfig(OcraftApiConfig.GAME));
    }

    private Config expectedConfiguration() {
        Path gameRoot = gameRoot();
        String exePath = gameRoot.resolve(Paths.get("Versions", CFG_EXE_BUILD_OLD, CFG_EXE_FILE)).toString();
        return ConfigFactory.parseMap(Map.ofEntries(
                entry(GAME_NET_IP, CFG_NET_IP),
                entry(GAME_NET_PORT, CFG_NET_PORT),
                entry(GAME_NET_TIMEOUT, 2000),
                entry(GAME_NET_RETRY_COUNT, 10),
                entry(GAME_EXE_ROOT, gameRoot.toString()),
                entry(GAME_EXE_PATH, exePath),
                entry(GAME_EXE_BUILD, CFG_EXE_BUILD_NEW),
                entry(GAME_EXE_FILE, CFG_EXE_FILE),
                entry(GAME_EXE_DATA_VER, CFG_EXE_DATA_VER),
                entry(GAME_EXE_IS_64, true),
                entry(GAME_WINDOW_W, CFG_WINDOW_W),
                entry(GAME_WINDOW_H, CFG_WINDOW_H),
                entry(GAME_WINDOW_X, CFG_WINDOW_X),
                entry(GAME_WINDOW_Y, CFG_WINDOW_Y),
                entry(GAME_WINDOW_MODE, 0),
                entry(GAME_CLI_NEEDS_SUPPORT_DIR, true)
        )).withValue(GAME_CLI_DATA_DIR, ConfigValueFactory.fromAnyRef(nothing()))
                .withValue(GAME_CLI_EGL_PATH, ConfigValueFactory.fromAnyRef(nothing()))
                .withValue(GAME_CLI_OS_MESA_PATH, ConfigValueFactory.fromAnyRef(nothing()))
                .withValue(GAME_CLI_DATA_DIR, ConfigValueFactory.fromAnyRef(nothing()))
                .withValue(GAME_CLI_TEMP_DIR, ConfigValueFactory.fromAnyRef(nothing()))
                .withValue(GAME_CLI_VERBOSE, ConfigValueFactory.fromAnyRef(nothing()));
    }

    private Path gameRoot() {
        return userHome.getRootFolder().resolve(GAME_ROOT);
    }

    private void initStarcraft2Executable(Path userDir) throws IOException {
        Path versionRoot = gameRoot().resolve(Paths.get("Versions"));
        createExecuteInfo(userDir);
        userHome.newFile(versionRoot.resolve(Paths.get(CFG_EXE_BUILD_OLD)), CFG_EXE_FILE);
        userHome.newFile(versionRoot.resolve(Paths.get(CFG_EXE_BUILD_NEW)), CFG_EXE_FILE);
    }

    private void createExecuteInfo(Path userDir) throws IOException {
        Path exeFilePath = Paths.get("Versions", CFG_EXE_BUILD_OLD, CFG_EXE_FILE);
        Files.write(
                userHome.newFile(userDir.resolve(GAME_DIR), EXECUTE_INFO_TXT),
                singletonList("executable = " + gameRoot().resolve(exeFilePath)));
    }

    @Test
    void preparesGameConfigurationIfNotProvidedOnLinux() throws IOException {
        assertThatConfigurationIsExtractedOnLinux();
    }

    private void assertThatConfigurationIsExtractedOnLinux() throws IOException {
        asLinux();
        initStarcraft2Executable(LINUX_USER_DIR);

        assertThatGameConfigurationIsCorrect(starcraft2Game().getGameConfiguration());
    }

    private void asLinux() {
        System.setProperty("os.name", "LINUX");
    }

    @Test
    void preparesGameConfigurationIfNotProvidedOnOsX() throws IOException {
        assertThatConfigurationIsExtractedOnOsX();
    }

    private void assertThatConfigurationIsExtractedOnOsX() throws IOException {
        asMac();
        initStarcraft2ExecutableOnMacOs(OSX_USER_DIR);

        assertThat(starcraft2Game().getGameConfiguration().getConfig(OcraftApiConfig.GAME)).as("MacOs game cfg")
                .isEqualTo(expectedMacOsConfiguration());
    }

    private void asMac() {
        System.setProperty("os.name", "MAC");
    }

    private void initStarcraft2ExecutableOnMacOs(Path userDir) throws IOException {
        Path versionRoot = gameRoot().resolve(Paths.get("Versions"));
        createExecuteInfoOnMacOs(userDir);
        userHome.newFile(
                versionRoot.resolve(Paths.get(CFG_EXE_BUILD_OLD, "SC2.app", "Contents", "MacOS")), CFG_EXE_FILE);
        userHome.newFile(
                versionRoot.resolve(Paths.get(CFG_EXE_BUILD_NEW, "SC2.app", "Contents", "MacOS")), CFG_EXE_FILE);
    }

    private void createExecuteInfoOnMacOs(Path userDir) throws IOException {
        Path exeFilePath = Paths.get("Versions", CFG_EXE_BUILD_OLD, "SC2.app", "Contents", "MacOS", CFG_EXE_FILE);
        Files.write(
                userHome.newFile(userDir.resolve(GAME_DIR), EXECUTE_INFO_TXT),
                singletonList("executable = " + gameRoot().resolve(exeFilePath)));
    }

    private Config expectedMacOsConfiguration() {
        Path exeFile = Paths.get("SC2.app", "Contents", "MacOS", CFG_EXE_FILE);
        return ConfigFactory.parseMap(Map.of(
                GAME_EXE_FILE, exeFile.toString(),
                GAME_EXE_PATH, gameRoot().resolve(Paths.get("Versions", CFG_EXE_BUILD_OLD).resolve(exeFile)).toString()
        )).withFallback(expectedConfiguration()).getConfig(OcraftApiConfig.GAME);
    }

    @Test
    void throwsExceptionWhenExecuteInfoIsNotFound() {
        assertThatExceptionOfType(StarCraft2ControllerException.class)
                .isThrownBy(() -> starcraft2Game().getGameConfiguration())
                .withMessage("Invalid argument was provided")
                .withCause(new IllegalArgumentException("ExecuteInfo.txt is required"));
    }

    @Test
    void throwsExceptionWhenExecutablePathDoesNotExist() {
        asWindows();
        userHome.newFile(WIN_USER_DIR.resolve("Starcraft II"), EXECUTE_INFO_TXT);

        assertThatExceptionOfType(StarCraft2ControllerException.class)
                .isThrownBy(() -> starcraft2Game().getGameConfiguration())
                .withMessage("Invalid argument was provided")
                .withCause(new IllegalArgumentException("executable path is required"));
    }

    @Test
    void throwsExceptionWhenExecutablePathIsTooShort() {
        asWindows();
        userHome.newFile(WIN_USER_DIR.resolve("Starcraft II"), EXECUTE_INFO_TXT);

        assertThatExceptionOfType(StarCraft2ControllerException.class)
                .isThrownBy(() -> starcraft2Game().withExecutablePath(Paths.get("sc2")).getGameConfiguration())
                .withMessage("Invalid path to the executable file: sc2");
    }

    @Test
    void allowsStartingGameFromCustomConfiguration() throws IOException {
        initStarcraft2Executable(LINUX_USER_DIR);

        Config cfg = starcraft2Game()
                .withExecutablePath(gameRoot().resolve(Paths.get("Versions", CFG_EXE_BUILD_OLD, CFG_EXE_FILE)))
                .withListenIp(CFG_NET_IP_CUSTOM)
                .withPort(CFG_NET_PORT_CUSTOM)
                .withWindowSize(CFG_WINDOW_W_CUSTOM, CFG_WINDOW_H_CUSTOM)
                .withWindowPosition(CFG_WINDOW_X_CUSTOM, CFG_WINDOW_Y_CUSTOM)
                .withDataVersion(CFG_EXE_DATA_VER_CUSTOM)
                .verbose(Boolean.valueOf(CFG_VERBOSE_CUSTOM))
                .withEglPath(CFG_EGL_PATH_CUSTOM)
                .withOsMesaPath(CFG_OS_MESA_PATH_CUSTOM)
                .withTmpDir(CFG_TMP_DIR_PATH_CUSTOM)
                .withDataDir(CFG_DATA_DIR_PATH_CUSTOM)
                .getGameConfiguration();

        assertThat(cfg.getConfig(OcraftApiConfig.GAME)).as("custom game cfg").isEqualTo(expectedCustomConfiguration());
    }

    private Config expectedCustomConfiguration() {
        return ConfigFactory.parseMap(Map.ofEntries(
                entry(GAME_NET_IP, CFG_NET_IP_CUSTOM),
                entry(GAME_NET_PORT, CFG_NET_PORT_CUSTOM),
                entry(GAME_WINDOW_W, CFG_WINDOW_W_CUSTOM),
                entry(GAME_WINDOW_H, CFG_WINDOW_H_CUSTOM),
                entry(GAME_WINDOW_X, CFG_WINDOW_X_CUSTOM),
                entry(GAME_WINDOW_Y, CFG_WINDOW_Y_CUSTOM),
                entry(GAME_EXE_DATA_VER, CFG_EXE_DATA_VER_CUSTOM),
                entry(GAME_CLI_VERBOSE, CFG_VERBOSE_CUSTOM),
                entry(GAME_CLI_TEMP_DIR, CFG_TMP_DIR_PATH_CUSTOM.toString()),
                entry(GAME_CLI_DATA_DIR, CFG_DATA_DIR_PATH_CUSTOM.toString()),
                entry(GAME_CLI_OS_MESA_PATH, CFG_OS_MESA_PATH_CUSTOM.toString()),
                entry(GAME_CLI_EGL_PATH, CFG_EGL_PATH_CUSTOM.toString())
        )).withFallback(expectedConfiguration()).getConfig(OcraftApiConfig.GAME);
    }

    // TODO p.picheta verify if it's really needed?
    @Test
    void managesConfigurationOfManyGameInstances() throws IOException {
        asWindows();
        initStarcraft2Executable(WIN_USER_DIR);

        Config instance01 = starcraft2Game().getGameConfiguration();
        Config instance02 = starcraft2Game().getGameConfiguration();
        Config instance03 = starcraft2Game().getGameConfiguration();
        Config instance04 = starcraft2Game().getGameConfiguration();
        Config instance05 = starcraft2Game().getGameConfiguration();

//        assertThat(instance01.getInt(GAME_NET_PORT)).as("game instance 01 port").isEqualTo(CFG_NET_PORT);
//        assertThat(instance02.getInt(GAME_NET_PORT)).as("game instance 02 port").isEqualTo(CFG_NET_PORT + 1);
//        assertThat(instance03.getInt(GAME_NET_PORT)).as("game instance 03 port").isEqualTo(CFG_NET_PORT + 2);
//        assertThat(instance04.getInt(GAME_NET_PORT)).as("game instance 04 port").isEqualTo(CFG_NET_PORT + 3);
//        assertThat(instance05.getInt(GAME_NET_PORT)).as("game instance 05 port").isEqualTo(CFG_NET_PORT + 4);

        assertThat(instance01.getInt(GAME_WINDOW_X)).as("game instance 01 window.x").isEqualTo(CFG_WINDOW_X);
        assertThat(instance01.getInt(GAME_WINDOW_Y)).as("game instance 01 window.y").isEqualTo(CFG_WINDOW_Y);

        assertThat(instance02.getInt(GAME_WINDOW_X)).as("game instance 02 window.x")
                .isEqualTo(CFG_WINDOW_X + CFG_WINDOW_W);
        assertThat(instance02.getInt(GAME_WINDOW_Y)).as("game instance 02 window.y").isEqualTo(CFG_WINDOW_Y);

        assertThat(instance03.getInt(GAME_WINDOW_X)).as("game instance 03 window.x").isEqualTo(CFG_WINDOW_X);
        assertThat(instance03.getInt(GAME_WINDOW_Y)).as("game instance 03 window.y")
                .isEqualTo(CFG_WINDOW_Y + CFG_WINDOW_H);

        assertThat(instance04.getInt(GAME_WINDOW_X)).as("game instance 04 window.x")
                .isEqualTo(CFG_WINDOW_X + CFG_WINDOW_W);
        assertThat(instance04.getInt(GAME_WINDOW_Y)).as("game instance 04 window.y")
                .isEqualTo(CFG_WINDOW_Y + CFG_WINDOW_H);

        assertThat(instance05.getInt(GAME_WINDOW_X)).as("game instance 05 window.x").isEqualTo(CFG_WINDOW_X);
        assertThat(instance05.getInt(GAME_WINDOW_Y)).as("game instance 05 window.y").isEqualTo(CFG_WINDOW_Y);
    }

}
