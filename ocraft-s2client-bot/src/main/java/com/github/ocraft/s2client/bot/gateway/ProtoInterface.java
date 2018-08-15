package com.github.ocraft.s2client.bot.gateway;

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

import com.github.ocraft.s2client.api.controller.S2Controller;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.game.GameStatus;
import com.github.ocraft.s2client.protocol.request.Request;
import com.github.ocraft.s2client.protocol.response.Response;
import com.github.ocraft.s2client.protocol.response.ResponseType;
import io.reactivex.Maybe;

import java.util.Map;
import java.util.Optional;

public interface ProtoInterface {
    boolean connectToGame(
            S2Controller theGame,
            Integer connectionTimeoutInMillis,
            Integer requestTimeoutInMillis,
            Boolean traced);

    boolean connectToGame(
            String address,
            Integer port,
            Integer connectionTimeoutInMillis,
            Integer requestTimeoutInMillis,
            Boolean traced);

    <T extends Request> Maybe<Response> sendRequest(T requestData);

    <T extends Request> Maybe<Response> sendRequest(BuilderSyntax<T> requestDataBuilder);

    Optional<Response> waitForResponse(Maybe<Response> waitFor);

    void quit();

    boolean pollResponse(ResponseType type);

    boolean hasResponsePending(ResponseType type);

    boolean hasResponsePending();

    Maybe<Response> getResponsePending(ResponseType type);

    GameStatus lastStatus();

    String getConnectToIp();

    Integer getConnectToPort();

    String getDataVersion();

    Integer getBaseBuild();

    Map<ResponseType, Integer> getCountUses();
}
