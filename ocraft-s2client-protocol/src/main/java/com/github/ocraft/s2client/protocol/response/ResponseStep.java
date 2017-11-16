package com.github.ocraft.s2client.protocol.response;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.GameStatus;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

public final class ResponseStep extends Response {

    private static final long serialVersionUID = -6077963672255615287L;

    private ResponseStep(Sc2Api.Status sc2ApiStatus) {
        super(ResponseType.STEP, GameStatus.from(sc2ApiStatus));
    }

    public static ResponseStep from(Sc2Api.Response sc2ApiResponse) {
        if (!hasStepResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have step response");
        }
        return new ResponseStep(sc2ApiResponse.getStatus());
    }

    private static boolean hasStepResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasStep();
    }

    @Override
    public boolean equals(Object o) {
        return this == o ||
                (o instanceof ResponseStep && super.equals(o) && ((ResponseStep) o).canEqual(this));
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseStep;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
