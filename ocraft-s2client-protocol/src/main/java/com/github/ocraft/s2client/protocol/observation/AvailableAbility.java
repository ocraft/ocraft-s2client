package com.github.ocraft.s2client.protocol.observation;

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

import SC2APIProtocol.Common;
import com.github.ocraft.s2client.protocol.GeneralizableAbility;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Ability;

import java.io.Serializable;
import java.util.function.UnaryOperator;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class AvailableAbility implements Serializable, GeneralizableAbility<AvailableAbility> {

    private static final long serialVersionUID = 4523885840022128043L;

    private static final boolean DEFAULT_REQUIRES_POINT = false;

    private final Ability ability;
    private final boolean requiresPoint;

    private AvailableAbility(Common.AvailableAbility sc2ApiAvailableAbility) {
        ability = tryGet(
                Common.AvailableAbility::getAbilityId, Common.AvailableAbility::hasAbilityId
        ).apply(sc2ApiAvailableAbility).map(Abilities::from).orElseThrow(required("ability"));

        requiresPoint = tryGet(
                Common.AvailableAbility::getRequiresPoint, Common.AvailableAbility::hasRequiresPoint
        ).apply(sc2ApiAvailableAbility).orElse(DEFAULT_REQUIRES_POINT);
    }

    private AvailableAbility(Ability ability, boolean requiresPoint) {
        this.ability = ability;
        this.requiresPoint = requiresPoint;
    }

    public static AvailableAbility from(Common.AvailableAbility sc2ApiAvailableAbility) {
        require("sc2api available ability", sc2ApiAvailableAbility);
        return new AvailableAbility(sc2ApiAvailableAbility);
    }

    public Ability getAbility() {
        return ability;
    }

    public boolean isRequiresPoint() {
        return requiresPoint;
    }

    @Override
    public AvailableAbility generalizeAbility(UnaryOperator<Ability> generalize) {
        return new AvailableAbility(generalize.apply(ability), requiresPoint);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AvailableAbility that = (AvailableAbility) o;

        return requiresPoint == that.requiresPoint && ability == that.ability;
    }

    @Override
    public int hashCode() {
        int result = ability.hashCode();
        result = 31 * result + (requiresPoint ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
