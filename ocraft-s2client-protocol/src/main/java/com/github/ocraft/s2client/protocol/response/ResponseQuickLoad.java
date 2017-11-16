package com.github.ocraft.s2client.protocol.response;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.GameStatus;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

public final class ResponseQuickLoad extends Response {

    private static final long serialVersionUID = -3809601289626843992L;

    private ResponseQuickLoad(Sc2Api.Status sc2ApiStatus) {
        super(ResponseType.QUICK_LOAD, GameStatus.from(sc2ApiStatus));
    }

    public static ResponseQuickLoad from(Sc2Api.Response sc2ApiResponse) {
        if (!hasQuickLoadResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have quick load response");
        }
        return new ResponseQuickLoad(sc2ApiResponse.getStatus());
    }

    private static boolean hasQuickLoadResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasQuickLoad();
    }

    @Override
    public boolean equals(Object o) {
        return this == o ||
                (o instanceof ResponseQuickLoad && super.equals(o) && ((ResponseQuickLoad) o).canEqual(this));
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseQuickLoad;
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