package com.github.ocraft.s2client.sample;

/*-
 * #%L
 * ocraft-s2client-sample
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

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.api.test.GameServer;
import com.github.ocraft.s2client.bot.S2Agent;
import com.github.ocraft.s2client.bot.S2Coordinator;
import com.github.ocraft.s2client.protocol.game.Difficulty;
import com.github.ocraft.s2client.protocol.game.Race;

public class SampleProxy {

    private static final String GAME_VERSION = "3.17.1.57218";
    private static final String DATA_VERSION = "3F2FCED08798D83B873B5543BEFA6C4B";
    private static final int DATA_BUILD = 57218;
    private static final int BASE_BUILD = 56787;
    private static final int TEST_PORT = 5000;

    public static void main(String[] args) {
        GameServer game = GameServer.create(TEST_PORT)
                .onRequest(Sc2Api.Request::hasPing, SampleProxy::ping)
                .start();

        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .loadSettings(new String[]{"-m", "Lava Flow"})
                .setTraced(true)
                .setParticipants(
                        S2Coordinator.createParticipant(Race.TERRAN, new S2Agent() {
                        }),
                        S2Coordinator.createComputer(Race.ZERG, Difficulty.MEDIUM))
                .connect("127.0.0.1", TEST_PORT);

        s2Coordinator.quit();
        game.stop();
    }

    private static Sc2Api.Response ping() {
        return Sc2Api.Response.newBuilder().setPing(
                Sc2Api.ResponsePing.newBuilder()
                        .setBaseBuild(BASE_BUILD)
                        .setDataBuild(DATA_BUILD)
                        .setDataVersion(DATA_VERSION)
                        .setGameVersion(GAME_VERSION)
                        .build())
                .build();
    }
}
