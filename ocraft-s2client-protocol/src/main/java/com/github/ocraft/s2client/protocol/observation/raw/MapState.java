package com.github.ocraft.s2client.protocol.observation.raw;

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

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.observation.spatial.ImageData;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class MapState implements Serializable {

    private static final long serialVersionUID = -4361255173604238600L;

    private final ImageData visibility;

    private final ImageData creep; /** 1 bit creep layer. */

    private MapState(Raw.MapState sc2ApiMapState) {
        visibility = tryGet(
                Raw.MapState::getVisibility, Raw.MapState::hasVisibility
        ).apply(sc2ApiMapState).map(ImageData::from).orElseThrow(required("visibility"));

        creep = tryGet(
                Raw.MapState::getCreep, Raw.MapState::hasCreep
        ).apply(sc2ApiMapState).map(ImageData::from).orElseThrow(required("creep"));
    }

    public static MapState from(Raw.MapState sc2ApiMapState) {
        require("sc2api map state", sc2ApiMapState);
        return new MapState(sc2ApiMapState);
    }

    public ImageData getVisibility() {
        return visibility;
    }

    public ImageData getCreep() {
        return creep;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapState mapState = (MapState) o;

        return visibility.equals(mapState.visibility) && creep.equals(mapState.creep);
    }

    @Override
    public int hashCode() {
        int result = visibility.hashCode();
        result = 31 * result + creep.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
