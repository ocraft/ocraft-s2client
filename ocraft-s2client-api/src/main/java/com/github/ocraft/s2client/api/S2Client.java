package com.github.ocraft.s2client.api;

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

import com.github.ocraft.s2client.api.controller.S2Controller;
import com.github.ocraft.s2client.api.log.DataFlowTracer;
import com.github.ocraft.s2client.api.syntax.OptionsSyntax;
import com.github.ocraft.s2client.api.syntax.S2ClientSyntax;
import com.github.ocraft.s2client.api.syntax.StartSyntax;
import com.github.ocraft.s2client.api.syntax.WithTracerSyntax;
import com.github.ocraft.s2client.api.vertx.VertxChannelProvider;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.RequestSerializer;
import com.github.ocraft.s2client.protocol.ResponseParser;
import com.github.ocraft.s2client.protocol.request.Request;
import com.github.ocraft.s2client.protocol.response.Response;
import com.github.ocraft.s2client.protocol.response.ResponseError;
import com.github.ocraft.s2client.protocol.response.ResponseType;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.MaybeSubject;
import io.reactivex.subscribers.DefaultSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.ocraft.s2client.api.OcraftApiConfig.CLIENT_BUFFER_SIZE_RESPONSE_STREAM;
import static com.github.ocraft.s2client.api.OcraftApiConfig.cfg;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.lang.String.format;

public class S2Client extends DefaultSubscriber<Response> {

    private final Logger log = LoggerFactory.getLogger(S2Client.class);

    private final ChannelProvider channelProvider = new VertxChannelProvider();
    private final Flowable<Response> responseStream;

    private final String connectToIp;
    private final Integer connectToPort;
    private final int requestTimeoutInMillis;
    private final int connectTimeoutInMillis;
    private final AtomicBoolean done = new AtomicBoolean(false);
    private final boolean traced;
    private final DataFlowTracer tracer;
    private final Phaser await = new Phaser(1);
    private final S2Controller game;

    public static class Builder implements S2ClientSyntax, OptionsSyntax, WithTracerSyntax {

        private String connectToIp = cfg().getString(OcraftApiConfig.CLIENT_NET_IP);
        private Integer connectToPort = cfg().getInt(OcraftApiConfig.CLIENT_NET_PORT);
        private boolean traced = cfg().getBoolean(OcraftApiConfig.CLIENT_TRACED);
        private int requestTimeoutInMillis = cfg().getInt(OcraftApiConfig.CLIENT_NET_SYNCH_REQUEST_TIMEOUT);
        private int connectTimeoutInMillis = cfg().getInt(OcraftApiConfig.CLIENT_NET_CONNECT_TIMEOUT);
        private S2Controller game;
        private DataFlowTracer tracer = new DataFlowTracer();
        private Runnable onConnectionLost;

        @Override
        public OptionsSyntax connectTo(String gameListenIp, Integer gameListenPort) {
            if (isSet(gameListenIp)) connectToIp = gameListenIp;
            if (isSet(gameListenPort)) connectToPort = gameListenPort;
            return this;
        }

        @Override
        public OptionsSyntax connectTo(S2Controller theGame) {
            require("game", theGame);
            this.connectToIp = theGame.getConfig().getString(OcraftApiConfig.GAME_NET_IP);
            this.connectToPort = theGame.getConfig().getInt(OcraftApiConfig.GAME_NET_PORT);
            this.game = theGame;
            return this;
        }

        @Override
        public OptionsSyntax requestTimeout(Integer timeoutInMillis) {
            if (isSet(timeoutInMillis)) this.requestTimeoutInMillis = timeoutInMillis;
            return this;
        }

        @Override
        public OptionsSyntax connectTimeout(Integer timeoutInMillis) {
            if (isSet(timeoutInMillis)) this.connectTimeoutInMillis = timeoutInMillis;
            return this;
        }

        @Override
        public OptionsSyntax onConnectionLost(Runnable callback) {
            if (isSet(callback)) this.onConnectionLost = callback;
            return this;
        }

        @Override
        public WithTracerSyntax traced(Boolean traced) {
            if (isSet(traced)) this.traced = traced;
            return this;
        }

        @Override
        public StartSyntax<S2Client> withTracer(DataFlowTracer tracer) {
            if (isSet(tracer)) this.tracer = tracer;
            return this;
        }

        @Override
        public S2Client start() {
            require("ip", connectToIp);
            require("port", connectToPort);
            if (traced) require("data flow tracer", tracer);
            return new S2Client(this);
        }

    }

    public static S2ClientSyntax starcraft2Client() {
        return new Builder();
    }

    private S2Client(Builder builder) {

        connectToIp = builder.connectToIp;
        connectToPort = builder.connectToPort;
        requestTimeoutInMillis = builder.requestTimeoutInMillis;
        connectTimeoutInMillis = builder.connectTimeoutInMillis;
        traced = builder.traced;
        tracer = builder.tracer;
        game = builder.game;

        log.info("Starting: {}", this);

        Channel channel = channelProvider.getChannel();
        channel.onConnectionLost(builder.onConnectionLost);
        responseStream = channel.outputStream().mergeWith(channel.errorStream())
                .map(this::prepareResponse)
                .toFlowable(BackpressureStrategy.ERROR)
                .onBackpressureBuffer(cfg().getInt(OcraftApiConfig.CLIENT_BUFFER_SIZE_RESPONSE_BACKPRESSURE))
                .observeOn(Schedulers.computation(), false, cfg().getInt(CLIENT_BUFFER_SIZE_RESPONSE_STREAM))
                .publish()
                .autoConnect()
                .doOnSubscribe(s -> await.register())
                .doOnCancel(await::arriveAndDeregister);

        responseStream().subscribe(this);
        await.arriveAndDeregister();

        Optional.ofNullable(game).ifPresent(s2Controller -> {
            responseStream().subscribe(s2Controller);
            await.arriveAndDeregister();
        });

        channelProvider.start(connectToIp, connectToPort, connectTimeoutInMillis);
    }

