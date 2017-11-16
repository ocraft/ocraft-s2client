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
package com.github.ocraft.s2client.protocol.response;

import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseConverterTest {

    @Test
    void throwsNullPointerExceptionForNullArgument() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new ResponseConverter().apply(nothing()))
                .withMessage("sc2api response is required");
    }

    @Test
    void throwsAssertionErrorForUnknownSc2ApiResponse() {
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> new ResponseConverter().apply(emptySc2ApiResponse()))
                .withMessage("unknown response");
    }

    @Test
    void createsResponseErrorWhenSc2ApiResponseHasErrorMessages() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithError())).isInstanceOf(ResponseError.class);
    }

    @Test
    void createsResponsePingWhenSc2ApiResponseHasPing() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithPing()))
                .as("creates ResponsePing instance from sc2api response")
                .isInstanceOf(ResponsePing.class);
    }

    @Test
    void createsResponseAvailableMapsWhenSc2ApiResponseHasAvailableMaps() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithAvailableMaps()))
                .as("creates ResponseAvailableMaps instance from sc2api response")
                .isInstanceOf(ResponseAvailableMaps.class);
    }

    @Test
    void createsResponseCreateGameWhenSc2ApiResponseHasCreateGame() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithCreateGame()))
                .as("creates ResponseCreateGame instance from sc2api response")
                .isInstanceOf(ResponseCreateGame.class);
    }

    @Test
    void createsResponseJoinGameWhenSc2ApiResponseHasJoinGame() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithJoinGame()))
                .as("creates ResponseJoinGame instance from sc2api response")
                .isInstanceOf(ResponseJoinGame.class);
    }

    @Test
    void createsResponseLeaveGameWhenSc2ApiResponseHasLeaveGame() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithLeaveGame()))
                .as("creates ResponseLeaveGame instance from sc2api response")
                .isInstanceOf(ResponseLeaveGame.class);
    }

    @Test
    void createsResponseRestartGameWhenSc2ApiResponseHasRestartGame() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithRestartGame()))
                .as("creates ResponseRestartGame instance from sc2api response")
                .isInstanceOf(ResponseRestartGame.class);
    }

    @Test
    void createsResponseQuitGameWhenSc2ApiResponseHasQuit() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithQuit()))
                .as("creates ResponseQuitGame instance from sc2api response")
                .isInstanceOf(ResponseQuitGame.class);
    }

    @Test
    void createsResponseStartReplayWhenSc2ApiResponseHasStartReplay() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithStartReplay()))
                .as("creates ResponseStartReplay instance from sc2api response")
                .isInstanceOf(ResponseStartReplay.class);
    }

    @Test
    void createsResponseReplayInfoWhenSc2ApiResponseHasReplayInfo() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithReplayInfo()))
                .as("creates ResponseReplayInfo instance from sc2api response")
                .isInstanceOf(ResponseReplayInfo.class);
    }

    @Test
    void createsResponseObservationWhenSc2ApiResponseHasResponseObservation() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithObservation()))
                .as("creates ResponseObservation instance from sc2api response")
                .isInstanceOf(ResponseObservation.class);
    }

    @Test
    void createsResponseStepWhenSc2ApiResponseHasResponseStep() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithStep()))
                .as("creates ResponseStep instance from sc2api response")
                .isInstanceOf(ResponseStep.class);
    }

    @Test
    void createsResponseActionWhenSc2ApiResponseHasResponseAction() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithAction()))
                .as("creates ResponseAction instance from sc2api response")
                .isInstanceOf(ResponseAction.class);
    }

    @Test
    void createsResponseQuickSaveWhenSc2ApiResponseHasResponseQuickSave() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithQuickSave()))
                .as("creates ResponseQuickSave instance from sc2api response")
                .isInstanceOf(ResponseQuickSave.class);
    }

    @Test
    void createsResponseQuickLoadWhenSc2ApiResponseHasResponseQuickLoad() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithQuickLoad()))
                .as("creates ResponseQuickLoad instance from sc2api response")
                .isInstanceOf(ResponseQuickLoad.class);
    }

    @Test
    void createsResponseSaveMapWhenSc2ApiResponseHasResponseSaveMap() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithSaveMap()))
                .as("creates ResponseSaveMap instance from sc2api response")
                .isInstanceOf(ResponseSaveMap.class);
    }

    @Test
    void createsResponseSaveReplayWhenSc2ApiResponseHasResponseSaveReplay() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithSaveReplay()))
                .as("creates ResponseSaveReplay instance from sc2api response")
                .isInstanceOf(ResponseSaveReplay.class);
    }

    @Test
    void createsResponseDebugWhenSc2ApiResponseHasResponseDebug() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithDebug()))
                .as("creates ResponseDebug instance from sc2api response")
                .isInstanceOf(ResponseDebug.class);
    }

    @Test
    void createsResponseGameInfoWhenSc2ApiResponseHasResponseGameInfo() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithGameInfo()))
                .as("creates ResponseGameInfo instance from sc2api response")
                .isInstanceOf(ResponseGameInfo.class);
    }

    @Test
    void createsResponseQueryWhenSc2ApiResponseHasResponseQuery() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithQuery()))
                .as("creates ResponseQuery instance from sc2api response")
                .isInstanceOf(ResponseQuery.class);
    }

    @Test
    void createsResponseDataWhenSc2ApiResponseHasResponseData() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithData()))
                .as("creates ResponseData instance from sc2api response")
                .isInstanceOf(ResponseData.class);
    }

    @Test
    void createsResponseObserverActionWhenSc2ApiResponseHasResponseObserverAction() {
        assertThat(new ResponseConverter().apply(sc2ApiResponseWithObserverAction()))
                .as("creates ResponseObserverAction instance from sc2api response")
                .isInstanceOf(ResponseObserverAction.class);
    }
}