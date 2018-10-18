package com.github.ocraft.s2client.protocol.observation.ui;

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

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Ability;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class BuildItem implements Serializable {
    private static final long serialVersionUID = 8023565274767731713L;

    private final Ability ability;
    private final float buildProgress;

    private BuildItem(Ui.BuildItem sc2ApiBuildItem) {
        ability = tryGet(Ui.BuildItem::getAbilityId, Ui.BuildItem::hasAbilityId)
                .apply(sc2ApiBuildItem).map(Abilities::from).orElseThrow(required("ability"));

        buildProgress = tryGet(
                Ui.BuildItem::getBuildProgress, Ui.BuildItem::hasBuildProgress
        ).apply(sc2ApiBuildItem).orElseThrow(required("build progress"));
    }

    public static BuildItem from(Ui.BuildItem sc2ApiBuildItem) {
        require("sc2api build item", sc2ApiBuildItem);
        return new BuildItem(sc2ApiBuildItem);
    }

    public Ability getAbility() {
        return ability;
    }

    public float getBuildProgress() {
        return buildProgress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BuildItem buildItem = (BuildItem) o;

        if (Float.compare(buildItem.buildProgress, buildProgress) != 0) return false;
        return ability.equals(buildItem.ability);
    }

    @Override
    public int hashCode() {
        int result = ability.hashCode();
        result = 31 * result + (buildProgress != +0.0f ? Float.floatToIntBits(buildProgress) : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
