package com.github.ocraft.s2client.protocol.request;

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
import com.google.protobuf.ByteString;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.github.ocraft.s2client.protocol.Fixtures.REPLAY_DATA_IN_BYTES;
import static com.github.ocraft.s2client.protocol.Fixtures.REPLAY_PATH;
import static com.github.ocraft.s2client.protocol.request.RequestReplayInfo.replayInfo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RequestReplayInfoTest {

    @Test
    void serializesToSc2ApiRequestReplayInfo() {
        Sc2Api.Request sc2ApiRequest = replayInfo()
                .of(Paths.get(REPLAY_PATH))
                .download()
                .build().toSc2Api();

        assertThat(sc2ApiRequest.hasReplayInfo()).as("sc2api request has replay info").isTrue();

        Sc2Api.RequestReplayInfo sc2ApiRequestReplayInfo = sc2ApiRequest.getReplayInfo();

        assertThat(sc2ApiRequestReplayInfo.hasReplayPath()).as("case of replay case is path").isTrue();
        assertThat(sc2ApiRequestReplayInfo.getReplayPath()).as("path of replay").isEqualTo(REPLAY_PATH);
        assertThat(sc2ApiRequestReplayInfo.getDownloadData()).as("download data is set").isTrue();
    }

    @Test
    void serializesOnlyRecentlyAddedReplayCase() {
        assertCorrectReplayCase(fullAccessTo(replayInfo().of(Paths.get(REPLAY_PATH)))
                .of(REPLAY_DATA_IN_BYTES)
                .build().toSc2Api().getReplayInfo());

        assertCorrectReplayCaseAfterOrderChange(fullAccessTo(replayInfo().of(REPLAY_DATA_IN_BYTES))
                .of(Paths.get(REPLAY_PATH))
                .build().toSc2Api().getReplayInfo());
    }

    private RequestReplayInfo.Builder fullAccessTo(Object obj) {
        return (RequestReplayInfo.Builder) obj;
    }

    private void assertCorrectReplayCase(Sc2Api.RequestReplayInfo sc2ApiRequestReplayInfo) {
        assertThat(sc2ApiRequestReplayInfo.hasReplayPath()).as("replay path is the case").isFalse();
        assertThat(sc2ApiRequestReplayInfo.hasReplayData()).as("replay data is the case").isTrue();
        assertThat(sc2ApiRequestReplayInfo.getReplayData()).as("replay data in bytes")
                .isEqualTo(ByteString.copyFrom(REPLAY_DATA_IN_BYTES));
    }

    private void assertCorrectReplayCaseAfterOrderChange(Sc2Api.RequestReplayInfo sc2ApiRequestReplayInfo) {
        assertThat(sc2ApiRequestReplayInfo.hasReplayPath()).as("replay path is the case").isTrue();
        assertThat(sc2ApiRequestReplayInfo.hasReplayData()).as("replay data is the case").isFalse();
        assertThat(sc2ApiRequestReplayInfo.getReplayPath()).as("path of replay").isEqualTo(REPLAY_PATH);
    }

    @Test
    void throwsExceptionWhenReplayCaseIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(replayInfo()).build())
                .withMessage("replay case is required");
    }

    @Test
    void serializesDefaultDownloadDataValueIfNotSet() {
        Sc2Api.RequestReplayInfo aSc2ApiRequestReplayInfo = replayInfo()
                .of(Paths.get(REPLAY_PATH))
                .build().toSc2Api().getReplayInfo();
        assertThat(aSc2ApiRequestReplayInfo.hasDownloadData()).as("download data value is set").isTrue();
        assertThat(aSc2ApiRequestReplayInfo.getDownloadData()).as("default download data value is set").isFalse();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestReplayInfo.class).withIgnoredFields("nanoTime").verify();
    }
}
