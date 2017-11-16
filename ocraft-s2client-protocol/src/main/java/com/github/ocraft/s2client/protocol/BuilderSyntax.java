package com.github.ocraft.s2client.protocol;

import java.util.Collection;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public interface BuilderSyntax<T> {
    T build();

    @SafeVarargs
    static <T> Collection<T> buildAll(BuilderSyntax<T>... builders) {
        return stream(builders).map(BuilderSyntax::build).collect(toList());
    }
}
