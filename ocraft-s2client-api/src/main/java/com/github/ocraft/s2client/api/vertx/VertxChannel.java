package com.github.ocraft.s2client.api.vertx;

/*-
 * #%L
 * ocraft-s2client-api
 * %%
 * Copyright (C) 2017 - 2018 Ocraft Project
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import com.github.ocraft.s2client.api.Channel;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import io.vertx.reactivex.core.eventbus.EventBus;
import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.reactivex.core.eventbus.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.BufferOverflowException;
import java.util.UUID;

import static com.github.ocraft.s2client.api.OcraftConfig.*;

public class VertxChannel implements Channel {

    private final Logger log = LoggerFactory.getLogger(VertxChannel.class);

    private static final String OUTPUT_STREAM = "OUTPUT_" + UUID.randomUUID().toString();
    private static final String INPUT_STREAM = "INPUT_" + UUID.randomUUID().toString();

    private final Observable<byte[]> outputStream;
    private final Observable<byte[]> inputStream;
    private final Subject<byte[]> errorStream = PublishSubject.<byte[]>create().toSerialized();
    private final MessageProducer<byte[]> inputMessageProducer;
    private final MessageProducer<byte[]> outputMessageProducer;

    static VertxChannel from(EventBus eventBus) {
        return new VertxChannel(eventBus);
    }

    private VertxChannel(EventBus eventBus) {
        outputStream = initOutputStream(eventBus);
        inputStream = initInputStream(eventBus);

        outputMessageProducer = eventBus
                .<byte[]>publisher(OUTPUT_STREAM)
                .setWriteQueueMaxSize(cfg().getInt(CLIENT_BUFFER_SIZE_RESPONSE_EVENT_BUS));

        inputMessageProducer = eventBus
                .<byte[]>sender(INPUT_STREAM)
                .setWriteQueueMaxSize(cfg().getInt(CLIENT_BUFFER_SIZE_REQUEST_EVENT_BUS));
    }

    private Observable<byte[]> initOutputStream(EventBus eventBus) {
        log.debug("registering event bus consumer [{}] for output stream", OUTPUT_STREAM);
        return eventBus
                .<byte[]>consumer(OUTPUT_STREAM)
                .toObservable()
                .map(Message::body)
                .doOnComplete(errorStream::onComplete);
    }

    private Observable<byte[]> initInputStream(EventBus eventBus) {
        log.debug("registering event bus consumer [{}] for input stream", INPUT_STREAM);
        return eventBus.<byte[]>consumer(INPUT_STREAM).toObservable().map(Message::body);
    }

    @Override
    public void input(byte[] inputBytes) {
        if (!inputMessageProducer.writeQueueFull()) {
            inputMessageProducer.send(inputBytes);
        } else {
            throw new BufferOverflowException();
        }
    }

    @Override
    public void output(byte[] outputBytes) {
        if (!outputMessageProducer.writeQueueFull()) {
            outputMessageProducer.send(outputBytes);
        } else {
            throw new BufferOverflowException();
        }
    }

    @Override
    public void error(Throwable error) {
        errorStream.onError(error);
    }

    @Override
    public Observable<byte[]> outputStream() {
        return outputStream;
    }

    @Override
    public Observable<byte[]> inputStream() {
        return inputStream;
    }

    @Override
    public Observable<byte[]> errorStream() {
        return errorStream;
    }

}
