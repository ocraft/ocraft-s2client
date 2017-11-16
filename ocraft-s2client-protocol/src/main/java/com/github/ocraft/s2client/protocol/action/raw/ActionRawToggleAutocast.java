package com.github.ocraft.s2client.protocol.action.raw;

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.syntax.action.raw.ActionRawToggleAutocastBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.raw.ActionRawToggleAutocastSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.raw.ForUnitsSyntax;
import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.util.HashSet;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static com.github.ocraft.s2client.protocol.Preconditions.requireNotEmpty;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

public final class ActionRawToggleAutocast implements Sc2ApiSerializable<Raw.ActionRawToggleAutocast> {

    private static final long serialVersionUID = 7653142081908980904L;

    private final Ability ability;
    private final Set<Tag> unitTags;

    public static final class Builder
            implements ActionRawToggleAutocastSyntax, ForUnitsSyntax, ActionRawToggleAutocastBuilder {

        private Ability ability;
        private Set<Tag> unitTags = new HashSet<>();

        @Override
        public ActionRawToggleAutocastBuilder forUnits(Tag... units) {
            unitTags.addAll(asList(units));
            return this;
        }

        @Override
        public ActionRawToggleAutocastBuilder forUnits(Unit... units) {
            unitTags.addAll(stream(units).map(Unit::getTag).collect(toSet()));
            return this;
        }

        @Override
        public ForUnitsSyntax ofAbility(Ability ability) {
            this.ability = ability;
            return this;
        }

        @Override
        public ActionRawToggleAutocast build() {
            require("ability id", ability);
            requireNotEmpty("unit tag list", unitTags);
            return new ActionRawToggleAutocast(this);
        }
    }

    private ActionRawToggleAutocast(Builder builder) {
        ability = builder.ability;
        unitTags = builder.unitTags;
    }

    private ActionRawToggleAutocast(Raw.ActionRawToggleAutocast sc2ApiActionRawToggleAutocast) {
        this.ability = tryGet(
                Raw.ActionRawToggleAutocast::getAbilityId, Raw.ActionRawToggleAutocast::hasAbilityId
        ).apply(sc2ApiActionRawToggleAutocast).map(Abilities::from).orElseThrow(required("ability id"));

        this.unitTags = sc2ApiActionRawToggleAutocast.getUnitTagsList().stream()
                .map(Tag::from).collect(toSet());

        requireNotEmpty("unit tag list", unitTags);
    }

    public static ActionRawToggleAutocastSyntax toggleAutocast() {
        return new Builder();
    }

    public static ActionRawToggleAutocast from(Raw.ActionRawToggleAutocast sc2ApiActionRawToggleAutocast) {
        require("sc2api action raw toggle autocast", sc2ApiActionRawToggleAutocast);
        return new ActionRawToggleAutocast(sc2ApiActionRawToggleAutocast);
    }

    @Override
    public Raw.ActionRawToggleAutocast toSc2Api() {
        return Raw.ActionRawToggleAutocast.newBuilder()
                .setAbilityId(ability.toSc2Api())
                .addAllUnitTags(unitTags.stream().map(Tag::toSc2Api).collect(toSet()))
                .build();
    }

    public Ability getAbility() {
        return ability;
    }

    public Set<Tag> getUnitTags() {
        return new HashSet<>(unitTags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionRawToggleAutocast that = (ActionRawToggleAutocast) o;

        return ability == that.ability && unitTags.equals(that.unitTags);
    }

    @Override
    public int hashCode() {
        int result = ability.hashCode();
        result = 31 * result + unitTags.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
