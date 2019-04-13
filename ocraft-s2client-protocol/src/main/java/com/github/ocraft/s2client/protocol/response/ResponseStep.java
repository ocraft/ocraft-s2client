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
import com.github.ocraft.s2client.protocol.game.GameStatus;

import java.util.Objects;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

public final class ResponseStep extends Response {

    private static final long serialVersionUID = 6319486847857955735L;

    private final Integer simulationLoop;

    private ResponseStep(Sc2Api.ResponseStep responseStep, Sc2Api.Status sc2ApiStatus, int id) {
        super(ResponseType.STEP, GameStatus.from(sc2ApiStatus), id);
        this.simulationLoop = tryGet(
                Sc2Api.ResponseStep::getSimulationLoop, Sc2Api.ResponseStep::hasSimulationLoop
        ).apply(responseStep).orElse(nothing());
    }

    public static ResponseStep from(Sc2Api.Response sc2ApiResponse) {
        if (!hasStepResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have step response");
        }
        return new ResponseStep(sc2ApiResponse.getStep(), sc2ApiResponse.getStatus(), sc2ApiResponse.getId());
    }

    private static boolean hasStepResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasStep();
    }

    /**
     * Max simulation_loop is (1<<19) before "end of time" will occur.
     * The "end of time" is classified as the maximum number of game loops or absolute game time representable as
     * a positive fixed point number. When we reach the "end of time", permanently pause the game and end the game
     * for all.
     */
    public Optional<Integer> getSimulationLoop() {
        return Optional.ofNullable(simulationLoop);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseStep)) return false;
        if (!super.equals(o)) return false;

        ResponseStep that = (ResponseStep) o;

        if (!that.canEqual(this)) return false;

        return Objects.equals(simulationLoop, that.simulationLoop);
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseStep;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (simulationLoop != null ? simulationLoop.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
