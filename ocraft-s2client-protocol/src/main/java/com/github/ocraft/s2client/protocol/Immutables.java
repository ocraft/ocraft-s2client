package com.github.ocraft.s2client.protocol;

public final class Immutables {

    private Immutables() {
        throw new AssertionError("private constructor");
    }

    public static byte[] copyOf(byte[] array) {
        byte[] newArray = new byte[array.length];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;

    }

}
