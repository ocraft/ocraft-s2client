package com.github.ocraft.s2client.protocol;

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
