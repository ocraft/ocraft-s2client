package com.github.ocraft.s2client.protocol.observation.spatial;

import SC2APIProtocol.Common;
import com.github.ocraft.s2client.protocol.Images;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

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
                .withMessage("Unsupported image type with bits per pixel [2]. Expected {1, 8, 32}.");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(ImageData.class)
                .withNonnullFields("size", "data")
                .withIgnoredFields("imageType")
                .withPrefabValues(
                        BufferedImage.class,
                        new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB),
                        new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB))
                .verify();
    }
}