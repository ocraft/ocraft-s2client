package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.game.BattlenetMap;
import com.github.ocraft.s2client.protocol.game.LocalMap;

public interface RequestCreateGameSyntax {
    WithPlayerSetupSyntax onBattlenetMap(BattlenetMap battlenetMap);

    WithPlayerSetupSyntax onLocalMap(LocalMap localMap);
}
