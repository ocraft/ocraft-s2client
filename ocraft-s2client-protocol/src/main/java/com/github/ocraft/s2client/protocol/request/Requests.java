package com.github.ocraft.s2client.protocol.request;

import com.github.ocraft.s2client.protocol.syntax.request.*;

public interface Requests {
    static RequestActionSyntax actions() {
        return RequestAction.actions();
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
