package com.github.ocraft.s2client.protocol.response;

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

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.*;
import com.github.ocraft.s2client.protocol.game.GameStatus;

import java.util.Collections;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

public final class ResponseData extends Response {

    private static final long serialVersionUID = -5814145777819095941L;

    private final Set<AbilityData> abilities;
    private final Set<UnitTypeData> unitTypes;
    private final Set<UpgradeData> upgrades;
    private final Set<BuffData> buffs;
    private final Set<EffectData> effects;

    private ResponseData(Sc2Api.ResponseData sc2ApiResponseData, Sc2Api.Status status) {
        super(ResponseType.DATA, GameStatus.from(status));

        abilities = sc2ApiResponseData.getAbilitiesList().stream().map(AbilityData::from)
                .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
        unitTypes = sc2ApiResponseData.getUnitsList().stream().map(UnitTypeData::from)
                .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
        upgrades = sc2ApiResponseData.getUpgradesList().stream().map(UpgradeData::from)
                .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
        buffs = sc2ApiResponseData.getBuffsList().stream().map(BuffData::from)
                .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
        effects = sc2ApiResponseData.getEffectsList().stream().map(EffectData::from)
                .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
    }

    public static ResponseData from(Sc2Api.Response sc2ApiResponse) {
        if (!hasDataResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have data response");
        }
        return new ResponseData(sc2ApiResponse.getData(), sc2ApiResponse.getStatus());
    }

    private static boolean hasDataResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasData();
    }

    public Set<AbilityData> getAbilities() {
        return abilities;
    }

    public Set<UnitTypeData> getUnitTypes() {
        return unitTypes;
    }

    public Set<UpgradeData> getUpgrades() {
        return upgrades;
    }

    public Set<BuffData> getBuffs() {
        return buffs;
    }

    public Set<EffectData> getEffects() {
        return effects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ResponseData that = (ResponseData) o;

        return abilities.equals(that.abilities) &&
                unitTypes.equals(that.unitTypes) &&
                upgrades.equals(that.upgrades) &&
                buffs.equals(that.buffs) &&
                effects.equals(that.effects);
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseData;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + abilities.hashCode();
        result = 31 * result + unitTypes.hashCode();
        result = 31 * result + upgrades.hashCode();
        result = 31 * result + buffs.hashCode();
        result = 31 * result + effects.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
