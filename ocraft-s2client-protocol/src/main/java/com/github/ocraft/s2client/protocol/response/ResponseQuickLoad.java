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
