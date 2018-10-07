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
import com.github.ocraft.s2client.protocol.game.GameStatus;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.spatial.PointI;
import com.google.protobuf.ByteString;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseGameInfoTest {
    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveGameInfo() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseGameInfo.from(nothing()))
                .withMessage("provided argument doesn't have game info response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseGameInfo.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have game info response");
    }

    @Test
    void throwsExceptionWhenMapNameIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseGameInfo.from(
                        sc2ApiGameInfoWithout(Sc2Api.ResponseGameInfo.Builder::clearMapName)))
                .withMessage("map name is required");
    }

    private Sc2Api.Response sc2ApiGameInfoWithout(Consumer<Sc2Api.ResponseGameInfo.Builder> clear) {
        return Sc2Api.Response.newBuilder()
                .setGameInfo(without(() -> sc2ApiResponseGameInfo().toBuilder(), clear)).build();
    }

    @Test
    void throwsExceptionWhenInterfaceOptionsIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseGameInfo.from(
                        sc2ApiGameInfoWithout(Sc2Api.ResponseGameInfo.Builder::clearOptions)))
                .withMessage("interface options is required");
    }

    @Test
    void throwsExceptionWhenPlayersInfoIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseGameInfo.from(
                        sc2ApiGameInfoWithout(Sc2Api.ResponseGameInfo.Builder::clearPlayerInfo)))
                .withMessage("players info is required");
    }

    @Test
    void convertsSc2ApiResponseGameInfoToResponseGameInfo() {
        assertThatAllFieldsAreProperlyConverted(ResponseGameInfo.from(sc2ApiResponseWithGameInfo()));
    }

    private void assertThatAllFieldsAreProperlyConverted(ResponseGameInfo responseGameInfo) {
        assertThat(responseGameInfo.getType()).as("game info: type").isEqualTo(ResponseType.GAME_INFO);
        assertThat(responseGameInfo.getStatus()).as("game info: status").isEqualTo(GameStatus.IN_GAME);
        assertThat(responseGameInfo.getMapName()).as("game info: map name").isEqualTo(BATTLENET_MAP_NAME);
        assertThat(responseGameInfo.getModNames()).as("game info: mod names").containsExactlyInAnyOrder(MOD_NAME);
        assertThat(responseGameInfo.getLocalMap()).as("game info: local map").isNotEmpty();
        assertThat(responseGameInfo.getPlayersInfo()).as("game info: players info").isNotEmpty();
        assertThat(responseGameInfo.getStartRaw()).as("game info: start raw").isNotEmpty();
        assertThat(responseGameInfo.getInterfaceOptions()).as("game info: interface options").isNotNull();
    }

    @Test
    void hasEmptyModNamesSetIfNotProvided() {
        assertThat(ResponseGameInfo.from(
                sc2ApiGameInfoWithout(Sc2Api.ResponseGameInfo.Builder::clearModNames)).getModNames())
                .as("game info: default mod names").isEmpty();
    }

    @Test
    void convertsWorldPositionToMinimapPosition() {
        assertThat(ResponseGameInfo.from(sc2ApiResponseWithGameInfo()).convertWorldToMinimap(Point2d.of(5, 7)))
                .isEqualTo(PointI.of(5, 57));
    }

    @Test
    void convertsWorldPositionToCameraPosition() {
        assertThat(ResponseGameInfo.from(sc2ApiResponseWithGameInfo())
                .convertWorldToCamera(Point2d.of(10, 20), Point2d.of(5, 7)))
                .isEqualTo(PointI.of(18, 66));
    }

    @Test
    void findsRandomLocationOnPlayableArea() {
        assertThat(ResponseGameInfo.from(sc2ApiResponseWithGameInfo()).findRandomLocation()).isNotNull();
    }

    @Test
    void findsCenterOfMap() {
        assertThat(ResponseGameInfo.from(sc2ApiResponseWithGameInfo()).findCenterOfMap()).isNotNull();
    }

    @Test
    void fulfillsEqualsContract() throws UnsupportedEncodingException {
        EqualsVerifier
                .forClass(ResponseGameInfo.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status", "mapName", "modNames", "playersInfo", "interfaceOptions")
                .withRedefinedSuperclass()
                .withPrefabValues(
                        ByteString.class,
                        ByteString.copyFrom("test", "UTF-8"),
                        ByteString.copyFrom("test2", "UTF-8"))
                .verify();
    }
}
