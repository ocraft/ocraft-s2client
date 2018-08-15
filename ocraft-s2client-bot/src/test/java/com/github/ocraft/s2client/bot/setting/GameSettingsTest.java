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

import com.github.ocraft.s2client.api.S2Client;
import com.github.ocraft.s2client.bot.Fixtures;
import com.github.ocraft.s2client.bot.S2Agent;
import com.github.ocraft.s2client.bot.S2Coordinator;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import com.github.ocraft.s2client.protocol.game.Race;
import com.github.ocraft.s2client.test.TemporaryFolder;
import com.github.ocraft.s2client.test.TemporaryFolderExtension;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(TemporaryFolderExtension.class)
class GameSettingsTest {

    private TemporaryFolder gameDir;

    @Test
    void findsPlayerSetupForAgent() {
        S2Agent agent = mock(S2Agent.class);
        PlayerSettings playerSettings = S2Coordinator.createParticipant(Race.PROTOSS, agent);

        assertThat(new GameSettings().setPlayerSettings(asList(
                playerSettings,
                S2Coordinator.createParticipant(Race.ZERG, mock(S2Agent.class))
        )).playerSettingsFor(agent)).hasValue(playerSettings);
    }

    @Test
    void resolvesAbsoluteLocalMapPath() {
        LocalMap absolute = LocalMap.of(Fixtures.BEL_SHIR_VESTIGE_LE.toAbsolutePath());
        assertThat(new GameSettings().setLocalMap(absolute).resolveMap(new ProcessSettings()).getLocalMap())
                .as("absolute path of local map")
                .hasValue(absolute);
    }

    @Test
    void resolvesGameDirLocalMapLocation() {
        gameDir.newFile(Paths.get("maps", "Test"), Fixtures.MAP_EMPTY.getFileName().toString());
        LocalMap gameLocalMap = LocalMap.of(Fixtures.MAP_EMPTY);
        assertThat(new GameSettings()
                .setLocalMap(gameLocalMap)
                .resolveMap(new ProcessSettings().setRootPath(gameDir.getRootFolder()))
                .getLocalMap()
        ).as("game path of local map").hasValue(gameLocalMap);
    }

    @Test
    void resolvesLibraryDirLocalMapPath() throws URISyntaxException {
        LocalMap libraryMap = LocalMap.of(Fixtures.BEL_SHIR_VESTIGE_LE);
        assertThat(new GameSettings().setLocalMap(libraryMap).resolveMap(new ProcessSettings()).getLocalMap())
                .as("library path of local map")
                .hasValue(LocalMap.of(Paths.get(getLibraryMap(Fixtures.BEL_SHIR_VESTIGE_LE))));
    }

    private URI getLibraryMap(Path path) throws URISyntaxException {
        return this.getClass().getClassLoader().getResource(Paths.get("maps").resolve(path).toString()).toURI();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(GameSettings.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withPrefabValues(S2Client.class, mock(S2Client.class), mock(S2Client.class))
                .verify();
    }

}
