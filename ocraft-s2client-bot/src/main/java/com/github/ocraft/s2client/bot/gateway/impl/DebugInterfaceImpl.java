package com.github.ocraft.s2client.bot.gateway.impl;

/*-
 * #%L
 * ocraft-s2client-bot
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

import com.github.ocraft.s2client.bot.gateway.DebugInterface;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.action.Action;
import com.github.ocraft.s2client.protocol.action.raw.ActionRawCameraMove;
import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.debug.*;
import com.github.ocraft.s2client.protocol.request.RequestAction;
import com.github.ocraft.s2client.protocol.request.RequestDebug;
import com.github.ocraft.s2client.protocol.request.Requests;
import com.github.ocraft.s2client.protocol.response.ResponseAction;
import com.github.ocraft.s2client.protocol.response.ResponseDebug;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.spatial.PointI;
import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.util.ArrayList;
import java.util.List;

class DebugInterfaceImpl implements DebugInterface {

    private final ControlInterfaceImpl controlInterface;
    private final List<DebugText> texts = new ArrayList<>();
    private final List<DebugLine> lines = new ArrayList<>();
    private final List<DebugBox> boxes = new ArrayList<>();
    private final List<DebugSphere> spheres = new ArrayList<>();
    private final List<DebugCommand> commands = new ArrayList<>();
    private final List<Tag> unitsToKill = new ArrayList<>();
    private boolean setScore;
    private float score;
    private DebugEndGame.EndResult endgameResult;
    private boolean hasMoveCamera;
    private Point moveCamera;
    private boolean appTestSet;
    private DebugTestProcess.Test appTest;
    private int delayMs;

    DebugInterfaceImpl(ControlInterfaceImpl controlInterface) {
        this.controlInterface = controlInterface;
    }

    private ControlInterfaceImpl control() {
        return controlInterface;
    }

    @Override
    public DebugInterface debugTextOut(String out, Color color) {
        texts.add(DebugText.text().of(out).withColor(color).build());
        return this;
    }

    @Override
    public DebugInterface debugTextOut(String out, Point2d ptVirtual2d, Color color, int size) {
        texts.add(DebugText.text()
                .of(out)
                .withColor(color)
                .withSize(size)
                .on(Point.of(ptVirtual2d.getX(), ptVirtual2d.getY(), 0.0f))
                .build());
        return this;
    }

    @Override
    public DebugInterface debugTextOut(String out, Point pt3d, Color color, int size) {
        texts.add(DebugText.text()
                .of(out)
                .withColor(color)
                .withSize(size)
                .onMap(pt3d)
                .build());
        return this;
    }

    @Override
    public DebugInterface debugLineOut(Point p0, Point p1, Color color) {
        lines.add(DebugLine.line().of(p0, p1).withColor(color).build());
        return this;
    }

    @Override
    public DebugInterface debugBoxOut(Point p0, Point p1, Color color) {
        boxes.add(DebugBox.box().of(p0, p1).withColor(color).build());
        return this;
    }

    @Override
    public DebugInterface debugSphereOut(Point p, float r, Color color) {
        spheres.add(DebugSphere.sphere().on(p).withRadius(r).withColor(color).build());
        return this;
    }

    @Override
    public DebugInterface debugCreateUnit(UnitType unitType, PointI p, int playerId, int count) {
        return debugCreateUnit(unitType, Point2d.of(p.getX(), p.getY()), playerId, count);
    }

    @Override
    public DebugInterface debugCreateUnit(UnitType unitType, Point2d p, int playerId, int count) {
        commands.add(DebugCommand.command()
                .of(DebugCreateUnit.createUnit().ofType(unitType).forPlayer(playerId).on(p).withQuantity(count)));
        return this;
    }

    @Override
    public DebugInterface debugKillUnit(Unit unit) {
        unitsToKill.add(unit.getTag());
        return this;
    }

    @Override
    public DebugInterface debugShowMap() {
        commands.add(DebugCommand.command().of(DebugGameState.SHOW_MAP));
        return this;
    }

    @Override
    public DebugInterface debugEnemyControl() {
        commands.add(DebugCommand.command().of(DebugGameState.CONTROL_ENEMY));
        return this;
    }

    @Override
    public DebugInterface debugIgnoreFood() {
        commands.add(DebugCommand.command().of(DebugGameState.FOOD));
        return this;
    }

    @Override
    public DebugInterface debugIgnoreResourceCost() {
        commands.add(DebugCommand.command().of(DebugGameState.FREE));
        return this;
    }

    @Override
    public DebugInterface debugGiveAllResources() {
        commands.add(DebugCommand.command().of(DebugGameState.ALL_RESOURCES));
        return this;
    }

    @Override
    public DebugInterface debugGodMode() {
        commands.add(DebugCommand.command().of(DebugGameState.GOD));
        return this;
    }

    @Override
    public DebugInterface debugIgnoreMineral() {
        commands.add(DebugCommand.command().of(DebugGameState.MINERALS));
        return this;
    }

    @Override
    public DebugInterface debugNoCooldowns() {
        commands.add(DebugCommand.command().of(DebugGameState.COOLDOWN));
        return this;
    }

    @Override
    public DebugInterface debugGiveAllTech() {
        commands.add(DebugCommand.command().of(DebugGameState.TECH_TREE));
        return this;
    }

    @Override
    public DebugInterface debugGiveAllUpgrades() {
        commands.add(DebugCommand.command().of(DebugGameState.UPGRADE));
        return this;
    }

    @Override
    public DebugInterface debugFastBuild() {
        commands.add(DebugCommand.command().of(DebugGameState.FAST_BUILD));
        return this;
    }

    @Override
    public DebugInterface debugSetScore(float score) {
        this.setScore = true;
        this.score = score;
        return this;
    }

    @Override
    public DebugInterface debugEndGame(boolean victory) {
        if (victory) {
            endgameResult = DebugEndGame.EndResult.DECLARE_VICTORY;
        } else {
            endgameResult = DebugEndGame.EndResult.SURRENDER;
        }
        return this;
    }

    @Override
    public DebugInterface debugSetEnergy(float value, Unit unit) {
        commands.add(DebugCommand.command()
                .of(DebugSetUnitValue.setUnitValue().forUnit(unit).set(DebugSetUnitValue.UnitValue.ENERGY).to(value)));
        return this;
    }

    @Override
    public DebugInterface debugSetLife(float value, Unit unit) {
        commands.add(DebugCommand.command()
                .of(DebugSetUnitValue.setUnitValue().forUnit(unit).set(DebugSetUnitValue.UnitValue.LIFE).to(value)));
        return this;
    }

    @Override
    public DebugInterface debugSetShields(float value, Unit unit) {
        commands.add(DebugCommand.command()
                .of(DebugSetUnitValue.setUnitValue().forUnit(unit).set(DebugSetUnitValue.UnitValue.SHIELDS).to(value)));
        return this;
    }

    @Override
    public DebugInterface debugMoveCamera(PointI pos) {
        return debugMoveCamera(Point.of(pos.getX(), pos.getY(), 0.0f));
    }

    @Override
    public DebugInterface debugMoveCamera(Point pos) {
        this.hasMoveCamera = true;
        this.moveCamera = pos;
        return this;
    }

    @Override
    public DebugInterface debugTestApp(DebugTestProcess.Test appTest, int delayMs) {
        this.appTestSet = true;
        this.appTest = appTest;
        this.delayMs = delayMs;
        return this;
    }

    @Override
    public boolean sendDebug() {
        if (!texts.isEmpty() || !lines.isEmpty() || !boxes.isEmpty() || !spheres.isEmpty()) {
            commands.add(DebugCommand.command().of(
                    DebugDraw.draw()
                            .texts(texts.toArray(new DebugText[0]))
                            .lines(lines.toArray(new DebugLine[0]))
                            .boxes(boxes.toArray(new DebugBox[0]))
                            .spheres(spheres.toArray(new DebugSphere[0]))));
        }
        if (!unitsToKill.isEmpty()) {
            commands.add(DebugCommand.command().of(DebugKillUnit.killUnit().withTags(unitsToKill.toArray(new Tag[0]))));
        }
        if (setScore) {
            commands.add(DebugCommand.command().of(DebugSetScore.setScore().to(score)));
        }
        if (endgameResult != null) {
            commands.add(DebugCommand.command().of(DebugEndGame.endGame().withResult(endgameResult)));
        }
        if (appTestSet) {
            commands.add(DebugCommand.command().of(
                    DebugTestProcess.testProcess().with(appTest).delayInMillis(delayMs)));
        }

        if (commands.isEmpty() && !hasMoveCamera) return false;

        boolean debugResult = true;
        boolean cameraMoveResult = true;
        if (!commands.isEmpty()) {
            BuilderSyntax<RequestDebug> request = Requests.debug().with(commands.toArray(new DebugCommand[0]));
            debugResult = control()
                    .waitForResponse(control().proto().sendRequest(request))
                    .flatMap(response -> response.as(ResponseDebug.class))
                    .isPresent();
        }
        if (hasMoveCamera) {
            RequestAction request = Requests.actions().of(
                    Action.action().raw(ActionRawCameraMove.cameraMove().to(moveCamera))).build();
            cameraMoveResult = control()
                    .waitForResponse(control().proto().sendRequest(request))
                    .flatMap(response -> response.as(ResponseAction.class))
                    .isPresent();
        }
        reset();

        return debugResult && cameraMoveResult;
    }

    private void reset() {
        commands.clear();
        texts.clear();
        lines.clear();
        boxes.clear();
        spheres.clear();
        unitsToKill.clear();
        setScore = false;
        score = 0.0f;
        endgameResult = null;
        appTestSet = false;
        appTest = null;
        delayMs = 0;
        hasMoveCamera = false;
        moveCamera = null;
    }
}
