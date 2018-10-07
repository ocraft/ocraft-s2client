package com.github.ocraft.s2client.bot;

/*-
 * #%L
 * ocraft-s2client-benchmark
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


import com.github.ocraft.s2client.protocol.game.Difficulty;
import com.github.ocraft.s2client.protocol.game.Race;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;

// -ea -XX:+UnlockCommercialFeatures -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints -XX:StartFlightRecording=filename=recording.jfr,settings=profile

// TODO p.picheta immutable collections -> opt (ResponseObservation...)

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5)
@Fork(value = 1, warmups = 1)
@Threads(1)
public class UpdateLoopBenchmark {

    private static class TestBot extends S2Agent {

        @Override
        public void onError(List<ClientError> clientErrors, List<String> protocolErrors) {
            throw new IllegalStateException(
                    String.format("client errors [%s]; protocol errors %s", clientErrors, protocolErrors));
        }
    }

    @State(Scope.Benchmark)
    public static class Context {

        private static final String GAME_SERVER_IP = "127.0.0.1";

        private S2Coordinator coordinator;

        @Setup(Level.Trial)
        public void doSetup() {
            coordinator = S2Coordinator.setup()
                    .setTraced(false)
                    .setParticipants(
                            S2Coordinator.createParticipant(Race.PROTOSS, new TestBot()),
                            S2Coordinator.createComputer(Race.ZERG, Difficulty.VERY_EASY))
                    .connect(GAME_SERVER_IP, GameStart.GAME_SERVER_PORT);
        }


        @TearDown(Level.Trial)
        public void doTearDown() {
            coordinator.quit();
        }

    }

    @Benchmark
    public boolean step(Context ctx) {
        return ctx.coordinator.update();
    }

    public static void main(String[] args) throws Exception {

        Options options = new OptionsBuilder()
                .include(UpdateLoopBenchmark.class.getSimpleName())
                .shouldFailOnError(true).shouldDoGC(true).build();

        new Runner(options).run();

        System.exit(1);
    }
}
