package com.github.ocraft.s2client.bot.gateway.impl;

/*-
 * #%L
 * ocraft-s2client-bot
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

import com.github.ocraft.s2client.protocol.response.Response;
import com.github.ocraft.s2client.protocol.response.ResponseType;
import io.reactivex.Maybe;
import io.reactivex.subjects.MaybeSubject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class ResponseQueue {

    private Map<ResponseType, MaybeSubject<Response>> pending = new ConcurrentHashMap<>();

    boolean offer(ResponseType type, Maybe<Response> waitFor) {
        if (!pending.containsKey(type)) {
            if (waitFor instanceof MaybeSubject) {
                pending.put(type, (MaybeSubject<Response>) waitFor);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    boolean peek(ResponseType type) {
        return pending.containsKey(type);
    }

    boolean peek() {
        return !pending.isEmpty();
    }

    boolean peekResponse(ResponseType type) {
        return pending.containsKey(type) && pending.get(type).hasValue();
    }

    Maybe<Response> poll(ResponseType type) {
        Maybe<Response> responseMaybe = pending.getOrDefault(type, null);
        pending.remove(type);
        return responseMaybe;
    }

}
