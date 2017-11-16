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
package com.github.ocraft.s2client.protocol.response;

import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.GameStatus;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public abstract class Response implements Serializable {

    private static final long serialVersionUID = 7924460130955030895L;

    private final ResponseType type;
    private final GameStatus status;
    private final long nanoTime = System.nanoTime();

    Response(ResponseType type, GameStatus status) {
        requireNonNull(type, "response type must not be null");
        this.type = type;
        this.status = status != null ? status : GameStatus.UNKNOWN;
    }

    public ResponseType getType() {
        return type;
    }

    public GameStatus getStatus() {
        return status;
    }

    public long getNanoTime() {
        return nanoTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Response)) return false;

        Response response = (Response) o;

        return response.canEqual(this) && type == response.type && status == response.status;
    }

    public boolean canEqual(Object other) {
        return (other instanceof Response);
    }

    public <T extends Response> Optional<T> as(Class<T> clazz) {
        if (clazz.isInstance(this)) {
            return Optional.of(clazz.cast(this));
        } else {
            return Optional.empty();
        }
    }

    public static <T extends Response> Predicate<Response> is(Class<T> clazz) {
        return clazz::isInstance;
    }

    public static <T extends Response> Predicate<Response> isNot(Class<T> clazz) {
        return response -> !is(clazz).test(response);
    }

    public boolean is(ResponseType responseType) {
        return responseType.equals(getType());
    }

    public boolean isNot(ResponseType responseType) {
        return !is(responseType);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
