package com.github.ocraft.s2client.bot.sequence;

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

import com.github.ocraft.s2client.bot.Fixtures;
import com.github.ocraft.s2client.bot.S2Coordinator;
import com.github.ocraft.s2client.bot.setting.GameSettings;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import com.github.ocraft.s2client.protocol.game.Race;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class MultiplayerTestBot extends UnitTestBot {

    private MultiplayerTestBot() {
        add(new WaitT(500));
    }

    @Override
    protected void onTestsBegin() {
        debug().debugShowMap();
        debug().debugEnemyControl();
    }

    @Override
    protected void onTestsEnd() {

    }

    private static boolean remoteSaveMap(
            S2Coordinator coordinator, Path sourceMap, Path remotePath) throws IOException {
        Optional<Path> libraryPath = GameSettings.libraryPath(sourceMap);
        return libraryPath.isPresent() && coordinator.remoteSaveMap(Files.readAllBytes(libraryPath.get()), remotePath);
    }

    public static boolean test() throws IOException {
        Path remotePath = Paths.get("temp_foo.SC2Map");

        MultiplayerTestBot bot01 = new MultiplayerTestBot();
        MultiplayerTestBot bot02 = new MultiplayerTestBot();
        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .loadSettings(new String[]{})
                .setMultithreaded(true)
                .setParticipants(
                        S2Coordinator.createParticipant(Race.TERRAN, bot01),
                        S2Coordinator.createParticipant(Race.TERRAN, bot02))
                .launchStarcraft();

        if (!remoteSaveMap(s2Coordinator, Fixtures.BEL_SHIR_VESTIGE_LE, remotePath)) {
            return false;
        }

        s2Coordinator.startGame(LocalMap.of(remotePath));

        while (!bot01.isFinished() && !bot02.isFinished()) {
            s2Coordinator.update();
        }
        s2Coordinator.quit();
        return true;
    }

}
