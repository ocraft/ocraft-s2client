package com.github.ocraft.s2client.protocol.game;

import SC2APIProtocol.Sc2Api;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ReplayInfoTest {

    @Test
    void throwsExceptionWhenSc2ApiResponseReplayInfoIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ReplayInfo.from(nothing()))
                .withMessage("sc2api response replay info is required");
    }

    @Test
    void convertsSc2ApiReplayInfoToReplayInfo() {
        assertThatAllFieldsAreMapped(ReplayInfo.from(sc2ApiResponseReplayInfo()));
    }

    private void assertThatAllFieldsAreMapped(ReplayInfo replayInfo) {
        assertThat(replayInfo.getBattlenetMap()).as("replay info: battlenet map")
                .hasValue(BattlenetMap.of(BATTLENET_MAP_NAME));
        assertThat(replayInfo.getLocalMap()).as("replay info: local map")
                .hasValue(LocalMap.of(Paths.get(LOCAL_MAP_PATH)));
        assertThat(replayInfo.getPlayerInfo()).as("replay info: player info").isNotEmpty();
        assertThat(replayInfo.getGameDurationLoops()).as("replay info: game duration loops")
                .isEqualTo(GAME_DURATION_LOOPS);
        assertThat(replayInfo.getGameDurationSeconds()).as("replay info: game duration seconds")
                .isEqualTo(GAME_DURATION_SECONDS);
        assertThat(replayInfo.getGameVersion()).as("replay info: game version").isEqualTo(GAME_VERSION);
        assertThat(replayInfo.getDataVersion()).as("replay info: data version").isEqualTo(DATA_VERSION);
        assertThat(replayInfo.getDataBuild()).as("replay info: data build").isEqualTo(DATA_BUILD);
        assertThat(replayInfo.getBaseBuild()).as("replay info: base build").isEqualTo(BASE_BUILD);
    }

    @Test
    void throwsExceptionWhenMapInfoIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ReplayInfo.from(sc2ApiReplayInfoWithoutMap()))
                .withMessage("map info (local or battlenet) is required");
    }

    private Sc2Api.ResponseReplayInfo sc2ApiReplayInfoWithoutMap() {
        return without(
                () -> sc2ApiResponseReplayInfo().toBuilder(),
                Sc2Api.ResponseReplayInfo.Builder::clearLocalMapPath,
                Sc2Api.ResponseReplayInfo.Builder::clearMapName).build();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ReplayInfo.class)
                .withNonnullFields("playerInfo", "gameVersion", "dataVersion")
                .withRedefinedSuperclass()
                .verify();
    }
}