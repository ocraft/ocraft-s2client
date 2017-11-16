package com.github.ocraft.s2client.protocol.response;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.action.ActionResult;
import com.github.ocraft.s2client.protocol.game.GameStatus;

import java.util.ArrayList;
import java.util.List;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static java.util.stream.Collectors.toList;

public final class ResponseAction extends Response {

    private static final long serialVersionUID = 4533986173144519313L;

    private final List<ActionResult> results;

    private ResponseAction(Sc2Api.ResponseAction sc2ApiResponseAction, Sc2Api.Status status) {
        super(ResponseType.ACTION, GameStatus.from(status));
        results = sc2ApiResponseAction.getResultList().stream().map(ActionResult::from).collect(toList());
    }

    public static ResponseAction from(Sc2Api.Response sc2ApiResponse) {
        if (!hasActionResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have action response");
        }
        return new ResponseAction(sc2ApiResponse.getAction(), sc2ApiResponse.getStatus());
    }

    private static boolean hasActionResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasAction();
    }

    public List<ActionResult> getResults() {
        return new ArrayList<>(results);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseAction)) return false;
        if (!super.equals(o)) return false;

        ResponseAction that = (ResponseAction) o;

        return that.canEqual(this) && results.equals(that.results);
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseAction;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + results.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
