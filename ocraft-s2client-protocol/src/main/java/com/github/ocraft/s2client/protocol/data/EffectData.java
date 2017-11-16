/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
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
 */
package com.github.ocraft.s2client.protocol.data;

import SC2APIProtocol.Data;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class EffectData implements Serializable {

    private static final long serialVersionUID = 6673079704985681098L;

    private final Effect effect;
    private final String name;
    private final String friendlyName;
    private final float radius;

    private EffectData(Data.EffectData sc2ApiEffectData) {
        effect = tryGet(Data.EffectData::getEffectId, Data.EffectData::hasEffectId)
                .apply(sc2ApiEffectData).map(Effects::from).orElseThrow(required("effect"));

        name = tryGet(Data.EffectData::getName, Data.EffectData::hasName)
                .apply(sc2ApiEffectData).orElseThrow(required("name"));

        friendlyName = tryGet(Data.EffectData::getFriendlyName, Data.EffectData::hasFriendlyName)
                .apply(sc2ApiEffectData).orElseThrow(required("friendly name"));

        radius = tryGet(Data.EffectData::getRadius, Data.EffectData::hasRadius)
                .apply(sc2ApiEffectData).orElseThrow(required("radius"));
    }

    public static EffectData from(Data.EffectData sc2ApiEffectData) {
        require("sc2api effect data", sc2ApiEffectData);
        return new EffectData(sc2ApiEffectData);
    }

    public Effect getEffect() {
        return effect;
    }

    public String getName() {
        return name;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EffectData that = (EffectData) o;

        return Float.compare(that.radius, radius) == 0 &&
                effect == that.effect &&
                name.equals(that.name) &&
                friendlyName.equals(that.friendlyName);
    }

    @Override
    public int hashCode() {
        int result = effect.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + friendlyName.hashCode();
        result = 31 * result + (radius != +0.0f ? Float.floatToIntBits(radius) : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
