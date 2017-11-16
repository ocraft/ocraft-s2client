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
package com.github.ocraft.s2client.protocol.unit;

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.spatial.Point;

import java.io.Serializable;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class UnitOrder implements Serializable {

    private static final long serialVersionUID = -3274022682192775161L;

    private final Ability ability;
    private final Tag targetedUnitTag;
    private final Point targetedWorldSpacePosition;
    private final Float progress; // Progress of train abilities. Range: [0.0, 1.0]

    private UnitOrder(Raw.UnitOrder sc2ApiUnitOrder) {
        ability = tryGet(Raw.UnitOrder::getAbilityId, Raw.UnitOrder::hasAbilityId)
                .apply(sc2ApiUnitOrder).map(Abilities::from).orElseThrow(required("ability"));

        targetedUnitTag = tryGet(Raw.UnitOrder::getTargetUnitTag, Raw.UnitOrder::hasTargetUnitTag)
                .apply(sc2ApiUnitOrder).map(Tag::from).orElse(nothing());

        targetedWorldSpacePosition = tryGet(
                Raw.UnitOrder::getTargetWorldSpacePos, Raw.UnitOrder::hasTargetWorldSpacePos
        ).apply(sc2ApiUnitOrder).map(Point::from).orElse(nothing());

        progress = tryGet(Raw.UnitOrder::getProgress, Raw.UnitOrder::hasProgress)
                .apply(sc2ApiUnitOrder).orElse(nothing());
    }

    public static UnitOrder from(Raw.UnitOrder sc2ApiUnitOrder) {
        require("sc2api unit order", sc2ApiUnitOrder);
        return new UnitOrder(sc2ApiUnitOrder);
    }

    public Ability getAbility() {
        return ability;
    }

    public Optional<Tag> getTargetedUnitTag() {
        return Optional.ofNullable(targetedUnitTag);
    }

    public Optional<Point> getTargetedWorldSpacePosition() {
        return Optional.ofNullable(targetedWorldSpacePosition);
    }

    public Optional<Float> getProgress() {
        return Optional.ofNullable(progress);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitOrder unitOrder = (UnitOrder) o;

        return ability == unitOrder.ability &&
                (targetedUnitTag != null
                        ? targetedUnitTag.equals(unitOrder.targetedUnitTag)
                        : unitOrder.targetedUnitTag == null) &&
                (targetedWorldSpacePosition != null
                        ? targetedWorldSpacePosition.equals(unitOrder.targetedWorldSpacePosition)
                        : unitOrder.targetedWorldSpacePosition == null) &&
                (progress != null ? progress.equals(unitOrder.progress) : unitOrder.progress == null);
    }

    @Override
    public int hashCode() {
        int result = ability.hashCode();
        result = 31 * result + (targetedUnitTag != null ? targetedUnitTag.hashCode() : 0);
        result = 31 * result + (targetedWorldSpacePosition != null ? targetedWorldSpacePosition.hashCode() : 0);
        result = 31 * result + (progress != null ? progress.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
