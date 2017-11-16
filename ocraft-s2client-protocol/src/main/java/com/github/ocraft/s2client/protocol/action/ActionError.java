package com.github.ocraft.s2client.protocol.action;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.unit.Tag;

import java.io.Serializable;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ActionError implements Serializable {

    private static final long serialVersionUID = 2108256201808064777L;

    private final Tag unitTag;
    private final Ability ability;
    private final ActionResult actionResult;

    private ActionError(Sc2Api.ActionError sc2ApiActionError) {
        this.unitTag = tryGet(
                Sc2Api.ActionError::getUnitTag, Sc2Api.ActionError::hasUnitTag
        ).apply(sc2ApiActionError).map(Tag::from).orElse(nothing());

        this.ability = tryGet(
                Sc2Api.ActionError::getAbilityId, Sc2Api.ActionError::hasAbilityId
        ).apply(sc2ApiActionError).map(Long::intValue).map(Abilities::from).orElse(nothing());

        this.actionResult = tryGet(
                Sc2Api.ActionError::getResult, Sc2Api.ActionError::hasResult
        ).apply(sc2ApiActionError).map(ActionResult::from).orElseThrow(required("action result"));
    }

    public static ActionError from(Sc2Api.ActionError sc2ApiActionError) {
        require("sc2api action error", sc2ApiActionError);
        return new ActionError(sc2ApiActionError);
    }

    public Optional<Tag> getUnitTag() {
        return Optional.ofNullable(unitTag);
    }

    public Optional<Ability> getAbility() {
        return Optional.ofNullable(ability);
    }

    public ActionResult getActionResult() {
        return actionResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionError that = (ActionError) o;

        return (unitTag != null ? unitTag.equals(that.unitTag) : that.unitTag == null) &&
                ability == that.ability &&
                actionResult == that.actionResult;
    }

    @Override
    public int hashCode() {
        int result = unitTag != null ? unitTag.hashCode() : 0;
        result = 31 * result + (ability != null ? ability.hashCode() : 0);
        result = 31 * result + actionResult.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
