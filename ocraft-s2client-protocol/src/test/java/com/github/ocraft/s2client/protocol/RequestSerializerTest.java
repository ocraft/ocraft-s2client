package com.github.ocraft.s2client.protocol;

import SC2APIProtocol.Sc2Api;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.request.Requests.ping;
import static org.assertj.core.api.Assertions.assertThat;

class RequestSerializerTest {

    private final static byte[] EMPTY_ARRAY = new byte[0];

    @Test
    void returnsEmptyArrayForNullInput() {
        assertThat(new RequestSerializer().apply(nothing())).as("serialized request").isEqualTo(EMPTY_ARRAY);
    }

    @Test
    void serializesSc2ApiRequestToBytes() throws Exception {
        Sc2Api.Request initialRequest = Sc2Api.Request.newBuilder()
                .setPing(Sc2Api.RequestPing.newBuilder().build())
                .build();

        byte[] serializedRequest = new RequestSerializer().apply(ping());

        assertThat(serializedRequest).as("serialized request").isNotEmpty();
        assertThat(Sc2Api.Request.parseFrom(serializedRequest)).isEqualTo(initialRequest);
    }

}