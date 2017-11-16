/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
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
 */
package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Fixtures.rawUnitCommand;
import static com.github.ocraft.s2client.protocol.action.Action.action;
import static com.github.ocraft.s2client.protocol.request.RequestAction.actions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RequestActionTest {

    @Test
    void serializesToSc2ApiRequestAction() {
        assertThatAllFieldsInRequestAreSerialized(
                actions().of(action().raw(rawUnitCommand().build()).build()).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Sc2Api.Request sc2ApiRequest) {
        assertThat(sc2ApiRequest.hasAction()).as("sc2api request has action").isTrue();

        Sc2Api.RequestAction sc2ApiRequestAction = sc2ApiRequest.getAction();
        assertThat(sc2ApiRequestAction.getActionsList()).as("sc2api request action: action list").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenActionListIsEmpty() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((RequestAction.Builder) actions()).build())
                .withMessage("action list is required");
    }

    @Test
    void serializesToSc2ApiRequestActionUsingBuilder() {
        assertThat(actions().of(action().raw(rawUnitCommand())).build().toSc2Api().hasAction())
                .as("(builder ver.) sc2api request has action").isTrue();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestAction.class).withNonnullFields("actions").withIgnoredFields("nanoTime").verify();
    }
}