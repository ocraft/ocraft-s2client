package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import com.google.protobuf.ByteString;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.github.ocraft.s2client.protocol.Fixtures.DATA_IN_BYTES;
import static com.github.ocraft.s2client.protocol.Fixtures.LOCAL_MAP_PATH;
import static com.github.ocraft.s2client.protocol.request.RequestSaveMap.saveMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RequestSaveMapTest {

    @Test
    void serializesToSc2ApiRequestSaveMap() {
        assertThatAllFieldsInRequestAreSerialized(
                saveMap().to(LocalMap.of(Paths.get(LOCAL_MAP_PATH), DATA_IN_BYTES)).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Sc2Api.Request sc2ApiRequest) {
        assertThat(sc2ApiRequest.hasSaveMap()).as("sc2api request has save map").isTrue();

        Sc2Api.RequestSaveMap sc2ApiRequestSaveMap = sc2ApiRequest.getSaveMap();
        assertThat(sc2ApiRequestSaveMap.getMapData()).as("sc2api save map: map data")
                .isEqualTo(ByteString.copyFrom(DATA_IN_BYTES));
        assertThat(sc2ApiRequestSaveMap.getMapPath()).as("sc2api save map: map path").isEqualTo(LOCAL_MAP_PATH);
    }

    @Test
    void throwsExceptionWhenMapIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> saveMap().build())
                .withMessage("map is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestSaveMap.class).withIgnoredFields("nanoTime").withNonnullFields("map").verify();
    }
}