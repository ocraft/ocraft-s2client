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

import com.github.ocraft.s2client.bot.S2Agent;
import com.github.ocraft.s2client.protocol.game.BattlenetMap;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import com.github.ocraft.s2client.protocol.game.MultiplayerOptions;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

public final class GameSettings {

    private static final String SC2_MAP_EXTENSION = ".SC2Map";

    private LocalMap localMap;
    private BattlenetMap battlenetMap;
    private List<PlayerSettings> playerSettings = new ArrayList<>();
    private MultiplayerOptions multiplayerOptions;

    public GameSettings() {
    }

    public GameSettings(String mapName) {
        resolveMap(mapName);
    }

    public GameSettings resolveMap(String mapName) {
        if (mapName == null) return this;
        if (mapName.endsWith(SC2_MAP_EXTENSION)) {
            setLocalMap(LocalMap.of(Paths.get(mapName)));
        } else {
            setBattlenetMap(BattlenetMap.of(mapName));
        }
        return this;
    }

    public GameSettings resolveMap(ProcessSettings processSettings) {
        if (isSet(localMap) && localMap.getPath().isPresent()) {
            Path localMapPath = localMap.getPath().get();
            // Absolute path
            if (Files.exists(localMapPath)) return this;
            // Relative path - Game maps directory
            if (isSet(processSettings.getRootPath())) {
                Path gameRelative = processSettings.getRootPath().resolve("maps").resolve(localMapPath);
                if (Files.exists(gameRelative)) {
                    setLocalMap(LocalMap.of(localMapPath));
                    return this;
                }
            }
            // Relative path - Library maps directory
            Optional<Path> libraryPath = libraryPath(localMapPath);
            if (libraryPath.isPresent()) {
                setLocalMap(LocalMap.of(libraryPath.get().toAbsolutePath()));
                return this;
            }

        }
        return this;
    }

    public static Optional<Path> libraryPath(Path filePath) {
        return libraryPath(filePath, Paths.get("maps"));
    }

    public static Optional<Path> libraryPath(Path filePath, Path root) {
        URL resource = GameSettings.class.getClassLoader().getResource(root.resolve(filePath).toString());
        return Optional.ofNullable(resource)
                .map(GameSettings::asUri)
                .map(Paths::get);
    }

    private static URI asUri(URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public GameSettings setLocalMap(LocalMap localMap) {
        this.localMap = localMap;
        return this;
    }

    public Optional<LocalMap> getLocalMap() {
        return Optional.ofNullable(localMap);
    }

    public GameSettings setBattlenetMap(BattlenetMap battlenetMap) {
        this.battlenetMap = battlenetMap;
        return this;
    }

    public Optional<BattlenetMap> getBattlenetMap() {
        return Optional.ofNullable(battlenetMap);
    }

    public List<PlayerSettings> getPlayerSettings() {
        return playerSettings;
    }

    public GameSettings setPlayerSettings(List<PlayerSettings> playerSettings) {
        this.playerSettings.addAll(playerSettings);
        return this;
    }

    public GameSettings setMultiplayerOptions(MultiplayerOptions multiplayerOptions) {
        this.multiplayerOptions = multiplayerOptions;
        return this;
    }

    public MultiplayerOptions getMultiplayerOptions() {
        return multiplayerOptions;
    }

    public Optional<PlayerSettings> playerSettingsFor(S2Agent agent) {
        return playerSettings.stream().filter(player -> player.getAgent().equals(agent)).findFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameSettings that = (GameSettings) o;

        if (localMap != null ? !localMap.equals(that.localMap) : that.localMap != null) return false;
        if (battlenetMap != null ? !battlenetMap.equals(that.battlenetMap) : that.battlenetMap != null)
            return false;
        if (playerSettings != null ? !playerSettings.equals(that.playerSettings) : that.playerSettings != null)
            return false;
        return multiplayerOptions != null
                ? multiplayerOptions.equals(that.multiplayerOptions)
                : that.multiplayerOptions == null;
    }

    @Override
    public int hashCode() {
        int result = localMap != null ? localMap.hashCode() : 0;
        result = 31 * result + (battlenetMap != null ? battlenetMap.hashCode() : 0);
        result = 31 * result + (playerSettings != null ? playerSettings.hashCode() : 0);
        result = 31 * result + (multiplayerOptions != null ? multiplayerOptions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GameSettings{" +
                "localMap=" + localMap +
                ", battlenetMap=" + battlenetMap +
                ", playerSettings=" + playerSettings +
                ", multiplayerOptions=" + multiplayerOptions +
                '}';
    }
}
