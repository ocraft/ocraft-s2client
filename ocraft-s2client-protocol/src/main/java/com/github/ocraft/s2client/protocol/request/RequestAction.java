package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.action.Action;
import com.github.ocraft.s2client.protocol.syntax.action.ActionBuilder;
import com.github.ocraft.s2client.protocol.syntax.request.RequestActionSyntax;

import java.util.ArrayList;
import java.util.List;

import static com.github.ocraft.s2client.protocol.Preconditions.requireNotEmpty;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public final class RequestAction extends Request {

    private static final long serialVersionUID = 1667152914023566455L;

    private final List<Action> actions;

    public static final class Builder implements BuilderSyntax<RequestAction>, RequestActionSyntax {

        private List<Action> actions = new ArrayList<>();

        @Override
        public Builder of(Action... actions) {
            this.actions.addAll(asList(actions));
            return this;
        }

        @Override
        public final Builder of(ActionBuilder... actions) {
            this.actions.addAll(BuilderSyntax.buildAll(actions));
            return this;
        }

        @Override
        public RequestAction build() {
            requireNotEmpty("action list", actions);
            return new RequestAction(this);
        }
    }

    private RequestAction(Builder builder) {
        this.actions = builder.actions;
    }

    public static RequestActionSyntax actions() {
        return new Builder();
    }

    @Override
    public Sc2Api.Request toSc2Api() {
        return Sc2Api.Request.newBuilder()
                .setAction(Sc2Api.RequestAction.newBuilder()
                        .addAllActions(actions.stream().map(Action::toSc2Api).collect(toList()))
                        .build())
                .build();
    }

    public List<Action> getActions() {
        return new ArrayList<>(actions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestAction that = (RequestAction) o;

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
