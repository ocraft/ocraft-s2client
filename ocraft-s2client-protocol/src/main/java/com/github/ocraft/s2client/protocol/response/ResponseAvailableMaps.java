package com.github.ocraft.s2client.protocol.response;

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

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.BattlenetMap;
import com.github.ocraft.s2client.protocol.game.GameStatus;
import com.github.ocraft.s2client.protocol.game.LocalMap;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

public final class ResponseAvailableMaps extends Response {

    private static final long serialVersionUID = -8376471026942730289L;

    private final Set<BattlenetMap> battlenetMaps;
    private final Set<LocalMap> localMaps;

    private ResponseAvailableMaps(
            Sc2Api.ResponseAvailableMaps sc2ApiResponseAvailableMaps, Sc2Api.Status sc2ApiStatus) {
        super(ResponseType.AVAILABLE_MAPS, GameStatus.from(sc2ApiStatus));

        this.battlenetMaps = new HashSet<>(sc2ApiResponseAvailableMaps.getBattlenetMapNamesList())
                .stream()
                .map(BattlenetMap::of)
                .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));

        this.localMaps = sc2ApiResponseAvailableMaps
                .getLocalMapPathsList().stream()
                .map(Paths::get)
                .map(LocalMap::of)
                .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
    }

    public static ResponseAvailableMaps from(Sc2Api.Response sc2ApiResponse) {
        if (!hasAvailableMapsResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have available maps response");
        }
        return new ResponseAvailableMaps(sc2ApiResponse.getAvailableMaps(), sc2ApiResponse.getStatus());
    }

    private static boolean hasAvailableMapsResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasAvailableMaps();
    }

    public Set<BattlenetMap> getBattlenetMaps() {
        return this.battlenetMaps;
    }

    public Set<LocalMap> getLocalMaps() {
        return this.localMaps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseAvailableMaps)) return false;
        if (!super.equals(o)) return false;

        ResponseAvailableMaps that = (ResponseAvailableMaps) o;

        return that.canEqual(this) &&
                battlenetMaps.equals(that.battlenetMaps) &&
                localMaps.equals(that.localMaps);
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseAvailableMaps;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + battlenetMaps.hashCode();
        result = 31 * result + localMaps.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
