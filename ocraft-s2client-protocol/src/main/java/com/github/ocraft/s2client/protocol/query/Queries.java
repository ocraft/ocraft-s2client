package com.github.ocraft.s2client.protocol.query;

import com.github.ocraft.s2client.protocol.syntax.query.QueryAvailableAbilitiesSyntax;
import com.github.ocraft.s2client.protocol.syntax.query.QueryBuildingPlacementSyntax;

public interface Queries {

    static QueryPathingSyntax path() {
        return QueryPathing.path();
    }

    static QueryBuildingPlacementSyntax placeBuilding() {
        return QueryBuildingPlacement.placeBuilding();
    }

    static QueryAvailableAbilitiesSyntax availableAbilities() {
        return QueryAvailableAbilities.availableAbilities();
    }

}
