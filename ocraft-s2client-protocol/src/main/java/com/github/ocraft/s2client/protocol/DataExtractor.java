package com.github.ocraft.s2client.protocol;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public final class DataExtractor {

    private DataExtractor() {
        throw new AssertionError("private constructor");
    }

    public static <T, U> Function<T, Optional<U>> tryGet(Function<T, U> extract, Predicate<T> checkIfExist) {
        return response -> Optional.ofNullable(response).filter(checkIfExist).map(extract);
    }
}
