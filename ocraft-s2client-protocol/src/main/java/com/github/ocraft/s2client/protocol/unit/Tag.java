package com.github.ocraft.s2client.protocol.unit;

import com.fasterxml.jackson.annotation.JsonValue;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class Tag implements Sc2ApiSerializable<Long> {

    private static final long serialVersionUID = 795663374412078618L;

    private final Long value;

    private Tag(Long sc2apiTag) {
        this.value = sc2apiTag;
    }

    public static Tag from(Long sc2apiTag) {
        require("sc2api unit tag", sc2apiTag);
        return new Tag(sc2apiTag);
    }

    public static Tag of(Long sc2apiTag) {
        return Tag.from(sc2apiTag);
    }

    public static Tag tag(Long sc2apiTag) {
        return Tag.from(sc2apiTag);
    }

    @Override
    public Long toSc2Api() {
        return value;
    }

    @JsonValue
    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        return value.equals(tag.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
