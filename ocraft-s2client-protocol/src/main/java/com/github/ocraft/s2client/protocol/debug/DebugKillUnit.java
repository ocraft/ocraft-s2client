package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.syntax.debug.DebugKillUnitBuilder;
import com.github.ocraft.s2client.protocol.syntax.debug.DebugKillUnitSyntax;
import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.util.HashSet;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.Preconditions.requireNotEmpty;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

public final class DebugKillUnit implements Sc2ApiSerializable<Debug.DebugKillUnit> {

    private static final long serialVersionUID = -1505849217975183703L;

    private final Set<Tag> unitTags;

    public static final class Builder implements DebugKillUnitSyntax, DebugKillUnitBuilder {

        private Set<Tag> unitTag = new HashSet<>();

        @Override
        public DebugKillUnitBuilder withTags(Tag... unitTag) {
            this.unitTag.addAll(asList(unitTag));
            return this;
        }

        @Override
        public DebugKillUnitBuilder of(Unit... units) {
            this.unitTag.addAll(stream(units).map(Unit::getTag).collect(toSet()));
            return this;
        }

        @Override
        public DebugKillUnit build() {
            requireNotEmpty("unit tag set", unitTag);
            return new DebugKillUnit(this);
        }

    }

    private DebugKillUnit(Builder builder) {
        unitTags = builder.unitTag;
    }

    public static DebugKillUnitSyntax killUnit() {
        return new Builder();
    }

    @Override
    public Debug.DebugKillUnit toSc2Api() {
        return Debug.DebugKillUnit.newBuilder()
                .addAllTag(unitTags.stream().map(Tag::toSc2Api).collect(toSet()))
                .build();
    }

    public Set<Tag> getUnitTags() {
        return new HashSet<>(unitTags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DebugKillUnit that = (DebugKillUnit) o;

        return unitTags.equals(that.unitTags);
    }

    @Override
    public int hashCode() {
        return unitTags.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
