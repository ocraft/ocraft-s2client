package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.spatial.SpatialCameraSetup;

public interface MinimapSpatialSetupSyntax extends BuilderSyntax<SpatialCameraSetup> {
    /**
     *  Crop minimap to the playable area.
     */
    MinimapSpatialSetupSyntax cropToPlayableArea(Boolean value);

    /**
     * Return unit_type on the minimap, and potentially other cheating layers.
     */
    MinimapSpatialSetupSyntax allowCheatingLayers(Boolean value);
}
