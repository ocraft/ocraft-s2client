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
package com.github.ocraft.s2client.protocol.action.ui;

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.syntax.action.ui.ActionUiToggleAutocastBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.ui.ActionUiToggleAutocastSyntax;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ActionUiToggleAutocast implements Sc2ApiSerializable<Ui.ActionToggleAutocast> {

    private static final long serialVersionUID = -4515506830076144990L;

    private final Ability ability;

    public static final class Builder implements ActionUiToggleAutocastSyntax, ActionUiToggleAutocastBuilder {
        private Ability ability;

        @Override
        public ActionUiToggleAutocastBuilder ofAbility(Ability ability) {
            this.ability = ability;
            return this;
        }

        @Override
        public ActionUiToggleAutocast build() {
            require("ability", ability);
            return new ActionUiToggleAutocast(this);
        }
    }

    private ActionUiToggleAutocast(Builder builder) {
        ability = builder.ability;
    }

    private ActionUiToggleAutocast(Ui.ActionToggleAutocast sc2ApiActionToggleAutocast) {
        this.ability = tryGet(
                Ui.ActionToggleAutocast::getAbilityId, Ui.ActionToggleAutocast::hasAbilityId
        ).apply(sc2ApiActionToggleAutocast).map(Abilities::from).orElseThrow(required("ability id"));
    }

    public static ActionUiToggleAutocast from(Ui.ActionToggleAutocast sc2ApiActionToggleAutocast) {
        require("sc2api action ui toggle autocast", sc2ApiActionToggleAutocast);
        return new ActionUiToggleAutocast(sc2ApiActionToggleAutocast);
    }

    public static ActionUiToggleAutocastSyntax toggleAutocast() {
        return new Builder();
    }

    @Override
    public Ui.ActionToggleAutocast toSc2Api() {
        return Ui.ActionToggleAutocast.newBuilder().setAbilityId(ability.toSc2Api()).build();
    }

    public Ability getAbility() {
        return ability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionUiToggleAutocast that = (ActionUiToggleAutocast) o;

        return ability == that.ability;
    }

    @Override
    public int hashCode() {
        return ability.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
