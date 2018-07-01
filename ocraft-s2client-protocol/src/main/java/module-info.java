module com.github.ocraft.s2client.protocol {
    requires protobuf.java;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.datatype.jdk8;
    requires java.desktop;

    exports com.github.ocraft.s2client.protocol;
    exports com.github.ocraft.s2client.protocol.action;
    exports com.github.ocraft.s2client.protocol.action.observer;
    exports com.github.ocraft.s2client.protocol.action.raw;
    exports com.github.ocraft.s2client.protocol.action.spatial;
    exports com.github.ocraft.s2client.protocol.action.ui;
    exports com.github.ocraft.s2client.protocol.data;
    exports com.github.ocraft.s2client.protocol.debug;
    exports com.github.ocraft.s2client.protocol.game;
    exports com.github.ocraft.s2client.protocol.game.raw;
    exports com.github.ocraft.s2client.protocol.observation;
    exports com.github.ocraft.s2client.protocol.observation.raw;
    exports com.github.ocraft.s2client.protocol.observation.spatial;
    exports com.github.ocraft.s2client.protocol.observation.ui;
    exports com.github.ocraft.s2client.protocol.query;
    exports com.github.ocraft.s2client.protocol.request;
    exports com.github.ocraft.s2client.protocol.response;
    exports com.github.ocraft.s2client.protocol.score;
    exports com.github.ocraft.s2client.protocol.spatial;
    exports com.github.ocraft.s2client.protocol.syntax.action;
    exports com.github.ocraft.s2client.protocol.syntax.action.observer;
    exports com.github.ocraft.s2client.protocol.syntax.action.raw;
    exports com.github.ocraft.s2client.protocol.syntax.action.spatial;
    exports com.github.ocraft.s2client.protocol.syntax.action.ui;
    exports com.github.ocraft.s2client.protocol.syntax.debug;
    exports com.github.ocraft.s2client.protocol.syntax.query;
    exports com.github.ocraft.s2client.protocol.syntax.request;
    exports com.github.ocraft.s2client.protocol.unit;

    opens com.github.ocraft.s2client.protocol to com.fasterxml.jackson.databind;
}