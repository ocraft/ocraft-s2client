package com.github.ocraft.s2client.protocol;

import com.github.ocraft.s2client.bot.Fixtures;
import com.github.ocraft.s2client.protocol.observation.spatial.ImageData;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5)
@Fork(value = 1, warmups = 1)
@Threads(1)
public class ImageDataBenchmark {

    @Benchmark
    public ImageData deserializeImageData() {
        return ImageData.from(Fixtures.sc2ApiImageData());
    }

    public static void main(String[] args) throws Exception {

        Options options = new OptionsBuilder()
                .include(ImageDataBenchmark.class.getSimpleName())
                .shouldFailOnError(true).shouldDoGC(true).build();

        new Runner(options).run();

        System.exit(1);
    }
}