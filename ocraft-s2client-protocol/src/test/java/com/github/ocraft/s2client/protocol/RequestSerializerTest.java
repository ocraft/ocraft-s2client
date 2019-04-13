package com.github.ocraft.s2client.protocol;

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
import com.github.ocraft.s2client.protocol.request.RequestPing;
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
        RequestPing ping = ping();
        Sc2Api.Request initialRequest = Sc2Api.Request.newBuilder()
                .setPing(Sc2Api.RequestPing.newBuilder().build())
                .setId(ping.getId())
                .build();

        byte[] serializedRequest = new RequestSerializer().apply(ping);

        assertThat(serializedRequest).as("serialized request").isNotEmpty();
        assertThat(Sc2Api.Request.parseFrom(serializedRequest)).isEqualTo(initialRequest);
    }

}
