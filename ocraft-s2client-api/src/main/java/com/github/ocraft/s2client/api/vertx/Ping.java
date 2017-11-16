package com.github.ocraft.s2client.api.vertx;

import com.github.ocraft.s2client.protocol.RequestSerializer;
import com.github.ocraft.s2client.protocol.ResponseParser;
import com.github.ocraft.s2client.protocol.response.Response;
import com.github.ocraft.s2client.protocol.response.ResponsePing;
import com.github.ocraft.s2client.protocol.response.ResponseType;
import io.reactivex.observers.DefaultObserver;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.http.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.ocraft.s2client.protocol.Versions.API_VERSION;
import static com.github.ocraft.s2client.protocol.request.Requests.ping;

class Ping extends DefaultObserver<Buffer> {

    private final Logger log = LoggerFactory.getLogger(Ping.class);

    private final WebSocket webSocket;
    private final Runnable onConnectionVerified;
    private final VertxChannel channel;

    Ping(VertxChannel channel, WebSocket webSocket, Runnable onConnectionVerified) {
        this.channel = channel;
        this.webSocket = webSocket;
        this.onConnectionVerified = onConnectionVerified;
    }

    @Override
    public void onStart() {
        webSocket.getDelegate().writeBinaryMessage(
                io.vertx.core.buffer.Buffer.buffer(new RequestSerializer().apply(ping())));
    }

    @Override
    public void onComplete() {
        // Called when websocket is closed. Nothing to do here.
    }

    @Override
    public void onError(Throwable e) {
        log.debug("Ping.onError", e);
        channel.error(e);
    }

    @Override
    public void onNext(Buffer buffer) {
        Response response = new ResponseParser().apply(buffer.getDelegate().getBytes());
        if (ResponseType.PING.equals(response.getType())) {
            String gameVersion = ((ResponsePing) response).getGameVersion();
            if (!API_VERSION.equals(gameVersion)) {
                log.warn("Ocraft uses sc2api in version {}. The game server uses {}.", API_VERSION, gameVersion);
            }
            cancel();
            onConnectionVerified.run();
        }
    }
}
