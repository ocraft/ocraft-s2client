package com.github.ocraft.s2client.api;

import com.github.ocraft.s2client.api.controller.S2Controller;
import com.github.ocraft.s2client.api.log.DataFlowTracer;
import com.github.ocraft.s2client.api.syntax.S2ClientSyntax;
import com.github.ocraft.s2client.api.syntax.StartSyntax;
import com.github.ocraft.s2client.api.syntax.TracedSyntax;
import com.github.ocraft.s2client.api.syntax.WithTracerSyntax;
import com.github.ocraft.s2client.api.vertx.VertxChannelProvider;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.RequestSerializer;
import com.github.ocraft.s2client.protocol.ResponseParser;
import com.github.ocraft.s2client.protocol.request.Request;
import com.github.ocraft.s2client.protocol.response.Response;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DefaultSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.ocraft.s2client.api.OcraftConfig.CLIENT_BUFFER_SIZE_RESPONSE_STREAM;
import static com.github.ocraft.s2client.api.OcraftConfig.cfg;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public class S2Client extends DefaultSubscriber<Response> {

    private final Logger log = LoggerFactory.getLogger(S2Client.class);

    private final ChannelProvider channelProvider = new VertxChannelProvider();
    private final Flowable<Response> responseStream;

    private final String connectToIp;
    private final Integer connectToPort;
    private final AtomicBoolean done = new AtomicBoolean(false);
    private final boolean traced;
    private final DataFlowTracer tracer;
    private final Phaser await = new Phaser(1);
    private final S2Controller game;

    public static class Builder implements S2ClientSyntax, TracedSyntax, WithTracerSyntax {

        private String connectToIp;
        private Integer connectToPort;
        private boolean traced = cfg().getBoolean(OcraftConfig.CLIENT_TRACED);
        private S2Controller game;
        private DataFlowTracer tracer = new DataFlowTracer();

        @Override
        public TracedSyntax connectTo(String gameListenIp, int gameListenPort) {
            connectToIp = gameListenIp;
            connectToPort = gameListenPort;
            return this;
        }

        @Override
        public TracedSyntax connectTo(S2Controller theGame) {
            this.connectToIp = theGame.getConfig().getString(OcraftConfig.GAME_NET_IP);
            this.connectToPort = theGame.getConfig().getInt(OcraftConfig.GAME_NET_PORT);
            this.game = theGame;
            return this;
        }

        @Override
        public WithTracerSyntax traced(boolean traced) {
            this.traced = traced;
            return this;
        }

        @Override
        public StartSyntax<S2Client> withTracer(DataFlowTracer tracer) {
            this.tracer = tracer;
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
        traced = builder.traced;
        tracer = builder.tracer;
        game = builder.game;

        log.info("Starting: {}", this);

        Channel channel = channelProvider.getChannel();
        responseStream = channel.outputStream().mergeWith(channel.errorStream())
                .map(this::prepareResponse)
                .toFlowable(BackpressureStrategy.ERROR)
                .onBackpressureBuffer(cfg().getInt(OcraftConfig.CLIENT_BUFFER_SIZE_RESPONSE_BACKPRESSURE))
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

        channelProvider.start(connectToIp, connectToPort);
    }

    private Response prepareResponse(byte[] responseBytes) {
        Response response = new ResponseParser().apply(responseBytes);
        if (traced) tracer.fire(response);
        return response;
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

    public void await() {
        await.arriveAndAwaitAdvance();
        stop();
        if (isSet(game)) {
            game.stop();
        }
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
        stop();
    }

    @Override
    public void onComplete() {
        stop();
    }

    @Override
    public String toString() {
        return "S2Client{" +
                "connectToIp='" + connectToIp + '\'' +
                ", connectToPort=" + connectToPort +
                ", done=" + done +
                ", traced=" + traced +
                '}';
    }
}
