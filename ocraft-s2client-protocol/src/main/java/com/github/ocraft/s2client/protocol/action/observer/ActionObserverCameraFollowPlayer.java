package com.github.ocraft.s2client.protocol.action.observer;

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
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverCameraFollowPlayerBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverCameraFollowPlayerSyntax;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

// since 4.0
public final class ActionObserverCameraFollowPlayer
        implements Sc2ApiSerializable<Sc2Api.ActionObserverCameraFollowPlayer> {

    private static final long serialVersionUID = -1145903700544829391L;

    private final int playerId;

    public static final class Builder
            implements ActionObserverCameraFollowPlayerSyntax, ActionObserverCameraFollowPlayerBuilder {

        private Integer playerId;

        @Override
        public ActionObserverCameraFollowPlayerBuilder withId(int playerId) {
            this.playerId = playerId;
            return this;
        }

        @Override
        public ActionObserverCameraFollowPlayer build() {
            require("player id", playerId);
            return new ActionObserverCameraFollowPlayer(this);
        }
    }

    private ActionObserverCameraFollowPlayer(ActionObserverCameraFollowPlayer.Builder builder) {
        playerId = builder.playerId;

    }

    public static ActionObserverCameraFollowPlayerSyntax cameraFollowPlayer() {
        return new ActionObserverCameraFollowPlayer.Builder();
    }

    @Override
    public Sc2Api.ActionObserverCameraFollowPlayer toSc2Api() {
        return Sc2Api.ActionObserverCameraFollowPlayer.newBuilder().setPlayerId(playerId).build();
    }

    public int getPlayerId() {
        return playerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionObserverCameraFollowPlayer that = (ActionObserverCameraFollowPlayer) o;

        return playerId == that.playerId;
    }

    @Override
    public int hashCode() {
        return playerId;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
