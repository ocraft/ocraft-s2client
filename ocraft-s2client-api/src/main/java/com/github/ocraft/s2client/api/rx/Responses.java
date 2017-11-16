package com.github.ocraft.s2client.api.rx;

import com.github.ocraft.s2client.protocol.response.Response;
import com.github.ocraft.s2client.protocol.response.ResponseType;
import io.reactivex.functions.Predicate;

public final class Responses {

    private Responses() {
        throw new AssertionError("private constructor");
    }

    public static <T extends Response> Predicate<Response> is(Class<T> clazz) {
        return clazz::isInstance;
    }

    public static <T extends Response> Predicate<Response> isNot(Class<T> clazz) {
        return response -> !clazz.isInstance(response);
    }

    public static Predicate<Response> is(ResponseType responseType) {
        return response -> responseType.equals(response.getType());
    }

    public static Predicate<Response> isNot(ResponseType responseType) {
        return response -> !responseType.equals(response.getType());
    }
}
