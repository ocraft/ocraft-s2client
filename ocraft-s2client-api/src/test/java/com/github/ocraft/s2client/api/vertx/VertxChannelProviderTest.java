package com.github.ocraft.s2client.api.vertx;

import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

class VertxChannelProviderTest {

    @Test
    void emitsErrorOnDeployFailure() {
        VertxChannelProvider provider = new VertxChannelProvider();
        provider.start(null, 0);

        TestObserver<byte[]> observer = new TestObserver<>();
        provider.getChannel().errorStream().subscribe(observer);

        observer.awaitTerminalEvent(1000, TimeUnit.MILLISECONDS);
        observer.assertError(NullPointerException.class);
    }


}