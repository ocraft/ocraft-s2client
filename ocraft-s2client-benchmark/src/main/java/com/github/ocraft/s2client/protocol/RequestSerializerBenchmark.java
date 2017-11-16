/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
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
 */
package com.github.ocraft.s2client.protocol;

import com.github.ocraft.s2client.protocol.game.BattlenetMap;
import com.github.ocraft.s2client.protocol.game.Difficulty;
import com.github.ocraft.s2client.protocol.request.Request;
import org.openjdk.jmh.annotations.*;

import static com.github.ocraft.s2client.protocol.game.ComputerPlayerSetup.computer;
import static com.github.ocraft.s2client.protocol.game.PlayerSetup.participant;
import static com.github.ocraft.s2client.protocol.game.Race.PROTOSS;
import static com.github.ocraft.s2client.protocol.request.Requests.createGame;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 10)
@Fork(value = 1, warmups = 1)
@Threads(10)
public class RequestSerializerBenchmark {

    @State(Scope.Benchmark)
    public static class Context {
        private Request request = createGame()
                .onBattlenetMap(BattlenetMap.of("Lava Flow"))
                .withPlayerSetup(participant(), computer(PROTOSS, Difficulty.MEDIUM))
                .disableFog()
                .build();
    }

    @Benchmark
    public byte[] serializeSc2ApiRequest(Context ctx) {
        return new RequestSerializer().apply(ctx.request);
    }

}
