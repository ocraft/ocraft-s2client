package com.github.ocraft.s2client.api.log;

import com.github.ocraft.s2client.protocol.request.Request;
import com.github.ocraft.s2client.protocol.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataFlowTracer {
    private final Logger log = LoggerFactory.getLogger(DataFlowTracer.class);

    public <T extends Request> void fire(T request) {
        log.trace("{}", request);
    }

    public <T extends Response> void fire(T response) {
        log.trace("{}", response);
    }
}
