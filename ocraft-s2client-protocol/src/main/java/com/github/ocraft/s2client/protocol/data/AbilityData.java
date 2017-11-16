package com.github.ocraft.s2client.protocol.data;

import SC2APIProtocol.Data;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class AbilityData implements Serializable {

    private static final long serialVersionUID = -4865171889353648868L;

    private final Ability ability;          // Stable ID.
    private final String linkName;          // Catalog name of the ability.
    private final int linkIndex;            // Catalog index of the ability.
    private final String buttonName;        // Name used for the command card. May not always be set.
    private final String friendlyName;      // A human friendly name when the button name or link name isn't descriptive.
    private final String hotkey;            // Hotkey. May not always be set.
    private final Integer remapsToAbilityId;// This ability id may be represented by the given more generic id.
    private final boolean available;        // If true, the ability may be used by this set of mods/map.
    private final Target target;            // Determines if a point is private final or required.
    private final boolean allowMinimap;     // Can be cast in the minimap.
    private final boolean allowAutocast;    // Autocast can be set.
    private final boolean building;         // Requires placement to construct a building.
    private final Float footprintRadius;    // Estimation of the footprint size. Need a better footprint.
    private final boolean instantPlacement; // Placement next to an existing structure, e.g., an add-on like a Tech Lab.
    private final Float castRange;          // Range unit can cast ability without needing to approach target.

    private AbilityData(Data.AbilityData sc2ApiAbilityData) {
        ability = tryGet(Data.AbilityData::getAbilityId, Data.AbilityData::hasAbilityId)
                .apply(sc2ApiAbilityData).map(Abilities::from).orElseThrow(required("ability"));

        linkName = tryGet(Data.AbilityData::getLinkName, Data.AbilityData::hasLinkName)
                .apply(sc2ApiAbilityData).orElseThrow(required("link name"));

        linkIndex = tryGet(Data.AbilityData::getLinkIndex, Data.AbilityData::hasLinkIndex)
                .apply(sc2ApiAbilityData).orElseThrow(required("link index"));

        buttonName = tryGet(Data.AbilityData::getButtonName, Data.AbilityData::hasButtonName)
                .apply(sc2ApiAbilityData).orElse(nothing());

        friendlyName = tryGet(Data.AbilityData::getFriendlyName, Data.AbilityData::hasFriendlyName)
                .apply(sc2ApiAbilityData).orElse(nothing());

        hotkey = tryGet(Data.AbilityData::getHotkey, Data.AbilityData::hasHotkey)
                .apply(sc2ApiAbilityData).orElse(nothing());

        remapsToAbilityId = tryGet(Data.AbilityData::getRemapsToAbilityId, Data.AbilityData::hasRemapsToAbilityId)
                .apply(sc2ApiAbilityData).orElse(nothing());

        available = tryGet(Data.AbilityData::getAvailable, Data.AbilityData::hasAvailable)
                .apply(sc2ApiAbilityData).orElse(false);

        target = tryGet(Data.AbilityData::getTarget, Data.AbilityData::hasTarget)
                .apply(sc2ApiAbilityData).map(Target::from).orElse(nothing());

        allowMinimap = tryGet(Data.AbilityData::getAllowMinimap, Data.AbilityData::hasAllowMinimap)
                .apply(sc2ApiAbilityData).orElse(false);

        allowAutocast = tryGet(Data.AbilityData::getAllowAutocast, Data.AbilityData::hasAllowAutocast)
                .apply(sc2ApiAbilityData).orElse(false);

        building = tryGet(Data.AbilityData::getIsBuilding, Data.AbilityData::hasIsBuilding)
                .apply(sc2ApiAbilityData).orElse(false);

        footprintRadius = tryGet(Data.AbilityData::getFootprintRadius, Data.AbilityData::hasFootprintRadius)
                .apply(sc2ApiAbilityData).orElse(nothing());

        instantPlacement = tryGet(Data.AbilityData::getIsInstantPlacement, Data.AbilityData::hasIsInstantPlacement)
                .apply(sc2ApiAbilityData).orElse(false);

        castRange = tryGet(Data.AbilityData::getCastRange, Data.AbilityData::hasCastRange)
                .apply(sc2ApiAbilityData).orElse(nothing());
    }

    public static AbilityData from(Data.AbilityData sc2ApiAbilityData) {
        require("sc2api ability data", sc2ApiAbilityData);
        return new AbilityData(sc2ApiAbilityData);
    }

    public Ability getAbility() {
        return ability;
    }

    public String getLinkName() {
        return linkName;
    }

    public int getLinkIndex() {
        return linkIndex;
    }

    public Optional<String> getButtonName() {
        return Optional.ofNullable(buttonName);
    }

    public Optional<String> getFriendlyName() {
        return Optional.ofNullable(friendlyName);
    }

    public Optional<String> getHotkey() {
        return Optional.ofNullable(hotkey);
    }

    public Optional<Integer> getRemapsToAbilityId() {
        return Optional.ofNullable(remapsToAbilityId);
    }

    public boolean isAvailable() {
        return available;
    }

    public Optional<Target> getTarget() {
        return Optional.ofNullable(target);
    }

    public boolean isAllowMinimap() {
        return allowMinimap;
    }

    public boolean isAllowAutocast() {
        return allowAutocast;
    }

    public boolean isBuilding() {
        return building;
    }

    public Optional<Float> getFootprintRadius() {
        return Optional.ofNullable(footprintRadius);
    }

    public boolean isInstantPlacement() {
        return instantPlacement;
    }

    public Optional<Float> getCastRange() {
        return Optional.ofNullable(castRange);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbilityData that = (AbilityData) o;

        return linkIndex == that.linkIndex &&
                available == that.available &&
                allowMinimap == that.allowMinimap &&
                allowAutocast == that.allowAutocast &&
                building == that.building &&
                instantPlacement == that.instantPlacement &&
                ability == that.ability &&
                linkName.equals(that.linkName) &&
                (buttonName != null ? buttonName.equals(that.buttonName) : that.buttonName == null) &&
                (friendlyName != null ? friendlyName.equals(that.friendlyName) : that.friendlyName == null) &&
                (hotkey != null ? hotkey.equals(that.hotkey) : that.hotkey == null) &&
                (remapsToAbilityId != null
                        ? remapsToAbilityId.equals(that.remapsToAbilityId)
                        : that.remapsToAbilityId == null) &&
                target == that.target &&
                (footprintRadius != null
                        ? footprintRadius.equals(that.footprintRadius)
                        : that.footprintRadius == null) &&
                (castRange != null ? castRange.equals(that.castRange) : that.castRange == null);
    }

    @Override
    public int hashCode() {
        int result = ability.hashCode();
        result = 31 * result + linkName.hashCode();
        result = 31 * result + linkIndex;
        result = 31 * result + (buttonName != null ? buttonName.hashCode() : 0);
        result = 31 * result + (friendlyName != null ? friendlyName.hashCode() : 0);
        result = 31 * result + (hotkey != null ? hotkey.hashCode() : 0);
        result = 31 * result + (remapsToAbilityId != null ? remapsToAbilityId.hashCode() : 0);
        result = 31 * result + (available ? 1 : 0);
        result = 31 * result + (target != null ? target.hashCode() : 0);
        result = 31 * result + (allowMinimap ? 1 : 0);
        result = 31 * result + (allowAutocast ? 1 : 0);
        result = 31 * result + (building ? 1 : 0);
        result = 31 * result + (footprintRadius != null ? footprintRadius.hashCode() : 0);
        result = 31 * result + (instantPlacement ? 1 : 0);
        result = 31 * result + (castRange != null ? castRange.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
