package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.game.InterfaceOptions;
import com.google.protobuf.ByteString;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Defaults.defaultInterfaces;
import static com.github.ocraft.s2client.protocol.Defaults.defaultSpatialSetup;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.game.InterfaceOptions.interfaces;
import static com.github.ocraft.s2client.protocol.request.RequestStartReplay.startReplay;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RequestStartReplayTest {

    @Test
    void serializesToSc2ApiRequestStartReplay() {
        assertThatAllFieldsInRequestAreSerialized(startReplay()
                .from(Paths.get(REPLAY_PATH))
                .overrideMapData(DATA_IN_BYTES)
                .use(interfaces().raw().score()
                        .featureLayer(defaultSpatialSetup())
                        .render(defaultSpatialSetup())
                        .build())
                .toObserve(PLAYER_ID)
                .disableFog()
                .realtime()
                .build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Sc2Api.Request sc2ApiRequest) {
        assertThat(sc2ApiRequest.hasStartReplay()).as("sc2api request has start replay").isTrue();
        Sc2Api.RequestStartReplay sc2ApiRequestStartReplay = sc2ApiRequest.getStartReplay();
        assertThat(sc2ApiRequestStartReplay.hasReplayPath()).as("case of replay case is path").isTrue();
        assertThat(sc2ApiRequestStartReplay.getReplayPath()).as("path of replay").isEqualTo(REPLAY_PATH);
        assertThat(sc2ApiRequestStartReplay.getMapData()).as("override map data")
                .isEqualTo(ByteString.copyFrom(DATA_IN_BYTES));
        assertThat(sc2ApiRequestStartReplay.getObservedPlayerId()).as("observed player id").isEqualTo(PLAYER_ID);
        assertThat(sc2ApiRequestStartReplay.hasOptions()).as("interface option is set").isTrue();
        assertThat(sc2ApiRequestStartReplay.getDisableFog()).as("disable fog is set").isTrue();
        assertThat(sc2ApiRequestStartReplay.getRealtime()).as("realtime is set").isTrue();
    }

    @Test
    void serializesToSc2ApiRequestStartReplayUsingBuilders() {
        assertThatAllFieldsInRequestAreSerialized(startReplay()
                .from(Paths.get(REPLAY_PATH))
                .overrideMapData(DATA_IN_BYTES)
                .use(interfaces().raw().score()
                        .featureLayer(defaultSpatialSetup())
                        .render(defaultSpatialSetup()))
                .toObserve(PLAYER_ID)
                .disableFog()
                .realtime()
                .build().toSc2Api());
    }

    @Test
    void serializesOnlyRecentlyAddedReplayCase() {
        assertCorrectReplayCase(fullAccessTo(startReplay().from(Paths.get(REPLAY_PATH)))
                .from(REPLAY_DATA_IN_BYTES)
                .use(defaultInterfaces())
                .toObserve(PLAYER_ID)
                .build().toSc2Api().getStartReplay());

        assertCorrectReplayCaseAfterOrderChange(fullAccessTo(startReplay().from(REPLAY_DATA_IN_BYTES))
                .from(Paths.get(REPLAY_PATH))
                .use(defaultInterfaces())
                .toObserve(PLAYER_ID)
                .build().toSc2Api().getStartReplay());
    }

    private RequestStartReplay.Builder fullAccessTo(Object obj) {
        return (RequestStartReplay.Builder) obj;
    }

    private void assertCorrectReplayCase(Sc2Api.RequestStartReplay sc2ApiRequestStartReplay) {
        assertThat(sc2ApiRequestStartReplay.hasReplayPath()).as("replay path is the case").isFalse();
        assertThat(sc2ApiRequestStartReplay.hasReplayData()).as("replay data is the case").isTrue();
        assertThat(sc2ApiRequestStartReplay.getReplayData()).as("replay data in bytes")
                .isEqualTo(ByteString.copyFrom(REPLAY_DATA_IN_BYTES));
    }

    private void assertCorrectReplayCaseAfterOrderChange(Sc2Api.RequestStartReplay sc2ApiRequestStartReplay) {
        assertThat(sc2ApiRequestStartReplay.hasReplayPath()).as("replay path is the case").isTrue();
        assertThat(sc2ApiRequestStartReplay.hasReplayData()).as("replay data is the case").isFalse();
        assertThat(sc2ApiRequestStartReplay.getReplayPath()).as("path of replay").isEqualTo(REPLAY_PATH);
    }

    @Test
    void throwsExceptionWhenReplayCaseIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(startReplay()).build())
                .withMessage("one of replay case is required");
    }

    @Test
    void throwsExceptionIfInterfaceOptionsAreNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(aRequestStartReplay().use((InterfaceOptions) nothing())).build())
                .withMessage("interface option is required");
    }

    private RequestStartReplay.Builder aRequestStartReplay() {
        return fullAccessTo(startReplay().from(Paths.get(REPLAY_PATH)).use(defaultInterfaces()).toObserve(PLAYER_ID));
    }

    @Test
    void throwsExceptionWhenObservedPlayerIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(
                        () -> fullAccessTo(startReplay().from(Paths.get(REPLAY_PATH)).use(defaultInterfaces())).build())
                .withMessage("observed player is required");
    }

    @Test
    void serializesDefaultDisableFogValueIfNotSet() {
        Sc2Api.RequestStartReplay aSc2ApiRequestStartReplay = aRequestStartReplay().build().toSc2Api().getStartReplay();
        assertThat(aSc2ApiRequestStartReplay.hasDisableFog()).as("disable fog value is set").isTrue();
        assertThat(aSc2ApiRequestStartReplay.getDisableFog()).as("default disable fog value is set").isFalse();
    }

    @Test
    void serializesDefaultRealtimeValueIfNotSet() {
        Sc2Api.RequestStartReplay aSc2ApiRequestStartReplay = aRequestStartReplay().build().toSc2Api().getStartReplay();
        assertThat(aSc2ApiRequestStartReplay.hasRealtime()).as("realtime value is set").isTrue();
        assertThat(aSc2ApiRequestStartReplay.getRealtime()).as("default realtime value is set").isFalse();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestStartReplay.class)
                .withIgnoredFields("nanoTime").withNonnullFields("interfaceOptions").verify();
    }

}