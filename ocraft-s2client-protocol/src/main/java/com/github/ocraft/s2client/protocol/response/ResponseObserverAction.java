package com.github.ocraft.s2client.protocol.response;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.GameStatus;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

// since 4.0
public final class ResponseObserverAction extends Response {

    private static final long serialVersionUID = -909307390727840258L;

    private ResponseObserverAction(Sc2Api.Status sc2ApiStatus) {
        super(ResponseType.OBSERVER_ACTION, GameStatus.from(sc2ApiStatus));
    }

    public static ResponseObserverAction from(Sc2Api.Response sc2ApiResponse) {
        if (!hasObserverActionResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have observer action response");
        }
        return new ResponseObserverAction(sc2ApiResponse.getStatus());
    }

    private static boolean hasObserverActionResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasObsAction();
    }

    @Override
    public boolean equals(Object o) {
        return this == o ||
                (o instanceof ResponseObserverAction && super.equals(o) && ((ResponseObserverAction) o).canEqual(this));
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseObserverAction;
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
