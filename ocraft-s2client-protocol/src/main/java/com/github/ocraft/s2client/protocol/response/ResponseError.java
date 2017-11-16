package com.github.ocraft.s2client.protocol.response;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.GameStatus;

import java.util.Collections;
import java.util.List;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

public final class ResponseError extends Response {

    private static final long serialVersionUID = -2188150135746027233L;

    private final List<String> errors;

    private ResponseError(List<String> errors, Sc2Api.Status status) {
        super(ResponseType.ERROR, GameStatus.from(status));
        this.errors = Collections.unmodifiableList(errors);
    }

    public static ResponseError from(Sc2Api.Response sc2ApiResponse) {
        if (!isSet(sc2ApiResponse) || sc2ApiResponse.getErrorCount() == 0) {
            throw new IllegalArgumentException("provided argument doesn't have error response");
        }
        return new ResponseError(sc2ApiResponse.getErrorList(), sc2ApiResponse.getStatus());
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseError)) return false;
        if (!super.equals(o)) return false;

        ResponseError that = (ResponseError) o;

        return that.canEqual(this) && errors.equals(that.errors);
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseError;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + errors.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
