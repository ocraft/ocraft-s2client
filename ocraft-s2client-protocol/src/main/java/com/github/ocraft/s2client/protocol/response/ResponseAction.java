package com.github.ocraft.s2client.protocol.response;

/*-
 * #%L
 * ocraft-s2client-protocol
 * %%
 * Copyright (C) 2017 - 2018 Ocraft Project
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.action.ActionResult;
import com.github.ocraft.s2client.protocol.game.GameStatus;

import java.util.Collections;
import java.util.List;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public final class ResponseAction extends Response {

    private static final long serialVersionUID = 4533986173144519313L;

    private final List<ActionResult> results;

    private ResponseAction(Sc2Api.ResponseAction sc2ApiResponseAction, Sc2Api.Status status) {
        super(ResponseType.ACTION, GameStatus.from(status));
        results = sc2ApiResponseAction.getResultList().stream().map(ActionResult::from)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
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
        return results;
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
