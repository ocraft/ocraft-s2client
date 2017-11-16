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
