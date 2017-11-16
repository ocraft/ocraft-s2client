package com.github.ocraft.s2client.protocol.syntax.action.spatial;

import com.github.ocraft.s2client.protocol.data.Ability;

public interface ActionSpatialUnitCommandSyntax {
    TargetSyntax useAbility(Ability ability);
}
