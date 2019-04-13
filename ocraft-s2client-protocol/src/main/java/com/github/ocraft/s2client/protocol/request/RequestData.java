package com.github.ocraft.s2client.protocol.request;

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
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.response.ResponseType;
import com.github.ocraft.s2client.protocol.syntax.request.RequestDataSyntax;

import static java.util.Arrays.stream;

public final class RequestData extends Request {

    private static final long serialVersionUID = -6188023493268720867L;

    private final boolean ability;
    private final boolean unitType;
    private final boolean upgrade;
    private final boolean buff;
    private final boolean effect;

    public enum Type {
        ABILITIES, BUFFS, UPGRADES, EFFECTS, UNITS
    }

    public static final class Builder implements BuilderSyntax<RequestData>, RequestDataSyntax {

        private boolean ability;
        private boolean unitType;
        private boolean upgrade;
        private boolean buff;
        private boolean effect;

        @Override
        public BuilderSyntax<RequestData> of(Type... types) {
            stream(types).forEach(type -> {
                switch (type) {
                    case BUFFS:
                        this.buff = true;
                        break;
                    case UNITS:
                        this.unitType = true;
                        break;
                    case EFFECTS:
                        this.effect = true;
                        break;
                    case UPGRADES:
                        this.upgrade = true;
                        break;
                    case ABILITIES:
                        this.ability = true;
                        break;
                    default:
                        throw new AssertionError("unknown data type: " + type);
                }
            });
            return this;
        }

        @Override
        public RequestData build() {
            return new RequestData(this);
        }
    }

    private RequestData(Builder builder) {
        ability = builder.ability;
        unitType = builder.unitType;
        upgrade = builder.upgrade;
        buff = builder.buff;
        effect = builder.effect;
    }

    public static RequestDataSyntax data() {
        return new Builder();
    }

    @Override
    public Sc2Api.Request toSc2Api() {
        return Sc2Api.Request.newBuilder()
                .setData(Sc2Api.RequestData.newBuilder()
                        .setAbilityId(ability)
                        .setBuffId(buff)
                        .setEffectId(effect)
                        .setUnitTypeId(unitType)
                        .setUpgradeId(upgrade)
                        .build())
                .build();
    }

    @Override
    public ResponseType responseType() {
        return ResponseType.DATA;
    }

    public boolean isAbility() {
        return ability;
    }

    public boolean isUnitType() {
        return unitType;
    }

    public boolean isUpgrade() {
        return upgrade;
    }

    public boolean isBuff() {
        return buff;
    }

    public boolean isEffect() {
        return effect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RequestData that = (RequestData) o;

        if (ability != that.ability) return false;
        if (unitType != that.unitType) return false;
        if (upgrade != that.upgrade) return false;
        if (buff != that.buff) return false;
        return effect == that.effect;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (ability ? 1 : 0);
        result = 31 * result + (unitType ? 1 : 0);
        result = 31 * result + (upgrade ? 1 : 0);
        result = 31 * result + (buff ? 1 : 0);
        result = 31 * result + (effect ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
