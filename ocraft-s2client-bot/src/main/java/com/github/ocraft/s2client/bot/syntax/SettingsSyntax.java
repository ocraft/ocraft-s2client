package com.github.ocraft.s2client.bot.syntax;

/*-
 * #%L
 * ocraft-s2client-bot
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

import com.github.ocraft.s2client.protocol.game.ReplayInfo;
import com.github.ocraft.s2client.protocol.spatial.SpatialCameraSetup;

import java.nio.file.Path;

public interface SettingsSyntax extends GameParticipantSyntax, ReplaySyntax {

    /**
     * Used to load settings. Settings will be discovered in the following order:
     * <ol>
     * <li>If command line arguments are provided it will use them. Invoke binary with --help to see expected
     * arguments.</li>
     * <li>(Recommended) If the StarCraft II binary has been run the function will auto discover its location.</li>
     * </ol>
     *
     * @param args Provided in main signature.
     */
    SettingsSyntax loadSettings(String[] args);

    /**
     * Used to load ladder settings.
     *
     * @param args Provided in main signature.
     */
    SettingsSyntax loadLadderSettings(String[] args);

    /**
     * Specifies whether bots or replays onStep function should be run in parallel. If set to true make sure your
     * bots are thread-safe if they reach into shared code.
     *
     * @param value True to multi-thread, false otherwise.
     */
    SettingsSyntax setMultithreaded(Boolean value);

    /**
     * Specifies whether the game should run in realtime or not. If the game is running in real time that means the
     * coordinator is not stepping it forward. The game is running and your bot reaches into it asynchronously
     * to read state.
     *
     * @param value True to be realtime, false otherwise.
     */
    SettingsSyntax setRealtime(Boolean value);

    /**
     * Sets the number of game loops to run for each step.
     *
     * @param stepSize Number of game loops to run for each step.
     */
    SettingsSyntax setStepSize(Integer stepSize);

    /**
     * Sets the path to the StarCraft II binary.
     *
     * @param path Absolute file path.
     */
    SettingsSyntax setProcessPath(Path path);

    /**
     * Set the correct data version of a replay to allow faster replay loading. Saves a few seconds if replay is not
     * up to date. Works only in combination with correct process path set by "setProcessPath".
     *
     * @param version Look in "ocraft-s2client-protocol/src/main/resources/versions.json" for the property
     *                "data-hash". Or read it from
     *                {@link ReplayInfo#getDataVersion()}.
     */
    SettingsSyntax setDataVersion(String version);

    /**
     * Sets the timeout for network operations.
     *
     * @param timeoutInMillis timeout in milliseconds.
     */
    SettingsSyntax setTimeoutMS(Integer timeoutInMillis);

    /**
     * Sets the first port number to use. Subsequent port assignments are sequential.
     *
     * @param portStart First port number.
     */
    SettingsSyntax setPortStart(Integer portStart);

    /**
     * Indicates whether feature layers should be provided in the observation.
     *
     * @param settings Configuration of feature layer settings.
     * @see com.github.ocraft.s2client.protocol.spatial.SpatialCameraSetup
     */
    SettingsSyntax setFeatureLayers(SpatialCameraSetup settings);

    /**
     * Indicates whether render layers should be provided in the observation.
     *
     * @param settings Configuration of render layer settings.
     * @see com.github.ocraft.s2client.protocol.spatial.SpatialCameraSetup
     */
    SettingsSyntax setRender(SpatialCameraSetup settings);

    /**
     * Sets the game window dimensions.
     *
     * @param width  Width of game window.
     * @param height Height of game window.
     */
    SettingsSyntax setWindowSize(Integer width, Integer height);

    /**
     * Sets the game window location.
     *
     * @param x X position of game window.
     * @param y y position of game window.
     */
    SettingsSyntax setWindowLocation(Integer x, Integer y);

    /**
     * Uses generalized abilities where possible.
     * <p>
     * Example: BUILD_TECHLAB_BARRACKS, BUILD_TECHLAB_FACTORY, BUILD_TECHLAB_STARPORT ability ids are generalized to
     * BUILD_TECHLAB ability id in the observation.
     */
    SettingsSyntax setUseGeneralizedAbilityId(Boolean value);

    /**
     * Enables logging on game server of all protocol requests/responses to std::err.
     */
    SettingsSyntax setVerbose(Boolean value);

    /**
     * By default the game is started from inside a support directory containing some needed libraries.
     * Set to {@code false} when working with the
     * <a href="https://github.com/Blizzard/s2client-proto#downloads">Linux packages</a>.
     */
    SettingsSyntax setNeedsSupportDir(Boolean value);

    /**
     * Enables logging on client of all protocol requests/responses to com.github.ocraft.s2client.api.log.DataFlowTracer
     * logger on level TRACE (JSON format).
     */
    SettingsSyntax setTraced(Boolean value);

    /**
     * Overrides the directory that temp files are created in.
     * <p>
     * Defaults to: /tmp/
     *
     * @param tmpDirPath Path to tmp dir.
     */
    SettingsSyntax setTmpDir(Path tmpDirPath);

    /**
     * Override the path to find the data package.
     * Required if the binary is not in the standard versions folder location.
     * <p>
     * Defaults to: ../../
     *
     * @param dataDirPath Path to data dir.
     */
    SettingsSyntax setDataDir(Path dataDirPath);

    /**
     * Sets the path the to software rendering library.
     * Required for using the rendered interface with software rendering
     * <p>
     * Example: /usr/lib/x86_64-linux-gnu/libOSMesa.so
     *
     * @param osMesaPath Path to OsMesa library.
     */
    SettingsSyntax setOsMesaPath(Path osMesaPath);

    /**
     * Sets the path the to hardare rendering library.
     * Required for using the rendered interface with hardware rendering.
     * <p>
     * Example: /usr/lib/nvidia-384/libEGL.so
     *
     * @param eglPath Path to egl library.
     */
    SettingsSyntax setEglPath(Path eglPath);

    /**
     * By default cloaked units are completely hidden. This shows some details.
     */
    SettingsSyntax setShowCloaked(Boolean showCloaked);

    /**
     * By default raw actions select, act and revert the selection. This is useful
     * if you're playing simultaneously with the agent so it doesn't steal your
     * selection. This inflates APM (due to deselect) and makes the actions hard
     * to follow in a replay. Setting this to true will cause raw actions to do
     * select, act, but not revert the selection.
     */
    SettingsSyntax setRawAffectsSelection(Boolean rawAffectsSelection);

    /**
     * Changes the coordinates in raw.proto to be relative to the playable area.
     * The map_size and playable_area will be the diagonal of the real playable area.
     */
    SettingsSyntax setRawCropToPlayableArea(Boolean rawCropToPlayableArea);
}
