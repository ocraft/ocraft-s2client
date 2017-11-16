/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
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
 */
package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.syntax.debug.DebugTestProcessBuilder;
import com.github.ocraft.s2client.protocol.syntax.debug.DebugTestProcessSyntax;
import com.github.ocraft.s2client.protocol.syntax.debug.DelaySyntax;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class DebugTestProcess implements Sc2ApiSerializable<Debug.DebugTestProcess> {

    private static final long serialVersionUID = -4936079485755485193L;

    private final Test test;
    private final int delayInMillis;

    public enum Test implements Sc2ApiSerializable<Debug.DebugTestProcess.Test> {
        HANG,
        CRASH,
        EXIT;

        @Override
        public Debug.DebugTestProcess.Test toSc2Api() {
            switch (this) {
                case HANG:
                    return Debug.DebugTestProcess.Test.hang;
                case CRASH:
                    return Debug.DebugTestProcess.Test.crash;
                case EXIT:
                    return Debug.DebugTestProcess.Test.exit;
                default:
                    throw new AssertionError("unknown test: " + this);
            }
        }
    }

    public static final class Builder implements DebugTestProcessSyntax, DelaySyntax {

        private Test test;
        private int delayInMillis;

        @Override
        public DelaySyntax with(Test test) {
            this.test = test;
            return this;
        }

        @Override
        public DebugTestProcessBuilder delayInMillis(int delayInMillis) {
            this.delayInMillis = delayInMillis;
            return this;
        }

        @Override
        public DebugTestProcess build() {
            require("test", test);
            return new DebugTestProcess(this);
        }

    }

    private DebugTestProcess(Builder builder) {
        test = builder.test;
        delayInMillis = builder.delayInMillis;
    }

    public static DebugTestProcessSyntax testProcess() {
        return new Builder();
    }

    @Override
    public Debug.DebugTestProcess toSc2Api() {
        return Debug.DebugTestProcess.newBuilder().setTest(test.toSc2Api()).setDelayMs(delayInMillis).build();
    }

    public Test getTest() {
        return test;
    }

    public int getDelayInMillis() {
        return delayInMillis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DebugTestProcess that = (DebugTestProcess) o;

        return delayInMillis == that.delayInMillis && test == that.test;
    }

    @Override
    public int hashCode() {
        int result = test.hashCode();
        result = 31 * result + delayInMillis;
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
