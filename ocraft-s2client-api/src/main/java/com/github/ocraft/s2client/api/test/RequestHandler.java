package com.github.ocraft.s2client.api.test;

/*-
 * #%L
 * ocraft-s2client-api
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

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.lang.String.format;

class RequestHandler {

    private ConcurrentLinkedDeque<Handler> handlers = new ConcurrentLinkedDeque<>();

    void addHandler(Predicate<Sc2Api.Request> condition, Supplier<Sc2Api.Response> responseSupplier) {
        handlers.add(new Handler(condition, responseSupplier));
    }

    class Handler {
        private Predicate<Sc2Api.Request> condition;
        private Supplier<Sc2Api.Response> responseSupplier;

        Handler(Predicate<Sc2Api.Request> condition, Supplier<Sc2Api.Response> responseSupplier) {
            require("condition", condition);
            require("response supplier", responseSupplier);
            this.condition = condition;
            this.responseSupplier = responseSupplier;
        }

        Predicate<Sc2Api.Request> getCondition() {
            return condition;
        }

        Supplier<Sc2Api.Response> getResponseSupplier() {
            return responseSupplier;
        }
    }

    Sc2Api.Response handle(Sc2Api.Request request) {
        Iterator<Handler> iterator = handlers.descendingIterator();
        while (iterator.hasNext()) {
            Handler handler = iterator.next();
            if (handler.getCondition().test(request)) {
                return handler.getResponseSupplier().get();
            }
        }
        throw new IllegalArgumentException(format("Could not handle request (%s)", request));
    }

}
