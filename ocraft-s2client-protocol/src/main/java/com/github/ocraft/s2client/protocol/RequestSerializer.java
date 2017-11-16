package com.github.ocraft.s2client.protocol;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.request.Request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Function;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

public class RequestSerializer implements Function<Request, byte[]> {
    @Override
    public byte[] apply(Request request) {
        if (!isSet(request)) return new byte[0];
        Sc2Api.Request sc2ApiRequest = request.toSc2Api();

        ByteArrayOutputStream output = new ByteArrayOutputStream(sc2ApiRequest.getSerializedSize());
        try {
            sc2ApiRequest.writeTo(output);
        } catch (IOException e) {
            throw new ProtocolException(e);
        }

        return output.toByteArray();
    }
}
