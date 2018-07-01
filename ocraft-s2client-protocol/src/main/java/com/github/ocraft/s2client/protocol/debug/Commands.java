package com.github.ocraft.s2client.protocol.debug;

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

import com.github.ocraft.s2client.protocol.syntax.debug.*;

public interface Commands {

    interface Draw {
        static DebugTextSyntax text() {
            return DebugText.text();
        }

        static DebugLineSyntax line() {
            return DebugLine.line();
        }

        static DebugBoxSyntax box() {
            return DebugBox.box();
        }

        static DebugSphereSyntax sphere() {
            return DebugSphere.sphere();
        }
    }

    static DebugDrawSyntax draw() {
        return DebugDraw.draw();
    }

    static DebugCreateUnitSyntax createUnit() {
        return DebugCreateUnit.createUnit();
    }

    static DebugKillUnitSyntax killUnit() {
        return DebugKillUnit.killUnit();
    }

    static DebugTestProcessSyntax testProcess() {
        return DebugTestProcess.testProcess();
    }

    static DebugSetScoreSyntax setScore() {
        return DebugSetScore.setScore();
    }

    static DebugEndGameSyntax endGame() {
        return DebugEndGame.endGame();
    }

    static DebugSetUnitValueSyntax setUnitValue() {
        return DebugSetUnitValue.setUnitValue();
    }
}
