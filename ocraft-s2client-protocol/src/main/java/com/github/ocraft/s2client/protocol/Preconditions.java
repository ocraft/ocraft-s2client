package com.github.ocraft.s2client.protocol;

import java.util.Collection;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static java.lang.String.format;
import static java.util.Arrays.stream;

public final class Preconditions {

    public static boolean isSet(Object o) {
        return o != nothing();
    }

    private Preconditions() {
        throw new AssertionError("private constructor");
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
        if (stream(objects).filter(Preconditions::isSet).count() == 0) {
            throw required("one of " + name).get();
        }
    }

    @SafeVarargs
    public static <T extends Collection> void oneOfIsNotEmpty(String name, T... objects) {
        require(name, objects);
        if (stream(objects).filter(c -> !c.isEmpty()).count() == 0) {
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

}
