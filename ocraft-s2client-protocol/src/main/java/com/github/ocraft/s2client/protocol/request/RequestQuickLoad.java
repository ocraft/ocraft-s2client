/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
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
 */
package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;

public final class RequestQuickLoad extends Request {

    private static final long serialVersionUID = -6506844164479922929L;

    private RequestQuickLoad() {
    }

    public static RequestQuickLoad quickLoad() {
        return new RequestQuickLoad();
    }

    @Override
    public Sc2Api.Request toSc2Api() {
        return Sc2Api.Request.newBuilder()
                .setQuickLoad(Sc2Api.RequestQuickLoad.newBuilder().build())
                .build();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof RequestQuickLoad;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
