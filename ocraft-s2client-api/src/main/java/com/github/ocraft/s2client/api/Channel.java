package com.github.ocraft.s2client.api;

import io.reactivex.Observable;

public interface Channel {
    void input(byte[] inputBytes);

    void output(byte[] outputBytes);

    void error(Throwable error);

    Observable<byte[]> outputStream();

    Observable<byte[]> inputStream();

    Observable<byte[]> errorStream();
}
