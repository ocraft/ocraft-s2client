package com.github.ocraft.s2client.protocol.syntax.request;

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
