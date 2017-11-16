package com.github.ocraft.s2client.api.controller;

public class StarCraft2LaunchException extends RuntimeException {
    private static final long serialVersionUID = 7383556038700838299L;

    StarCraft2LaunchException(String message, Throwable cause) {
        super(message, cause);
    }

    StarCraft2LaunchException(Throwable cause) {
        super(cause);
    }

    StarCraft2LaunchException(String message) {
        super(message);
    }
}
