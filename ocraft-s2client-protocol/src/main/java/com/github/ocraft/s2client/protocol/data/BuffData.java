package com.github.ocraft.s2client.protocol.data;

import SC2APIProtocol.Data;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class BuffData implements Serializable {

    private static final long serialVersionUID = -4459965548063361044L;

    private final Buff buff;
    private final String name;

    private BuffData(Data.BuffData sc2ApiBuffData) {
        buff = tryGet(Data.BuffData::getBuffId, Data.BuffData::hasBuffId)
                .apply(sc2ApiBuffData).map(Buffs::from).orElseThrow(required("buff"));

        name = tryGet(Data.BuffData::getName, Data.BuffData::hasName)
                .apply(sc2ApiBuffData).orElseThrow(required("name"));
    }

    public static BuffData from(Data.BuffData sc2ApiBuffData) {
        require("sc2api buff data", sc2ApiBuffData);
        return new BuffData(sc2ApiBuffData);
    }

    public Buff getBuff() {
        return buff;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BuffData buffData = (BuffData) o;

        return buff == buffData.buff && name.equals(buffData.name);
    }

    @Override
    public int hashCode() {
        int result = buff.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
