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
package com.github.ocraft.s2client.protocol.score;

import SC2APIProtocol.ScoreOuterClass;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class CategoryScoreDetails implements Serializable {

    private static final long serialVersionUID = 9024728727903722737L;

    private final float none; // Used when no other category is configured in game data
    private final float army;
    private final float economy;
    private final float technology;
    private final float upgrade;

    private CategoryScoreDetails(ScoreOuterClass.CategoryScoreDetails sc2APiCategoryScoreDetails) {
        none = tryGet(
                ScoreOuterClass.CategoryScoreDetails::getNone, ScoreOuterClass.CategoryScoreDetails::hasNone
        ).apply(sc2APiCategoryScoreDetails).orElseThrow(required("none"));

        army = tryGet(
                ScoreOuterClass.CategoryScoreDetails::getArmy, ScoreOuterClass.CategoryScoreDetails::hasArmy
        ).apply(sc2APiCategoryScoreDetails).orElseThrow(required("army"));

        economy = tryGet(
                ScoreOuterClass.CategoryScoreDetails::getEconomy, ScoreOuterClass.CategoryScoreDetails::hasEconomy
        ).apply(sc2APiCategoryScoreDetails).orElseThrow(required("economy"));

        technology = tryGet(
                ScoreOuterClass.CategoryScoreDetails::getTechnology, ScoreOuterClass.CategoryScoreDetails::hasTechnology
        ).apply(sc2APiCategoryScoreDetails).orElseThrow(required("technology"));

        upgrade = tryGet(
                ScoreOuterClass.CategoryScoreDetails::getUpgrade, ScoreOuterClass.CategoryScoreDetails::hasUpgrade
        ).apply(sc2APiCategoryScoreDetails).orElseThrow(required("upgrade"));

    }

    public static CategoryScoreDetails from(ScoreOuterClass.CategoryScoreDetails sc2ApiCategoryScoreDetails) {
        require("sc2api category score details", sc2ApiCategoryScoreDetails);
        return new CategoryScoreDetails(sc2ApiCategoryScoreDetails);
    }

    public float getNone() {
        return none;
    }

    public float getArmy() {
        return army;
    }

    public float getEconomy() {
        return economy;
    }

    public float getTechnology() {
        return technology;
    }

    public float getUpgrade() {
        return upgrade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryScoreDetails that = (CategoryScoreDetails) o;

        return Float.compare(that.none, none) == 0 &&
                Float.compare(that.army, army) == 0 &&
                Float.compare(that.economy, economy) == 0 &&
                Float.compare(that.technology, technology) == 0 &&
                Float.compare(that.upgrade, upgrade) == 0;
    }

    @Override
    public int hashCode() {
        int result = (none != +0.0f ? Float.floatToIntBits(none) : 0);
        result = 31 * result + (army != +0.0f ? Float.floatToIntBits(army) : 0);
        result = 31 * result + (economy != +0.0f ? Float.floatToIntBits(economy) : 0);
        result = 31 * result + (technology != +0.0f ? Float.floatToIntBits(technology) : 0);
        result = 31 * result + (upgrade != +0.0f ? Float.floatToIntBits(upgrade) : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
