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

import com.github.ocraft.s2client.bot.gateway.ActionFeatureLayerInterface;
import com.github.ocraft.s2client.bot.gateway.ActionInterface;
import com.github.ocraft.s2client.bot.gateway.AgentControlInterface;
import com.github.ocraft.s2client.protocol.request.Requests;
import com.github.ocraft.s2client.protocol.response.ResponseRestartGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Consumer;

class AgentControlInterfaceImpl implements AgentControlInterface {

    private Logger log = LoggerFactory.getLogger(AgentControlInterfaceImpl.class);

    private final ControlInterfaceImpl controlInterface;
    private final ActionFeatureLayerInterfaceImpl actionFeatureLayerInterface;
    private final ActionInterfaceImpl actionInterface;

    AgentControlInterfaceImpl(ControlInterfaceImpl controlInterface) {
        this.controlInterface = controlInterface;
        this.actionFeatureLayerInterface = new ActionFeatureLayerInterfaceImpl(controlInterface);
        this.actionInterface = new ActionInterfaceImpl(controlInterface);
    }

    private ControlInterfaceImpl control() {
        return controlInterface;
    }

    @Override
    public boolean restart() {
        Optional<ResponseRestartGame> responseRestartGame = control()
                .waitForResponse(control().proto().sendRequest(Requests.restartGame()))
                .flatMap(response -> response.as(ResponseRestartGame.class));

        boolean isSuccess = responseRestartGame.isPresent() && !responseRestartGame.get().getError().isPresent();
        if (!isSuccess) {
            responseRestartGame.ifPresent(createRestartGameErrorHandler());
        } else {
            controlInterface.getObservation();
            controlInterface.onGameStart();
            controlInterface.clientEvents().onGameStart();
        }
        return isSuccess && control().isInGame();
    }

    private Consumer<? super ResponseRestartGame> createRestartGameErrorHandler() {
        return response -> {
            response.getError().ifPresent(
                    errorCode -> log.error("RestartGame request returned an error code: {}", errorCode));
            response.getErrorDetails().ifPresent(
                    errorDetails -> log.error("RestartGame request returned error details: {}", errorDetails));
        };
    }

    @Override
    public ActionInterface action() {
        return actionInterface;
    }

    @Override
    public ActionFeatureLayerInterface actionsFeatureLayer() {
        return actionFeatureLayerInterface;
    }
}
