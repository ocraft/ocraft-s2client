package com.github.ocraft.s2client.protocol.observation.spatial;

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

import SC2APIProtocol.Common;
import com.github.ocraft.s2client.protocol.Images;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.google.protobuf.ByteString;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ImageDataTest {
    @Test
    void throwsExceptionWhenSc2ApiImageDataIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ImageData.from(nothing()))
                .withMessage("sc2api image data is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiImageData() {
        assertThatAllFieldsAreConverted(ImageData.from(sc2ApiImageData()));
    }

    private void assertThatAllFieldsAreConverted(ImageData imageData) {
        assertThat(imageData.getBitsPerPixel()).as("image data: bits per pixel").isEqualTo(BITS_PER_PIXEL);
        assertThat(imageData.getSize()).as("image data: size").isNotNull();
        assertThat(imageData.getData()).as("image data: data").isEqualTo(Images.HEIGHT_MAP.toByteArray());
        assertThatRenderedImageHasValidParameters(imageData);
    }

    private void assertThatRenderedImageHasValidParameters(ImageData imageData) {
        BufferedImage image = imageData.getImage();
        assertThat(image).as("image data: image").isNotNull();
        assertThat(image.getWidth()).as("image data: width").isEqualTo(SCREEN_SIZE_X);
        assertThat(image.getHeight()).as("image data: height").isEqualTo(SCREEN_SIZE_Y);
        assertThat(image.getColorModel().getPixelSize()).as("image data: pixel size")
                .isEqualTo(BITS_PER_PIXEL);
    }

    @Test
    void throwsExceptionWhenBitsPerPixelIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ImageData.from(without(
                        () -> sc2ApiImageData().toBuilder(),
                        Common.ImageData.Builder::clearBitsPerPixel).build()))
                .withMessage("bits per pixel is required");
    }

    @Test
    void throwsExceptionWhenSizeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ImageData.from(without(
                        () -> sc2ApiImageData().toBuilder(),
                        Common.ImageData.Builder::clearSize).build()))
                .withMessage("size is required");
    }

    @Test
    void throwsExceptionWhenImageIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ImageData.from(without(
                        () -> sc2ApiImageData().toBuilder(),
                        Common.ImageData.Builder::clearData).build()))
                .withMessage("data is required");
    }

    @Test
    void throwsExceptionWhenImageSizeIsInvalid() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ImageData.from(corruptedSc2ApiImageData()))
                .withMessage("expected image size [4096] is not equal actual size [1]");
    }

    @Test
    void throwsExceptionWhenImageTypeIsUnsupported() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ImageData.from(sc2ApiImageDataWithTwoBitPerPixel()))
                .withMessage("Unsupported image type with bits per pixel [2]. Expected {1, 8, 24, 32}.");
    }

    @Test
    void samplesPointOnImage() {
        ImageData imageData = ImageData.from(sc2ApiImageData());

        assertThat(imageData.sample(Point2d.of(57.0f, 63.0f))).as("image sample").isEqualTo(121);
    }

    @Test
    void fulfillsEqualsContract() throws UnsupportedEncodingException {
        EqualsVerifier
                .forClass(ImageData.class)
                .withNonnullFields("size", "data")
                .withIgnoredFields("imageType")
                .withPrefabValues(
                        ByteString.class,
                        ByteString.copyFrom("test", "UTF-8"),
                        ByteString.copyFrom("test2", "UTF-8"))
                .withPrefabValues(
                        BufferedImage.class,
                        new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB),
                        new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB))
                .verify();
    }
}
