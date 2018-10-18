package com.github.ocraft.s2client.protocol;

/*-
 * #%L
 * ocraft-s2client-protocol
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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.stream;

public final class Versions {

    public static final String API_VERSION = "4.6.2.69232";

    private static Map<Integer, GameVersion> gameVersions = new HashMap<>();

    static {
        loadGameVersions();
    }

    private Versions() {
        throw new AssertionError("private constructor");
    }

    private static void loadGameVersions() {
        try {
            GameVersion[] versions = new ObjectMapper().readValue(
                    Versions.class.getClassLoader().getResourceAsStream("versions.json"),
                    GameVersion[].class);
            stream(versions).forEach(ver -> gameVersions.put(ver.getVersion(), ver));
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
    }

    public static Optional<GameVersion> versionFor(int baseBuild) {
        return Optional.ofNullable(gameVersions.get(baseBuild));
    }

}
