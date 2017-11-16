package com.github.ocraft.s2client.protocol.query;

import SC2APIProtocol.Query;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.action.ActionResult;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class BuildingPlacement implements Serializable {

    private static final long serialVersionUID = 4314835200008321718L;

    private final ActionResult result;

    private BuildingPlacement(Query.ResponseQueryBuildingPlacement sc2ApiResponseQueryBuildingPlacement) {
        result = tryGet(
                Query.ResponseQueryBuildingPlacement::getResult, Query.ResponseQueryBuildingPlacement::hasResult
        ).apply(sc2ApiResponseQueryBuildingPlacement).map(ActionResult::from).orElseThrow(required("result"));
    }

    public static BuildingPlacement from(Query.ResponseQueryBuildingPlacement sc2ApiResponseQueryBuildingPlacement) {
        require("sc2api response query building placement", sc2ApiResponseQueryBuildingPlacement);
        return new BuildingPlacement(sc2ApiResponseQueryBuildingPlacement);
    }

    public ActionResult getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BuildingPlacement that = (BuildingPlacement) o;

        return result == that.result;
    }

    @Override
    public int hashCode() {
        return result.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
