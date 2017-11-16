package com.github.ocraft.s2client.protocol.response;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.GameStatus;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

public final class ResponseQuickSave extends Response {

    private static final long serialVersionUID = -3809601289626843992L;

    private ResponseQuickSave(Sc2Api.Status sc2ApiStatus) {
        super(ResponseType.QUICK_SAVE, GameStatus.from(sc2ApiStatus));
    }

    public static ResponseQuickSave from(Sc2Api.Response sc2ApiResponse) {
        if (!hasQuickSaveResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have quick save response");
        }
        return new ResponseQuickSave(sc2ApiResponse.getStatus());
    }

    private static boolean hasQuickSaveResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasQuickSave();
    }

    @Override
    public boolean equals(Object o) {
        return this == o ||
                (o instanceof ResponseQuickSave && super.equals(o) && ((ResponseQuickSave) o).canEqual(this));
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseQuickSave;
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