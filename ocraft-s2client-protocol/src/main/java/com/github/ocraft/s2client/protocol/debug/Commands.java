package com.github.ocraft.s2client.protocol.debug;

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
