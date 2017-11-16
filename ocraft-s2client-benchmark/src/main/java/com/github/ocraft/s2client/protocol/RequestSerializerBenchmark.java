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
