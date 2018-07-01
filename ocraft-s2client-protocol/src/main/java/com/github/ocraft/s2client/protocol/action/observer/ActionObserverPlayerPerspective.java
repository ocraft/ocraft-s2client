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
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverPlayerPerspectiveBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverPlayerPerspectiveSyntax;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

// since 4.0
public final class ActionObserverPlayerPerspective
        implements Sc2ApiSerializable<Sc2Api.ActionObserverPlayerPerspective> {

    private static final long serialVersionUID = 6112751517868096950L;

    private static final int ALL_PLAYERS = 0;

    private final int playerId;

    public static final class Builder
            implements ActionObserverPlayerPerspectiveSyntax, ActionObserverPlayerPerspectiveBuilder {

        private Integer playerId;

        @Override
        public ActionObserverPlayerPerspectiveBuilder ofPlayer(int playerId) {
            this.playerId = playerId;
            return this;
        }

        @Override
        public ActionObserverPlayerPerspectiveBuilder ofAll() {
            playerId = ALL_PLAYERS;
            return this;
        }

        @Override
        public ActionObserverPlayerPerspective build() {
            require("player id", playerId);
            return new ActionObserverPlayerPerspective(this);
        }
    }

    private ActionObserverPlayerPerspective(Builder builder) {
        playerId = builder.playerId;

    }

    public static ActionObserverPlayerPerspectiveSyntax playerPerspective() {
        return new Builder();
    }

    @Override
    public Sc2Api.ActionObserverPlayerPerspective toSc2Api() {
        return Sc2Api.ActionObserverPlayerPerspective.newBuilder().setPlayerId(playerId).build();
    }

    public int getPlayerId() {
        return playerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionObserverPlayerPerspective that = (ActionObserverPlayerPerspective) o;

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
