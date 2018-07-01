package com.github.ocraft.s2client.protocol.action;

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
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.action.observer.ActionObserverCameraFollowPlayer;
import com.github.ocraft.s2client.protocol.action.observer.ActionObserverCameraFollowUnits;
import com.github.ocraft.s2client.protocol.action.observer.ActionObserverCameraMove;
import com.github.ocraft.s2client.protocol.action.observer.ActionObserverPlayerPerspective;
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverCameraFollowPlayerBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverCameraFollowUnitsBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverCameraMoveBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverPlayerPerspectiveBuilder;

import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

// since 4.0
public final class ObserverAction implements Sc2ApiSerializable<Sc2Api.ObserverAction> {

    private static final long serialVersionUID = -7478858885331502760L;

    private final ActionObserverPlayerPerspective playerPerspective;
    private final ActionObserverCameraMove cameraMove;
    private final ActionObserverCameraFollowPlayer cameraFollowPlayer;
    private final ActionObserverCameraFollowUnits cameraFollowUnits;

    public static final class Builder {
        public ObserverAction of(ActionObserverPlayerPerspective playerPerspective) {
            require("player perspective", playerPerspective);
            return new ObserverAction(playerPerspective);
        }

        public ObserverAction of(ActionObserverPlayerPerspectiveBuilder playerPerspective) {
            return new ObserverAction(playerPerspective.build());
        }

        public ObserverAction of(ActionObserverCameraMove cameraMove) {
            require("camera move", cameraMove);
            return new ObserverAction(cameraMove);
        }

        public ObserverAction of(ActionObserverCameraMoveBuilder cameraMove) {
            return new ObserverAction(cameraMove.build());
        }

        public ObserverAction of(ActionObserverCameraFollowPlayer cameraFollowPlayer) {
            require("camera follow player", cameraFollowPlayer);
            return new ObserverAction(cameraFollowPlayer);
        }

        public ObserverAction of(ActionObserverCameraFollowPlayerBuilder cameraFollowPlayer) {
            return new ObserverAction(cameraFollowPlayer.build());
        }

        public ObserverAction of(ActionObserverCameraFollowUnits cameraFollowUnits) {
            require("camera follow units", cameraFollowUnits);
            return new ObserverAction(cameraFollowUnits);
        }

        public ObserverAction of(ActionObserverCameraFollowUnitsBuilder cameraFollowUnits) {
            return new ObserverAction(cameraFollowUnits.build());
        }
    }

    public static Builder observerAction() {
        return new Builder();
    }

    private ObserverAction(ActionObserverPlayerPerspective playerPerspective) {
        this.playerPerspective = playerPerspective;
        this.cameraMove = nothing();
        this.cameraFollowPlayer = nothing();
        this.cameraFollowUnits = nothing();
    }

    private ObserverAction(ActionObserverCameraMove cameraMove) {
        this.playerPerspective = nothing();
        this.cameraMove = cameraMove;
        this.cameraFollowPlayer = nothing();
        this.cameraFollowUnits = nothing();
    }

    private ObserverAction(ActionObserverCameraFollowPlayer cameraFollowPlayer) {
        this.playerPerspective = nothing();
        this.cameraMove = nothing();
        this.cameraFollowPlayer = cameraFollowPlayer;
        this.cameraFollowUnits = nothing();
    }

    private ObserverAction(ActionObserverCameraFollowUnits cameraFollowUnits) {
        this.playerPerspective = nothing();
        this.cameraMove = nothing();
        this.cameraFollowPlayer = nothing();
        this.cameraFollowUnits = cameraFollowUnits;
    }

    @Override
    public Sc2Api.ObserverAction toSc2Api() {
        Sc2Api.ObserverAction.Builder aSc2ApiObserverAction = Sc2Api.ObserverAction.newBuilder();

        getPlayerPerspective()
                .map(ActionObserverPlayerPerspective::toSc2Api).ifPresent(aSc2ApiObserverAction::setPlayerPerspective);
        getCameraMove().map(ActionObserverCameraMove::toSc2Api).ifPresent(aSc2ApiObserverAction::setCameraMove);
        getCameraFollowPlayer()
                .map(ActionObserverCameraFollowPlayer::toSc2Api)
                .ifPresent(aSc2ApiObserverAction::setCameraFollowPlayer);
        getCameraFollowUnits()
                .map(ActionObserverCameraFollowUnits::toSc2Api).ifPresent(aSc2ApiObserverAction::setCameraFollowUnits);

        return aSc2ApiObserverAction.build();
    }

    public Optional<ActionObserverPlayerPerspective> getPlayerPerspective() {
        return Optional.ofNullable(playerPerspective);
    }

    public Optional<ActionObserverCameraMove> getCameraMove() {
        return Optional.ofNullable(cameraMove);
    }

    public Optional<ActionObserverCameraFollowPlayer> getCameraFollowPlayer() {
        return Optional.ofNullable(cameraFollowPlayer);
    }

    public Optional<ActionObserverCameraFollowUnits> getCameraFollowUnits() {
        return Optional.ofNullable(cameraFollowUnits);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObserverAction that = (ObserverAction) o;

        return (playerPerspective != null
                ? playerPerspective.equals(that.playerPerspective)
                : that.playerPerspective == null) &&
                (cameraMove != null
                        ? cameraMove.equals(that.cameraMove)
                        : that.cameraMove == null) &&
                (cameraFollowPlayer != null
                        ? cameraFollowPlayer.equals(that.cameraFollowPlayer)
                        : that.cameraFollowPlayer == null) &&
                (cameraFollowUnits != null
                        ? cameraFollowUnits.equals(that.cameraFollowUnits)
                        : that.cameraFollowUnits == null);
    }

    @Override
    public int hashCode() {
        int result = playerPerspective != null ? playerPerspective.hashCode() : 0;
        result = 31 * result + (cameraMove != null ? cameraMove.hashCode() : 0);
        result = 31 * result + (cameraFollowPlayer != null ? cameraFollowPlayer.hashCode() : 0);
        result = 31 * result + (cameraFollowUnits != null ? cameraFollowUnits.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
