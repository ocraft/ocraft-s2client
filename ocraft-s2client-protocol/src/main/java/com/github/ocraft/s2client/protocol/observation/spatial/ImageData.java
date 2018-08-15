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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.spatial.Size2dI;
import com.google.protobuf.ByteString;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.Serializable;
import java.util.Arrays;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Immutables.copyOf;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.lang.String.format;

public final class ImageData implements Serializable {

    private static final long serialVersionUID = -6680326347092291741L;

    private final int bitsPerPixel;
    private final Size2dI size;
    private final byte[] data;
    private final int imageType;

    private ImageData(Common.ImageData sc2ApiImageData) {
        bitsPerPixel = tryGet(
                Common.ImageData::getBitsPerPixel, Common.ImageData::hasBitsPerPixel
        ).apply(sc2ApiImageData).orElseThrow(required("bits per pixel"));

        size = tryGet(
                Common.ImageData::getSize, Common.ImageData::hasSize
        ).apply(sc2ApiImageData).map(Size2dI::from).orElseThrow(required("size"));

        data = tryGet(
                Common.ImageData::getData, Common.ImageData::hasData
        ).apply(sc2ApiImageData).map(ByteString::toByteArray).orElseThrow(required("data"));

        int expectedSize = expectedSize();
        if (!imageSizeIsValid(expectedSize)) {
            throw new IllegalArgumentException(
                    format("expected image size [%d] is not equal actual size [%d]", expectedSize, data.length));
        }

        imageType = tryGetImageType();
    }

    private int expectedSize() {
        return size.getX() * size.getY() * bitsPerPixel / 8;
    }

    private boolean imageSizeIsValid(int expectedSize) {
        return expectedSize == data.length;
    }

    private int tryGetImageType() {
        switch (bitsPerPixel) {
            case 1:
                return BufferedImage.TYPE_BYTE_BINARY;
            case 8:
                return BufferedImage.TYPE_BYTE_GRAY;
            case 32:
                return BufferedImage.TYPE_4BYTE_ABGR;
            default:
                throw new IllegalArgumentException(
                        format("Unsupported image type with bits per pixel [%d]. Expected {1, 8, 32}.", bitsPerPixel));
        }
    }

    public static ImageData from(Common.ImageData sc2ApiImageData) {
        require("sc2api image data", sc2ApiImageData);
        return new ImageData(sc2ApiImageData);
    }

    public int getBitsPerPixel() {
        return bitsPerPixel;
    }

    public Size2dI getSize() {
        return size;
    }

    @JsonIgnore
    public byte[] getData() {
        return copyOf(data);
    }

    @JsonIgnore
    public BufferedImage getImage() {
        return convertToImage(data);
    }

    private BufferedImage convertToImage(byte[] imageBytes) {
        BufferedImage bufferedImage = new BufferedImage(size.getX(), size.getY(), imageType);
        byte[] imgData = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        System.arraycopy(imageBytes, 0, imgData, 0, imageBytes.length);
        return bufferedImage;
    }

    public int sample(Point2d point) {
        return data[(int) point.getX() + (size.getY() - 1 - (int) point.getY()) * size.getX()] & 0xFF;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageData imageData = (ImageData) o;

        return bitsPerPixel == imageData.bitsPerPixel &&
                size.equals(imageData.size) &&
                Arrays.equals(data, imageData.data);
    }

    @Override
    public int hashCode() {
        int result = bitsPerPixel;
        result = 31 * result + size.hashCode();
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
