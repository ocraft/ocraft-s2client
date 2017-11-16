package com.github.ocraft.s2client.protocol.syntax.debug;

import com.github.ocraft.s2client.protocol.data.UnitType;

public interface DebugCreateUnitSyntax {
    ForPlayerSyntax ofType(UnitType type);
}
