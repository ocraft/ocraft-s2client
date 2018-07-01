package com.github.ocraft.s2client.protocol.query;

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
