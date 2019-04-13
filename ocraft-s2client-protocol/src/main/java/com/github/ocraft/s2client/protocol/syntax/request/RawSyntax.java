package com.github.ocraft.s2client.protocol.syntax.request;

public interface RawSyntax extends ScoreSyntax {

    /**
     * By default raw actions select, act and revert the selection. This is useful
     * if you're playing simultaneously with the agent so it doesn't steal your
     * selection. This inflates APM (due to deselect) and makes the actions hard
     * to follow in a replay. Setting this to true will cause raw actions to do
     * select, act, but not revert the selection.
     */
    RawSyntax rawAffectsSelection(Boolean value);

    /**
     * Changes the coordinates in raw.proto to be relative to the playable area.
     * The map_size and playable_area will be the diagonal of the real playable area.
     */
    RawSyntax rawCropToPlayableArea(Boolean value);
}
