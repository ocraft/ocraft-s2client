package com.github.ocraft.s2client.protocol.observation.ui;

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Ability;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class BuildItem implements Serializable {
    private static final long serialVersionUID = 8023565274767731713L;

    private final Ability ability;
    private final float buildProgress;

    private BuildItem(Ui.BuildItem sc2ApiBuildItem) {
        ability = tryGet(Ui.BuildItem::getAbilityId, Ui.BuildItem::hasAbilityId)
                .apply(sc2ApiBuildItem).map(Abilities::from).orElseThrow(required("ability"));

        buildProgress = tryGet(
                Ui.BuildItem::getBuildProgress, Ui.BuildItem::hasBuildProgress
        ).apply(sc2ApiBuildItem).orElseThrow(required("build progress"));
    }

    public static BuildItem from(Ui.BuildItem sc2ApiBuildItem) {
        require("sc2api build item", sc2ApiBuildItem);
        return new BuildItem(sc2ApiBuildItem);
    }

    public Ability getAbility() {
        return ability;
    }

    public float getBuildProgress() {
        return buildProgress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BuildItem buildItem = (BuildItem) o;

        if (Float.compare(buildItem.buildProgress, buildProgress) != 0) return false;
        return ability.equals(buildItem.ability);
    }

    @Override
    public int hashCode() {
        int result = ability.hashCode();
        result = 31 * result + (buildProgress != +0.0f ? Float.floatToIntBits(buildProgress) : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
