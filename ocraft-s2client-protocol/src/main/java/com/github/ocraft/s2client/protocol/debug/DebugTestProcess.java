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
