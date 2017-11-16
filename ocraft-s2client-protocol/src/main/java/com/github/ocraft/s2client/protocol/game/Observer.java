package com.github.ocraft.s2client.protocol.game;

import com.fasterxml.jackson.annotation.JsonValue;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;

public final class Observer implements Sc2ApiSerializable<Integer> {

    private static final long serialVersionUID = 4395714227740523870L;

    private final int observedPlayerId;

    private Observer(int observedPlayerId) {
        this.observedPlayerId = observedPlayerId;
    }

    public static Observer of(int observedPlayerId) {
        return new Observer(observedPlayerId);
    }

    @JsonValue
    public int getObservedPlayerId() {
        return observedPlayerId;
    }

    @Override
    public Integer toSc2Api() {
        return observedPlayerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Observer observer = (Observer) o;

        return observedPlayerId == observer.observedPlayerId;
    }

    @Override
    public int hashCode() {
        return observedPlayerId;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
