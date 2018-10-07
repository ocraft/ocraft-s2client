package com.github.ocraft.s2client.bot;


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
