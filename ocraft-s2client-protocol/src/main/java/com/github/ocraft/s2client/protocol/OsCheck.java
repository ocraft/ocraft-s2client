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

import java.util.Locale;

/**
 * Helper class to check the operating system this Java VM runs in
 */
public final class OsCheck {

    public enum OsType {
        WINDOWS,
        MAC_OS,
        LINUX,
        OTHER
    }

    // Cached result of OS detection
    private static OsType detectedOs;

    /**
     * Detect the operating system from the os.name System property and cache
     * the result
     *
     * @return - the operating system detected
     */
    public static OsType getOSType() {
        if (detectedOs == null) {
            String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if ((os.contains("mac")) || (os.contains("darwin"))) {
                detectedOs = OsType.MAC_OS;
            } else if (os.contains("win")) {
                detectedOs = OsType.WINDOWS;
            } else if (os.contains("nux")) {
                detectedOs = OsType.LINUX;
            } else {
                detectedOs = OsType.OTHER;
            }
        }
        return detectedOs;
    }
}
