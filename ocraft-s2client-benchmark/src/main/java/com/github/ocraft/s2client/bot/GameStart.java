package com.github.ocraft.s2client.bot;

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
