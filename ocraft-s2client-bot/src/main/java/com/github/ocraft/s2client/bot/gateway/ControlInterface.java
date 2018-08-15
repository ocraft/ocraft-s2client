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

import com.github.ocraft.s2client.bot.ClientError;
import com.github.ocraft.s2client.bot.S2ReplayObserver;
import com.github.ocraft.s2client.bot.setting.InterfaceSettings;
import com.github.ocraft.s2client.bot.setting.PlayerSettings;
import com.github.ocraft.s2client.bot.setting.ProcessSettings;
import com.github.ocraft.s2client.protocol.game.BattlenetMap;
import com.github.ocraft.s2client.protocol.game.GameStatus;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import com.github.ocraft.s2client.protocol.game.MultiplayerOptions;
import com.github.ocraft.s2client.protocol.response.Response;
import com.github.ocraft.s2client.protocol.response.ResponseType;
import com.github.ocraft.s2client.protocol.unit.Tag;
import io.reactivex.Maybe;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface ControlInterface {

    ProtoInterface proto();

    ObservationInterface observation();

    boolean connect(ProcessSettings processSettings);

    boolean remoteSaveMap(LocalMap localMap);

    boolean createGame(BattlenetMap battlenetMap, List<PlayerSettings> playerSettings, Boolean realtime);

    boolean createGame(LocalMap localMap, List<PlayerSettings> playerSettings, Boolean realtime);

    Maybe<Response> requestJoinGame(
            PlayerSettings playerSettings, InterfaceSettings interfaceSettings, MultiplayerOptions multiplayerOptions);

    boolean isMultiplayer();

    boolean waitJoinGame(Maybe<Response> waitFor);

    Maybe<Response> requestLeaveGame();

    boolean pollLeaveGame(Maybe<Response> waitFor);

    boolean pollLeaveGame(); // TODO p.picheta to test usages

    Maybe<Response> step(int count);

    boolean waitStep(Maybe<Response> waitFor);

    boolean saveReplay(Path path) throws IOException;

    boolean ping();

    // General.

    void quit();

    Optional<Response> waitForResponse(Maybe<Response> waitFor);

    void setProcessInfo(ProcessInfo pi);

    ProcessInfo getProcessInfo();

    AppState getAppState();

    GameStatus getLastStatus();

    boolean isInGame();

    boolean isFinishedGame();

    boolean isReadyForCreateGame();

    boolean pollResponse(ResponseType type);

    boolean hasResponsePending(ResponseType type);

    boolean hasResponsePending();

    Maybe<Response> getResponsePending(ResponseType type);

    boolean getObservation();

    boolean issueEvents(List<Tag> commands);

    void onGameStart();

    // Diagnostic.

    void dumpProtoUsage();

    void error(ClientError error, List<String> errors);

    void errorIf(boolean condition, ClientError error, List<String> errors);

    List<ClientError> getClientErrors();

    List<String> getProtocolErrors();

    void clearClientErrors();

    void clearProtocolErrors();

    void useGeneralizedAbility(boolean useGeneralizedAbilityId);

    // Save/Load.
    boolean save();

    boolean load();

    AgentControlInterface agentControl();

    QueryInterface query();

    DebugInterface debug();

    ObserverActionInterface observerAction();

    ReplayControlInterface replayControl(S2ReplayObserver replayObserver);
}
