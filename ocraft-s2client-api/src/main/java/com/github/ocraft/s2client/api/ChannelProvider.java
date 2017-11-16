package com.github.ocraft.s2client.api;

public interface ChannelProvider {

    void start(String ip, int port);

    void stop();

    Channel getChannel();
}
