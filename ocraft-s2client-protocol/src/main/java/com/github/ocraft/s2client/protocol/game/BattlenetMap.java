package com.github.ocraft.s2client.protocol.game;

import com.fasterxml.jackson.annotation.JsonValue;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;

public final class BattlenetMap implements Sc2ApiSerializable<String> {

    private static final long serialVersionUID = 4490686832518868510L;

    private final String name;

    private BattlenetMap(String name) {
        this.name = name;
    }

    @Override
    public String toSc2Api() {
        return name;
    }

    public static BattlenetMap of(String battlenetMapName) {
        if (battlenetMapName == null) throw new IllegalArgumentException("battlenet map name is required");
        return new BattlenetMap(battlenetMapName);
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BattlenetMap that = (BattlenetMap) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
