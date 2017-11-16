package com.github.ocraft.s2client.protocol;

import java.util.function.Supplier;

public final class Constants {

    private Constants() {
        throw new AssertionError("private constructor");
    }

    public static <T> T nothing() {
        return null;
    }

    public static Supplier<AssertionError> errorOf(String errorMessage) {
        return () -> new AssertionError(errorMessage);
    }

}
