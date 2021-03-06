package com.github.ocraft.s2client.protocol.game.raw;

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
import com.google.protobuf.ByteString;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiStartRaw;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class StartRawTest {
    @Test
    void throwsExceptionWhenSc2ApiStartRawIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> StartRaw.from(nothing()))
                .withMessage("sc2api start raw is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiStartRaw() {
        assertThatAllFieldsAreConverted(StartRaw.from(sc2ApiStartRaw()));
    }

    private void assertThatAllFieldsAreConverted(StartRaw startRaw) {
        assertThat(startRaw.getMapSize()).as("start raw: map size").isNotNull();
        assertThat(startRaw.getPathingGrid()).as("start raw: pathing grid").isNotNull();
        assertThat(startRaw.getTerrainHeight()).as("start raw: terrain height").isNotNull();
        assertThat(startRaw.getPlacementGrid()).as("start raw: placement grid").isNotNull();
        assertThat(startRaw.getPlayableArea()).as("start raw: playable area").isNotNull();
        assertThat(startRaw.getStartLocations()).as("start raw: start locations").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenMapSizeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> StartRaw.from(
                        without(() -> sc2ApiStartRaw().toBuilder(), Raw.StartRaw.Builder::clearMapSize).build()))
                .withMessage("map size is required");
    }

    @Test
    void throwsExceptionWhenPathingGridIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> StartRaw.from(
                        without(() -> sc2ApiStartRaw().toBuilder(), Raw.StartRaw.Builder::clearPathingGrid).build()))
                .withMessage("pathing grid is required");
    }

    @Test
    void throwsExceptionWhenTerrainHeightIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> StartRaw.from(
                        without(() -> sc2ApiStartRaw().toBuilder(), Raw.StartRaw.Builder::clearTerrainHeight).build()))
                .withMessage("terrain height is required");
    }

    @Test
    void throwsExceptionWhenPlacementGridIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> StartRaw.from(
                        without(() -> sc2ApiStartRaw().toBuilder(), Raw.StartRaw.Builder::clearPlacementGrid).build()))
                .withMessage("placement grid is required");
    }

    @Test
    void throwsExceptionWhenPlayableAreaIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> StartRaw.from(
                        without(() -> sc2ApiStartRaw().toBuilder(), Raw.StartRaw.Builder::clearPlayableArea).build()))
                .withMessage("playable area is required");
    }

    @Test
    void fulfillsEqualsContract() throws UnsupportedEncodingException {
        EqualsVerifier
                .forClass(StartRaw.class)
                .withPrefabValues(
                        ByteString.class,
                        ByteString.copyFrom("test", "UTF-8"),
                        ByteString.copyFrom("test2", "UTF-8"))
                .withNonnullFields("mapSize", "pathingGrid", "terrainHeight", "placementGrid", "playableArea",
                        "startLocations")
                .verify();
    }
}
