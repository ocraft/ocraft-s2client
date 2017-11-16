package com.github.ocraft.s2client.protocol;

import java.io.Serializable;

public interface Sc2ApiSerializable<T> extends Serializable {
    T toSc2Api();
}
