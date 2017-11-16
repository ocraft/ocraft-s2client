package com.github.ocraft.s2client.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public final class Strings {

    private static ObjectWriter writer;

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        writer = objectMapper.writer();
    }

    private Strings() {
        throw new AssertionError("private constructor");
    }

    public static String toJson(Object o) {
        try {
            return writer.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new ProtocolException(e);
        }
    }
}
