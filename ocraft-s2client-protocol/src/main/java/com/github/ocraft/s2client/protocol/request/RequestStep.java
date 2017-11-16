package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Strings;
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
