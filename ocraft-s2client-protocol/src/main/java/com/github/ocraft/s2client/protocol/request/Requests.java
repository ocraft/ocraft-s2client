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

import com.github.ocraft.s2client.protocol.syntax.request.*;

public interface Requests {
    static RequestActionSyntax actions() {
        return RequestAction.actions();
    }

    static RequestObserverActionSyntax observerActions() {
        return RequestObserverAction.observerActions();
    }

    static RequestAvailableMaps availableMaps() {
        return RequestAvailableMaps.availableMaps();
    }

    static RequestCreateGameSyntax createGame() {
        return RequestCreateGame.createGame();
    }

    static RequestDataSyntax data() {
        return RequestData.data();
    }

    static RequestDebugSyntax debug() {
        return RequestDebug.debug();
    }

    static RequestGameInfo gameInfo() {
        return RequestGameInfo.gameInfo();
    }

    static RequestJoinGameSyntax joinGame() {
        return RequestJoinGame.joinGame();
    }

    static RequestLeaveGame leaveGame() {
        return RequestLeaveGame.leaveGame();
    }

    static RequestObservationSyntax observation() {
        return RequestObservation.observation();
    }

    static RequestPing ping() {
        return RequestPing.ping();
    }

    static RequestQuerySyntax query() {
        return RequestQuery.query();
    }

    static RequestQuickLoad quickLoad() {
        return RequestQuickLoad.quickLoad();
    }

    static RequestQuickSave quickSave() {
        return RequestQuickSave.quickSave();
    }

    static RequestQuitGame quitGame() {
        return RequestQuitGame.quitGame();
    }

    static RequestReplayInfoSyntax replayInfo() {
        return RequestReplayInfo.replayInfo();
    }

    static RequestRestartGame restartGame() {
        return RequestRestartGame.restartGame();
    }

    static RequestSaveMap.Builder saveMap() {
        return RequestSaveMap.saveMap();
    }

    static RequestSaveReplay saveReplay() {
        return RequestSaveReplay.saveReplay();
    }

    static RequestStartReplaySyntax startReplay() {
        return RequestStartReplay.startReplay();
    }

    static RequestStepSyntax nextStep() {
        return RequestStep.nextStep();
    }

}
