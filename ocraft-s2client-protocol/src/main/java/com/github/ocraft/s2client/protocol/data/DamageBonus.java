package com.github.ocraft.s2client.protocol.data;

/*-
 * #%L
 * ocraft-s2client-protocol
 * %%
 * Copyright (C) 2017 - 2018 Ocraft Project
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
