package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Common;
import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.game.BattlenetMap;
import com.github.ocraft.s2client.protocol.game.Difficulty;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import com.github.ocraft.s2client.protocol.game.Race;
import com.google.protobuf.ByteString;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.game.ComputerPlayerSetup.computer;
import static com.github.ocraft.s2client.protocol.game.PlayerSetup.observer;
import static com.github.ocraft.s2client.protocol.game.PlayerSetup.participant;
import static com.github.ocraft.s2client.protocol.request.RequestCreateGame.createGame;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RequestCreateGameTest {

    @Test
    void serializesToSc2ApiRequestCreateGame() {
        assertThatAllFieldsInRequestAreSerialized(
                createGame()
                        .onBattlenetMap(BattlenetMap.of(BATTLENET_MAP_NAME))
                        .withPlayerSetup(observer(), participant(), computer(Race.PROTOSS, Difficulty.MEDIUM))
                        .disableFog()
                        .realTime()
                        .withRandomSeed(RANDOM_SEED)
                        .build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Sc2Api.Request sc2ApiRequest) {
        assertThat(sc2ApiRequest.hasCreateGame()).as("sc2api request has create game").isTrue();

        Sc2Api.RequestCreateGame sc2ApiRequestCreateGame = sc2ApiRequest.getCreateGame();
        assertThat(sc2ApiRequestCreateGame.getDisableFog()).as("disable fog attribute").isTrue();
        assertThat(sc2ApiRequestCreateGame.getRealtime()).as("real time attribute").isTrue();
        assertThat(sc2ApiRequestCreateGame.getRandomSeed()).as("random seed attribute").isEqualTo(RANDOM_SEED);
        assertThat(sc2ApiRequestCreateGame.getPlayerSetupList()).as("player setup").containsExactlyInAnyOrder(
                aSc2ApiPlayerSetup(Sc2Api.PlayerType.Observer).build(),
                aSc2ApiPlayerSetup(Sc2Api.PlayerType.Participant).build(),
                aSc2ApiPlayerSetup(Sc2Api.PlayerType.Computer)
                        .setRace(Common.Race.Protoss).setDifficulty(Sc2Api.Difficulty.Medium).build());
        assertThat(sc2ApiRequestCreateGame.getBattlenetMapName()).as("battlenet map name")
                .isEqualTo(BATTLENET_MAP_NAME);
    }

    private Sc2Api.PlayerSetup.Builder aSc2ApiPlayerSetup(Sc2Api.PlayerType playerType) {
        return Sc2Api.PlayerSetup.newBuilder().setType(playerType);
    }

    @Test
    void doesNotSerializeRandomSeedIfNotSet() {
        assertThat(requestCreateGameWithDefaultValues().hasRandomSeed()).as("random seed is not set").isFalse();
    }

    private Sc2Api.RequestCreateGame requestCreateGameWithDefaultValues() {
        return createGame()
                .onBattlenetMap(BattlenetMap.of(BATTLENET_MAP_NAME))
                .withPlayerSetup(observer())
                .build().toSc2Api().getCreateGame();
    }

    @Test
    void serializesDefaultValueForDisableFogWhenNotSet() {
        assertThat(requestCreateGameWithDefaultValues().getDisableFog()).as("disable fog default value").isFalse();
    }

    @Test
    void serializesDefaultValueForRealTimeWhenNotSet() {
        assertThat(requestCreateGameWithDefaultValues().getRealtime()).as("real time default value").isFalse();
    }

    @Test
    void serializesOnlyRecentlyAddedMapData() {
        assertCorrectMap(fullAccessTo(createGame().onBattlenetMap(BattlenetMap.of(BATTLENET_MAP_NAME)))
                .onLocalMap(LocalMap.of(Paths.get(LOCAL_MAP_PATH), DATA_IN_BYTES))
                .withPlayerSetup(observer())
                .build().toSc2Api().getCreateGame());

        assertCorrectMapAfterOrderChange(
                fullAccessTo(createGame().onLocalMap(LocalMap.of(Paths.get(LOCAL_MAP_PATH), DATA_IN_BYTES)))
                        .onBattlenetMap(BattlenetMap.of(BATTLENET_MAP_NAME))
                        .withPlayerSetup(observer())
                        .build().toSc2Api().getCreateGame());
    }

    private RequestCreateGame.Builder fullAccessTo(Object obj) {
        return (RequestCreateGame.Builder) obj;
    }

    private void assertCorrectMap(Sc2Api.RequestCreateGame sc2ApiRequestCreateGame) {
        assertThat(sc2ApiRequestCreateGame.hasBattlenetMapName()).as("case of map is battlenet").isFalse();
        assertThat(sc2ApiRequestCreateGame.hasLocalMap()).as("case of map is local map").isTrue();
        assertThat(sc2ApiRequestCreateGame.getLocalMap()).as("local map").isEqualTo(expectedSc2ApiLocalMap());
    }

    private Sc2Api.LocalMap expectedSc2ApiLocalMap() {
        return Sc2Api.LocalMap.newBuilder()
                .setMapData(ByteString.copyFrom(DATA_IN_BYTES))
                .setMapPath(LOCAL_MAP_PATH)
                .build();
    }

    private void assertCorrectMapAfterOrderChange(Sc2Api.RequestCreateGame sc2ApiRequestCreateGame) {
        assertThat(sc2ApiRequestCreateGame.hasBattlenetMapName()).as("case of map is battlenet").isTrue();
        assertThat(sc2ApiRequestCreateGame.hasLocalMap()).as("case of map is local map").isFalse();
        assertThat(sc2ApiRequestCreateGame.getBattlenetMapName()).as("battlenet map name")
                .isEqualTo(BATTLENET_MAP_NAME);
    }

    @Test
    void throwsExceptionWhenMapDataIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(createGame()).build())
                .withMessage("one of map data is required");
    }

    @Test
    void throwsExceptionWhenPlayerSetupIsEmpty() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> createGame().onBattlenetMap(BattlenetMap.of(BATTLENET_MAP_NAME)).build())
                .withMessage("player setup is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestCreateGame.class)
                .withNonnullFields("playerSetups").withIgnoredFields("nanoTime").verify();
    }

}