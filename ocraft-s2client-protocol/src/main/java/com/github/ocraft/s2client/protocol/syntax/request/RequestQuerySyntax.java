package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.query.QueryAvailableAbilities;
import com.github.ocraft.s2client.protocol.query.QueryBuildingPlacement;
import com.github.ocraft.s2client.protocol.query.QueryPathing;
import com.github.ocraft.s2client.protocol.query.QueryPathingBuilder;
import com.github.ocraft.s2client.protocol.request.RequestQuery;
import com.github.ocraft.s2client.protocol.syntax.query.QueryAvailableAbilitiesBuilder;
import com.github.ocraft.s2client.protocol.syntax.query.QueryBuildingPlacementBuilder;

public interface RequestQuerySyntax {
    RequestQuery.Builder ofPathings(QueryPathing... pathings);

    RequestQuery.Builder ofPathings(QueryPathingBuilder... pathings);

    RequestQuery.Builder ofAbilities(QueryAvailableAbilities... abilities);

    RequestQuery.Builder ofAbilities(QueryAvailableAbilitiesBuilder... abilities);

    RequestQuery.Builder ofPlacements(QueryBuildingPlacement... placements);

    RequestQuery.Builder ofPlacements(QueryBuildingPlacementBuilder... placements);
}
