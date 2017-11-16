package com.github.ocraft.s2client.api.vertx;

import io.vertx.reactivex.core.Vertx;

class VertxFactory {
    private VertxFactory() {
        throw new AssertionError("private constructor");
    }

    static Vertx create() {
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4JLogDelegateFactory");
        return Vertx.vertx();
    }


}
