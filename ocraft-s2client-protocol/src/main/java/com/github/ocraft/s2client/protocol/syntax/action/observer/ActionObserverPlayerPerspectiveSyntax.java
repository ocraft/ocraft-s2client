package com.github.ocraft.s2client.protocol.syntax.action.observer;

public interface ActionObserverPlayerPerspectiveSyntax {
    ActionObserverPlayerPerspectiveBuilder ofPlayer(int playerId);

    ActionObserverPlayerPerspectiveBuilder ofAll();
}
