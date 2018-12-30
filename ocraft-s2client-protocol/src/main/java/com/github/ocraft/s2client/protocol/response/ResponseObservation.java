package com.github.ocraft.s2client.protocol.response;

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
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.action.Action;
import com.github.ocraft.s2client.protocol.action.ActionError;
import com.github.ocraft.s2client.protocol.game.GameStatus;
import com.github.ocraft.s2client.protocol.observation.ChatReceived;
import com.github.ocraft.s2client.protocol.observation.Observation;
import com.github.ocraft.s2client.protocol.observation.PlayerResult;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public final class ResponseObservation extends Response {

    private static final long serialVersionUID = 8195090336161172691L;

    private final List<Action> actions;
    private final List<ActionError> actionErrors;
    private final Observation observation;
    private final List<PlayerResult> playerResults;
    private final List<ChatReceived> chat;

    private ResponseObservation(Sc2Api.ResponseObservation sc2ApiResponseObservation, Sc2Api.Status status) {
        super(ResponseType.OBSERVATION, GameStatus.from(status));

        this.actions = sc2ApiResponseObservation.getActionsList().stream()
                .filter(actionIsValid()).map(Action::from)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));

        this.actionErrors = sc2ApiResponseObservation.getActionErrorsList().stream()
                .map(ActionError::from)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
        this.observation = tryGet(
                Sc2Api.ResponseObservation::getObservation, Sc2Api.ResponseObservation::hasObservation
        ).apply(sc2ApiResponseObservation).map(Observation::from).orElseThrow(required("observation"));
        this.playerResults = sc2ApiResponseObservation.getPlayerResultList().stream()
                .map(PlayerResult::from)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
        this.chat = sc2ApiResponseObservation.getChatList().stream().map(ChatReceived::from)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    private Predicate<Sc2Api.Action> actionIsValid() {
        return a -> a.getSerializedSize() > 0 && (
                a.hasActionRaw() ||
                        a.hasActionChat() ||
                        a.hasActionFeatureLayer() ||
                        a.hasActionRender() ||
                        a.hasActionUi());
    }

    public static ResponseObservation from(Sc2Api.Response sc2ApiResponse) {
        if (!hasObservationResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have observation response");
        }
        return new ResponseObservation(sc2ApiResponse.getObservation(), sc2ApiResponse.getStatus());
    }

    private static boolean hasObservationResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasObservation();
    }

    public List<Action> getActions() {
        return actions;
    }

    public List<ActionError> getActionErrors() {
        return actionErrors;
    }

    public Observation getObservation() {
        return observation;
    }

    public List<PlayerResult> getPlayerResults() {
        return playerResults;
    }

    public List<ChatReceived> getChat() {
        return chat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseObservation)) return false;
        if (!super.equals(o)) return false;

        ResponseObservation that = (ResponseObservation) o;

        return that.canEqual(this) &&
                actions.equals(that.actions) &&
                actionErrors.equals(that.actionErrors) &&
                observation.equals(that.observation) &&
                playerResults.equals(that.playerResults) &&
                chat.equals(that.chat);
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseObservation;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + actions.hashCode();
        result = 31 * result + actionErrors.hashCode();
        result = 31 * result + observation.hashCode();
        result = 31 * result + playerResults.hashCode();
        result = 31 * result + chat.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
