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
import com.github.ocraft.s2client.protocol.syntax.request.RequestStepSyntax;

public final class RequestStep extends Request {

    private static final long serialVersionUID = 5328362727033417346L;

    private static final int DEFAULT_GAME_LOOP_COUNT_IN_STEP = 1;

    private final int count;

    private RequestStep(Builder builder) {
        this.count = builder.count;
    }

    public static final class Builder implements RequestStepSyntax {

        private int count = DEFAULT_GAME_LOOP_COUNT_IN_STEP;

        @Override
        public BuilderSyntax<RequestStep> withCount(int gameLoopCount) {
            if (gameLoopCount < 1) throw new IllegalArgumentException("count must be greater than 0");
            this.count = gameLoopCount;
            return this;
        }

        @Override
        public RequestStep build() {
            return new RequestStep(this);
        }
    }

    public static RequestStepSyntax nextStep() {
        return new Builder();
    }

    public int getCount() {
        return count;
    }

    @Override
    public Sc2Api.Request toSc2Api() {
        return Sc2Api.Request.newBuilder()
                .setStep(Sc2Api.RequestStep.newBuilder().setCount(count).build())
                .build();
    }

    @Override
    public ResponseType responseType() {
        return ResponseType.STEP;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestStep that = (RequestStep) o;

        return count == that.count;
    }

    @Override
    public int hashCode() {
        return count;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
