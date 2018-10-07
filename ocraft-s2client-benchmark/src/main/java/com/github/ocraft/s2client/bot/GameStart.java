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

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.api.test.GameServer;

import java.util.function.Supplier;

public class GameStart {

    static final int GAME_SERVER_PORT = 5000;

    public static void main(String[] args) throws InterruptedException {
        GameServer.create(GAME_SERVER_PORT)
                .onRequest(Sc2Api.Request::hasPing, pingResponse())
                .onRequest(Sc2Api.Request::hasStep, stepResponse())
                .onRequest(Sc2Api.Request::hasObservation, observationResponse())
                .start();

        Thread.currentThread().join();
    }

    private static Supplier<Sc2Api.Response> observationResponse() {
        return () -> Sc2Api.Response.newBuilder()
                .setObservation(Fixtures.sc2ApiResponseObservationWithoutSpatialInterfaces())
                .setStatus(Sc2Api.Status.in_game)
                .build();
    }

    private static Supplier<Sc2Api.Response> stepResponse() {
        return () -> Sc2Api.Response.newBuilder().setStep(
                Sc2Api.ResponseStep.newBuilder().build())
                .setStatus(Sc2Api.Status.in_game)
                .build();
    }

    private static Supplier<Sc2Api.Response> pingResponse() {
        return () -> Sc2Api.Response.newBuilder().setPing(Fixtures.sc2ApiResponsePing())
                .setStatus(Sc2Api.Status.in_game)
                .build();
    }
}
