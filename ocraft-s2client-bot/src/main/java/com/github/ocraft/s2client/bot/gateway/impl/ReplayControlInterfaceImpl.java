package com.github.ocraft.s2client.bot.gateway.impl;

/*-
 * #%L
 * ocraft-s2client-bot
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

import com.github.ocraft.s2client.bot.S2ReplayObserver;
import com.github.ocraft.s2client.bot.gateway.ReplayControlInterface;
import com.github.ocraft.s2client.bot.setting.InterfaceSettings;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.game.ReplayInfo;
import com.github.ocraft.s2client.protocol.request.RequestReplayInfo;
import com.github.ocraft.s2client.protocol.request.RequestStartReplay;
import com.github.ocraft.s2client.protocol.response.Response;
import com.github.ocraft.s2client.protocol.response.ResponseReplayInfo;
import com.github.ocraft.s2client.protocol.response.ResponseStartReplay;
import io.reactivex.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

class ReplayControlInterfaceImpl implements ReplayControlInterface {

    private Logger log = LoggerFactory.getLogger(ReplayControlInterfaceImpl.class);

    private final ControlInterfaceImpl controlInterface;
    private final S2ReplayObserver replayObserver;

    private ReplayInfo replayInfo;
    private Path replayPath;

    ReplayControlInterfaceImpl(ControlInterfaceImpl controlInterface, S2ReplayObserver replayObserver) {
        this.controlInterface = controlInterface;
        this.replayObserver = replayObserver;
    }

    private ControlInterfaceImpl control() {
        return controlInterface;
    }

    @Override
    public boolean gatherReplayInfo(Path path, boolean downloadData) {
        BuilderSyntax<RequestReplayInfo> request = RequestReplayInfo.replayInfo().of(path).download(downloadData);
        Optional<ResponseReplayInfo> responseReplayInfo = control()
                .waitForResponse(control().proto().sendRequest(request))
                .flatMap(response -> response.as(ResponseReplayInfo.class));

        boolean isSuccess = responseReplayInfo.isPresent() &&
                responseReplayInfo.get().getReplayInfo().isPresent() &&
                !responseReplayInfo.get().getError().isPresent();
        if (!isSuccess) {
            responseReplayInfo.ifPresent(replayInfoErrorHandler());
        } else {
            this.replayInfo = responseReplayInfo.get().getReplayInfo().get();
            this.replayPath = path;
        }
        return isSuccess;
    }

    private Consumer<? super ResponseReplayInfo> replayInfoErrorHandler() {
        return response -> {
            response.getError().ifPresent(
                    errorCode -> log.error("ReplayInfo request returned an error code: {}", errorCode));
            response.getErrorDetails().ifPresent(
                    errorDetails -> log.error("ReplayInfo request returned error details: {}", errorDetails));
        };
    }

    @Override
    public Maybe<Response> loadReplay(Path replayPath, InterfaceSettings settings, int playerId, boolean realtime) {
        RequestStartReplay request = RequestStartReplay
                .startReplay()
                .from(replayPath)
                .use(control().interfaceOptionsFrom(settings))
                .toObserve(playerId)
                .realtime(realtime)
                .build();

        return control().proto().sendRequest(request);
    }

    @Override
    public boolean waitForReplay(Maybe<Response> waitFor) {
        Optional<ResponseStartReplay> responseStartReplay = control()
                .waitForResponse(waitFor)
                .flatMap(response -> response.as(ResponseStartReplay.class));

        boolean isSuccess = responseStartReplay.isPresent() && !responseStartReplay.get().getError().isPresent();
        if (!isSuccess) {
            responseStartReplay.ifPresent(startReplayErrorHandler());
        } else {
            if (control().isInGame()) {
                if (isSet(replayPath)) {
                    control().getObservation();
                    replayObserver.control().onGameStart();
                    replayObserver.onGameStart();
                    log.info("Replaying: '{}'", replayPath);
                } else {
                    log.info("WaitForReplay: new replay loaded, replay path unknown");
                }
            } else {
                log.error("WaitForReplay: not in a game.");
                isSuccess = false;
            }
        }
        return isSuccess;
    }

    private Consumer<? super ResponseStartReplay> startReplayErrorHandler() {
        return response -> {
            response.getError().ifPresent(
                    errorCode -> log.error("StartReplay request returned an error code: {}", errorCode));
            response.getErrorDetails().ifPresent(
                    errorDetails -> log.error("StartReplay request returned error details: {}", errorDetails));
        };
    }

    @Override
    public void useGeneralizedAbility(boolean value) {
        control().useGeneralizedAbility(value);
    }

    @Override
    public ReplayInfo getReplayInfo() {
        return replayInfo;
    }
}
