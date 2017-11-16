package com.github.ocraft.s2client.protocol.data;

import SC2APIProtocol.Data;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class DamageBonus implements Serializable {

    private static final long serialVersionUID = 5137816011208442815L;

    private final UnitAttribute attribute;
    private final float bonus;

    private DamageBonus(Data.DamageBonus sc2ApiDamageBonus) {
        attribute = tryGet(Data.DamageBonus::getAttribute, Data.DamageBonus::hasAttribute)
                .apply(sc2ApiDamageBonus).map(UnitAttribute::from).orElseThrow(required("attribute"));

        bonus = tryGet(Data.DamageBonus::getBonus, Data.DamageBonus::hasBonus)
                .apply(sc2ApiDamageBonus).orElseThrow(required("bonus"));
    }

    public static DamageBonus from(Data.DamageBonus sc2ApiDamageBonus) {
        require("sc2api damage bonus", sc2ApiDamageBonus);
        return new DamageBonus(sc2ApiDamageBonus);
    }

    public UnitAttribute getAttribute() {
        return attribute;
    }

    public float getBonus() {
        return bonus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DamageBonus that = (DamageBonus) o;

        return Float.compare(that.bonus, bonus) == 0 && attribute == that.attribute;
    }

    @Override
    public int hashCode() {
        int result = attribute.hashCode();
        result = 31 * result + (bonus != +0.0f ? Float.floatToIntBits(bonus) : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
