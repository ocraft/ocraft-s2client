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
import com.github.ocraft.s2client.protocol.debug.DebugGameState;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.debug.DebugCommand.command;
import static com.github.ocraft.s2client.protocol.request.RequestDebug.debug;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RequestDebugTest {
    @Test
    void serializesToSc2ApiRequestDebug() {
        assertThatAllFieldsInRequestAreSerialized(
                debug().with(command().of(DebugGameState.SHOW_MAP)).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Sc2Api.Request sc2ApiRequest) {
        assertThat(sc2ApiRequest.hasDebug()).as("sc2api request has debug").isTrue();

        Sc2Api.RequestDebug sc2ApiRequestDebug = sc2ApiRequest.getDebug();
        assertThat(sc2ApiRequestDebug.getDebugList()).as("sc2api debug: command list").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenMapIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((RequestDebug.Builder) debug()).build())
                .withMessage("command list is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestDebug.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("commands")
                .withRedefinedSuperclass()
                .verify();
    }
}
