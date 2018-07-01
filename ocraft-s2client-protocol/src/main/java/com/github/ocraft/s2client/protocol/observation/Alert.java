package com.github.ocraft.s2client.protocol.observation;

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

import SC2APIProtocol.Sc2Api;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public enum Alert {
    NUCLEAR_LAUNCH_DETECTED,
    NYDUS_WORM_DETECTED;

    public static Alert from(Sc2Api.Alert sc2ApiAlert) {
        require("sc2api alert", sc2ApiAlert);
        switch (sc2ApiAlert) {
            case NuclearLaunchDetected:
                return NUCLEAR_LAUNCH_DETECTED;
            case NydusWormDetected:
                return NYDUS_WORM_DETECTED;
            default:
                throw new AssertionError("unknown alert: " + sc2ApiAlert);
        }
    }
}
