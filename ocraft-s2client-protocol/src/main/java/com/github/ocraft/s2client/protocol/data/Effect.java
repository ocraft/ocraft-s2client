package com.github.ocraft.s2client.protocol.data;

import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;

public interface Effect extends Sc2ApiSerializable<Integer> {
    int getEffectId();
}
