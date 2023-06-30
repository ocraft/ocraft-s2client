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

import com.github.ocraft.s2client.bot.gateway.ControlInterface;
import com.github.ocraft.s2client.bot.gateway.ObserverActionInterface;
import com.github.ocraft.s2client.protocol.action.ObserverAction;
import com.github.ocraft.s2client.protocol.action.observer.ActionObserverCameraFollowPlayer;
import com.github.ocraft.s2client.protocol.action.observer.ActionObserverCameraMove;
import com.github.ocraft.s2client.protocol.request.RequestObserverAction;
import com.github.ocraft.s2client.protocol.request.Requests;
import com.github.ocraft.s2client.protocol.response.ResponseObserverAction;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.action.observer.ActionObserverPlayerPerspective;
import com.github.ocraft.s2client.protocol.action.observer.ActionObserverCameraFollowUnits;
import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.util.ArrayList;
import java.util.List;

class ObserverActionInterfaceImpl implements ObserverActionInterface {

    private final ControlInterface controlInterface;
    private final List<ObserverAction> actions = new ArrayList<>();

    ObserverActionInterfaceImpl(ControlInterface controlInterface) {
        this.controlInterface = controlInterface;
    }

    private ControlInterface control() {
        return controlInterface;
    }

    @Override
    public ObserverActionInterface cameraMove(Point2d point, float distance) {
        actions.add(ObserverAction.observerAction().of(
                ActionObserverCameraMove
                        .cameraMove()
                        .to(Point2d.of(point.getX(), point.getY()))
                        .withDistance(distance)));
        return this;
    }

    @Override
    public ObserverActionInterface cameraFollowPlayer(int playerId) {
        actions.add(ObserverAction.observerAction().of(
                ActionObserverCameraFollowPlayer.cameraFollowPlayer().withId(playerId)));
        return this;
    }

    @Override
    public ObserverActionInterface cameraSetPerspective(int playerId) {
        actions.add(ObserverAction.observerAction().of(ActionObserverPlayerPerspective.playerPerspective().ofPlayer(playerId)));
        return this;
    }

    @Override
    public ObserverActionInterface cameraFollowUnits(Unit...units) {
        actions.add(ObserverAction.observerAction().of(ActionObserverCameraFollowUnits.cameraFollowUnits().of(units)));
        return this;
    }

    @Override
    public ObserverActionInterface cameraFollowUnits(Tag...units) {
        actions.add(ObserverAction.observerAction().of(ActionObserverCameraFollowUnits.cameraFollowUnits().withTags(units)));
        return this;
    }

    @Override
    public boolean sendActions() {
        if (actions.isEmpty()) return false;
        RequestObserverAction.Builder request = Requests.observerActions().with(actions.toArray(new ObserverAction[0]));

        actions.clear();
        return control()
                .waitForResponse(control().proto().sendRequest(request))
                .flatMap(response -> response.as(ResponseObserverAction.class))
                .isPresent();
    }
}
