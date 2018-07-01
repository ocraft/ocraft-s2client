module com.github.ocraft.s2client.api {
    requires transitive com.github.ocraft.s2client.protocol;

    requires transitive reactive.streams;
    requires transitive rxjava;
    requires vertx.core;
    requires vertx.rx.java2;
    requires config;
    requires slf4j.api;
    requires jdk.unsupported;

    exports com.github.ocraft.s2client.api;
    exports com.github.ocraft.s2client.api.controller;
    exports com.github.ocraft.s2client.api.log;
    exports com.github.ocraft.s2client.api.rx;
    exports com.github.ocraft.s2client.api.syntax;
}