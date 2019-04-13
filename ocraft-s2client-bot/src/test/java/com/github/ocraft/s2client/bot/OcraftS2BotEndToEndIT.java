package com.github.ocraft.s2client.bot;

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

import com.github.ocraft.s2client.bot.sequence.*;
import com.github.ocraft.s2client.bot.setting.GameSettings;
import com.github.ocraft.s2client.protocol.game.AiBuild;
import com.github.ocraft.s2client.protocol.game.Difficulty;
import com.github.ocraft.s2client.protocol.game.Race;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("endtoend")
class OcraftS2BotEndToEndIT {

    private static final int TEST_TIMEOUT = 30;

    private class TestBot extends S2Agent {

        private boolean wasStarted;

        @Override
        public void onGameStart() {
            System.out.println("Hello world of Starcraft II bots!");
            wasStarted = true;
        }

        @Override
        public void onStep() {

        }

        boolean success() {
            return wasStarted;
        }
    }

    private class TestReplayObserver extends S2ReplayObserver {

        @Override
        public void onGameStart() {
            System.out.println("Hello world of Starcraft II replays!");
        }

        @Override
        public void onStep() {

        }

        boolean isFinished() {
            return observation().getGameLoop() > 100;
        }

        public boolean success() {
            return isFinished();
        }
    }

    @Test
    void providesSetupForTheGame() {
        TestBot bot = new TestBot();
        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .loadSettings(new String[]{"-m", "Lava Flow"})
                .setParticipants(
                        S2Coordinator.createParticipant(Race.PROTOSS, bot, "Adam"),
                        S2Coordinator.createComputer(Race.ZERG, Difficulty.MEDIUM, "Eve", AiBuild.RUSH))
                .launchStarcraft()
                .startGame();

        assertThat(s2Coordinator.getExePath()).as("actual game exe path").isNotNull();

        await().atMost(TEST_TIMEOUT, TimeUnit.SECONDS).until(bot::success);
        s2Coordinator.quit();
    }

    @Test
    void remapsAbilities() {
        assertThat(RemapAbilitiesTestBot.test()).isTrue();
    }

    @Test
    void providesSnapshots() {
        assertThat(SnapshotTestBot.test()).isTrue();
    }

    @Test
    void controlsMultiplayerGame() throws Exception {
        assertThat(MultiplayerTestBot.test()).isTrue();
    }

    @Test
    void controlsMovementAndCombat() {
        assertThat(MovementAndCombatTestBot.test()).isTrue();
    }

    @Test
    void allowsToFastRestartSinglePlayerGame() {
        assertThat(FastRestartSinglePlayerTestBot.test()).isTrue();
    }

    @Test
    void managesUnitCommands() {
        assertThat(UnitCommandTestBot.test()).isTrue();
    }

    @Test
    void processesReplays() throws Exception {
        TestReplayObserver replayObserver = new TestReplayObserver();
        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .setReplayPath(GameSettings.libraryPath(Paths.get("replays"), Paths.get("/"))
                        .orElseThrow(() -> new AssertionError("replay path is required")))
                .addReplayObserver(replayObserver)
                .launchStarcraft();

        assertThat(s2Coordinator.hasReplays()).as("replays are loaded").isTrue();

        while (!replayObserver.isFinished()) s2Coordinator.update();

        assertThat(replayObserver.success()).isTrue();
        s2Coordinator.quit();
    }
}
