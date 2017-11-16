package com.github.ocraft.s2client.protocol.syntax.action.observer;

import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

public interface ActionObserverCameraFollowUnitsSyntax {
    ActionObserverCameraFollowUnitsBuilder withTags(Tag... unitTag);

    ActionObserverCameraFollowUnitsBuilder of(Unit... units);
}