    private Response prepareResponse(byte[] responseBytes) {
        try {
            Response response = new ResponseParser().apply(responseBytes);
            if (traced) tracer.fire(response);
            return response;
        } catch (IllegalArgumentException e) {
            throw new ResponseParseException(e);
        }
    }

    public Flowable<Response> responseStream() {
        return responseStream;
    }

    public <T extends Request> void request(T requestData) {
        if (!done.get()) {
            require("request", requestData);
            if (traced) tracer.fire(requestData);
            channelProvider.getChannel().input(new RequestSerializer().apply(requestData));
        } else {
            throw new IllegalStateException("Client is already stopped.");
        }
    }

    public <T extends Request> void request(BuilderSyntax<T> requestDataBuilder) {
        request(requestDataBuilder.build());
    }

    public <T extends Request> Response requestSync(T requestData) {
        Maybe<Response> responseMaybe = waitForResponse(requestData.responseType());
        request(requestData);
        return responseMaybe.blockingGet();
    }

    public Maybe<Response> waitForResponse(ResponseType responseType) {
        MaybeSubject<Response> maybeSubject = MaybeSubject.create();
        responseStream()
                .filter(response -> response.is(responseType) || response.is(ResponseType.ERROR))
                .firstElement()
                .timeout(requestTimeoutInMillis, TimeUnit.MILLISECONDS)
                .subscribe(maybeSubject);
        return maybeSubject;
    }

    public <T extends Request> Response requestSync(BuilderSyntax<T> requestDataBuilder) {
        return requestSync(requestDataBuilder.build());
    }

    public <T extends Request, S extends Response> S requestSync(T requestData, Class<S> responseClass) {
        Response response = requestSync(requestData);
        response.as(ResponseError.class).ifPresent(responseError -> {
            throw ResponseErrorException.from(responseError);
        });

        return responseClass.cast(response);
    }

    public <T extends Request, S extends Response> S requestSync(
            BuilderSyntax<T> requestDataBuilder, Class<S> responseClass) {
        return requestSync(requestDataBuilder.build(), responseClass);
    }

    public boolean isDone() {
        return done.get();
    }

    public boolean stop() {
        log.debug("S2Client: STOP");
        if (done.compareAndSet(false, true)) {
            channelProvider.stop();
            return true;
        } else {
            return false;
        }
    }

    public boolean fullStop() {
        boolean status = stop();
        if (isSet(game)) {
            game.stop();
        }
        return status;
    }

    public boolean await() {
        await.arriveAndAwaitAdvance();
        return fullStop();
    }

    Channel channel() {
        return channelProvider.getChannel();
    }

    @Override
    public void onNext(Response response) {
        // Client is passive, nothing to do here.
    }

    @Override
    public void onError(Throwable throwable) {
        if (!(throwable instanceof ResponseParseException)) {
            stop();
        }
    }

    @Override
    public void onComplete() {
        stop();
    }

    public String getConnectToIp() {
        return connectToIp;
    }

    public Integer getConnectToPort() {
        return connectToPort;
    }

    public int getRequestTimeoutInMillis() {
        return requestTimeoutInMillis;
    }

    public int getConnectTimeoutInMillis() {
        return connectTimeoutInMillis;
    }

    public boolean isTraced() {
        return traced;
    }

    public S2Client untilReady() throws TimeoutException {
        return untilReady(() -> {
        });
    }

    public S2Client untilReady(Runnable onPull) throws TimeoutException {
        try {
            Boolean connectionResult = pullConnectionStatus(onPull);

            if (isDone()) {
                throw new IllegalStateException("Client is already closed.");
            }
            if (!connectionResult) {
                throw new IllegalStateException("Connection failed.");
            }
            return this;
        } catch (TimeoutException e) {
            throw new TimeoutException(format("connection timeout exceeded: %d ms", connectTimeoutInMillis));
        } catch (InterruptedException e) {
            log.debug("Thread was interrupted.", e);
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    private Boolean pullConnectionStatus(Runnable onPull)
            throws InterruptedException, ExecutionException, TimeoutException {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        try {
            CompletableFuture<Boolean> completionFuture = new CompletableFuture<>();
            final ScheduledFuture<?> checkFuture = executor.scheduleAtFixedRate(() -> {
                onPull.run();
                if (isDone() || channel().ready()) {
                    completionFuture.complete(true);
                }
            }, 0, 1, TimeUnit.SECONDS);
            return completionFuture
                    .whenComplete((result, thrown) -> checkFuture.cancel(true))
                    .exceptionally(throwable -> completionFuture.complete(false))
                    .get(connectTimeoutInMillis, TimeUnit.MILLISECONDS);
        } finally {
            executor.shutdownNow();
        }
    }

    @Override
    public String toString() {
        return "S2Client{" +
                "connectToIp='" + connectToIp + '\'' +
                ", connectToPort=" + connectToPort +
                ", requestTimeoutInMillis=" + requestTimeoutInMillis +
                ", connectTimeoutInMillis=" + connectTimeoutInMillis +
                ", done=" + done +
                ", traced=" + traced +
                '}';
    }
}
