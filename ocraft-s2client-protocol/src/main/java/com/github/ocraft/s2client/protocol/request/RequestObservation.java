package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.syntax.request.RequestObservationSyntax;

public final class RequestObservation extends Request {

    private static final long serialVersionUID = -943684698900532129L;

    private final boolean disableFog;

    private RequestObservation(Builder builder) {
        this.disableFog = builder.disableFog;
    }

    public static class Builder implements RequestObservationSyntax {

        private boolean disableFog;

        @Override
        public BuilderSyntax<RequestObservation> disableFog() {
            this.disableFog = true;
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
        return Sc2Api.Request.newBuilder()
                .setObservation(Sc2Api.RequestObservation.newBuilder()
                        .setDisableFog(disableFog)
                        .build())
                .build();
    }

    public boolean isDisableFog() {
        return disableFog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestObservation that = (RequestObservation) o;

        return disableFog == that.disableFog;
    }

    @Override
    public int hashCode() {
        return (disableFog ? 1 : 0);
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
