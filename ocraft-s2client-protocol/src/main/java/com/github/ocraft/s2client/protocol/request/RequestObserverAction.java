package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.action.ObserverAction;
import com.github.ocraft.s2client.protocol.syntax.request.RequestObserverActionSyntax;

import java.util.ArrayList;
import java.util.List;

import static com.github.ocraft.s2client.protocol.Preconditions.requireNotEmpty;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

// since 4.0
public final class RequestObserverAction extends Request {

    private static final long serialVersionUID = 1667152914023566455L;

    private final List<ObserverAction> actions;

    public static final class Builder implements BuilderSyntax<RequestObserverAction>, RequestObserverActionSyntax {

        private List<ObserverAction> actions = new ArrayList<>();

        @Override
        public Builder with(ObserverAction... actions) {
            this.actions.addAll(asList(actions));
            return this;
        }

        @Override
        public RequestObserverAction build() {
            requireNotEmpty("action list", actions);
            return new RequestObserverAction(this);
        }
    }

    private RequestObserverAction(Builder builder) {
        this.actions = builder.actions;
    }

    public static RequestObserverActionSyntax observerActions() {
        return new Builder();
    }

    @Override
    public Sc2Api.Request toSc2Api() {
        return Sc2Api.Request.newBuilder()
                .setObsAction(Sc2Api.RequestObserverAction.newBuilder()
                        .addAllActions(actions.stream().map(ObserverAction::toSc2Api).collect(toList()))
                        .build())
                .build();
    }

    public List<ObserverAction> getActions() {
        return new ArrayList<>(actions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestObserverAction that = (RequestObserverAction) o;

        return actions.equals(that.actions);
    }

    @Override
    public int hashCode() {
        return actions.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}

