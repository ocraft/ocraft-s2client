package com.github.ocraft.s2client.protocol.data;

import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;

import java.util.Set;

public interface Ability extends Sc2ApiSerializable<Integer> {
    int getAbilityId();

    Set<Target> getTargets();
}
