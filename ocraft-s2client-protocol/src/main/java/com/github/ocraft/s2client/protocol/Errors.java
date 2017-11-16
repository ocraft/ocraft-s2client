package com.github.ocraft.s2client.protocol;

import java.util.function.Supplier;

public final class Errors {

    private Errors() {
        throw new AssertionError("private constructor");
    }

    public static Supplier<IllegalArgumentException> required(String name) {
        return () -> new IllegalArgumentException(name + " is required");
    }

}
