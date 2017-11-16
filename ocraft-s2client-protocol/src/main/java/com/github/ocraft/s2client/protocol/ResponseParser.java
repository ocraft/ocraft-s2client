package com.github.ocraft.s2client.protocol;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.response.Response;
import com.github.ocraft.s2client.protocol.response.ResponseConverter;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.function.Function;

public class ResponseParser implements Function<byte[], Response> {
    @Override
    public Response apply(byte[] responseBytes) {
        try {
            return new ResponseConverter().apply(Sc2Api.Response.parseFrom(responseBytes));
        } catch (InvalidProtocolBufferException e) {
            throw new ProtocolException(e);
        }
    }
}
