package com.github.ocraft.s2client.protocol.syntax.debug;

import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

public interface DebugKillUnitSyntax {
    DebugKillUnitBuilder withTags(Tag... unitTag);

    DebugKillUnitBuilder of(Unit... units);
}
