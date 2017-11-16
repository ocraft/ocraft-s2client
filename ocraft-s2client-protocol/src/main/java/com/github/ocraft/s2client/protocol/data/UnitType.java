package com.github.ocraft.s2client.protocol.data;

import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;

import java.util.Set;

public interface UnitType extends Sc2ApiSerializable<Integer> {
    int getUnitTypeId();

    Set<Abilities> getAbilities();
}
