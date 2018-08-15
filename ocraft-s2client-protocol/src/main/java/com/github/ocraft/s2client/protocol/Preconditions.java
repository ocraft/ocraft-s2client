package com.github.ocraft.s2client.protocol;

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

import java.util.Collection;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static java.lang.String.format;
import static java.util.Arrays.stream;

public final class Preconditions {

    private Preconditions() {
        throw new AssertionError("private constructor");
    }

    public static boolean isSet(Object o) {
        return o != nothing();
    }

    public static <T> void require(String name, T argument) {
        if (!isSet(argument)) throw required(name).get();
    }

    public static <T extends Collection> void requireNotEmpty(String name, T argument) {
        require(name, argument);
        if (argument.isEmpty()) throw required(name).get();
    }

    public static <T extends String> void requireNotEmpty(String name, T argument) {
        require(name, argument);
        if (argument.isEmpty()) throw required(name).get();
    }

    public static void oneOfIsSet(String name, Object... objects) {
        require(name, objects);
        if (stream(objects).noneMatch(Preconditions::isSet)) {
            throw required("one of " + name).get();
        }
    }

    @SafeVarargs
    public static <T extends Collection> void oneOfIsNotEmpty(String name, T... objects) {
        require(name, objects);
        if (stream(objects).allMatch(Collection::isEmpty)) {
            throw required("one of " + name).get();
        }
    }

    public static <T extends Number> void between(String name, T actual, T from, T to) {
        double value = actual.doubleValue();
        double min = from.doubleValue();
        double max = to.doubleValue();
        if (value > max) {
            throw new IllegalArgumentException(format("%s has value %s and is greater than %s", name, actual, to));
        }
        if (value < min) {
            throw new IllegalArgumentException(format("%s has value %s and is lower than %s", name, actual, from));
        }
    }

    public static <T extends Number> void greaterOrEqual(String name, T actual, T min) {
        if (actual.doubleValue() < min.doubleValue()) {
            throw new IllegalArgumentException(format("%s has value %s and is lower than %s", name, actual, min));
        }
    }

    public static <T extends Number> void greaterThan(String name, T actual, T min) {
        if (actual.doubleValue() <= min.doubleValue()) {
            throw new IllegalArgumentException(format("%s has value %s and is not greater than %s", name, actual, min));
        }
    }

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static boolean isEmpty(Collection<?> value) {
        return value == null || value.isEmpty();
    }

    public static <T> boolean isEmpty(T[] value) {
        return value == null || value.length == 0;
    }
}
