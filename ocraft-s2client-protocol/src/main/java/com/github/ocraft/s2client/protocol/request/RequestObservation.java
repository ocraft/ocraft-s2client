package com.github.ocraft.s2client.protocol.request;

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
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.response.ResponseType;
import com.github.ocraft.s2client.protocol.syntax.request.ObservationGameLoopSyntax;
import com.github.ocraft.s2client.protocol.syntax.request.RequestObservationSyntax;

import java.util.Objects;
import java.util.Optional;

public final class RequestObservation extends Request {

    private static final long serialVersionUID = -943684698900532129L;

    private final boolean disableFog;
    private final Integer gameLoop;

    private RequestObservation(Builder builder) {
        this.disableFog = builder.disableFog;
        this.gameLoop = builder.gameLoop;
    }

    public static class Builder implements RequestObservationSyntax {

        private boolean disableFog;
        private Integer gameLoop;

        @Override
        public ObservationGameLoopSyntax disableFog() {
            this.disableFog = true;
            return this;
        }

        @Override
        public BuilderSyntax<RequestObservation> gameLoop(int loop) {
            this.gameLoop = loop;
            return this;
        }

        @Override
        public RequestObservation build() {
            return new RequestObservation(this);
        }
    }

    public static RequestObservationSyntax observation() {
        return new Builder();
    }

    @Override
    public Sc2Api.Request toSc2Api() {
        Sc2Api.RequestObservation.Builder request = Sc2Api.RequestObservation.newBuilder().setDisableFog(disableFog);

        Optional.ofNullable(gameLoop).ifPresent(request::setGameLoop);

        return Sc2Api.Request.newBuilder()
                .setObservation(request.build())
                .build();
    }

    @Override
    public ResponseType responseType() {
        return ResponseType.OBSERVATION;
    }

    public boolean isDisableFog() {
        return disableFog;
    }

    public Optional<Integer> getGameLoop() {
        return Optional.ofNullable(gameLoop);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RequestObservation that = (RequestObservation) o;

        if (disableFog != that.disableFog) return false;
        return Objects.equals(gameLoop, that.gameLoop);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (disableFog ? 1 : 0);
        result = 31 * result + (gameLoop != null ? gameLoop.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
