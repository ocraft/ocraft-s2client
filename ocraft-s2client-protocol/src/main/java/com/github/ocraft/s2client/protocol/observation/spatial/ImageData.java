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

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.lang.String.format;

public final class ImageData implements Serializable {

    private static final long serialVersionUID = -6095894861265443970L;

    private final int bitsPerPixel;
    private final Size2dI size;
    private final ByteString data;
    private final int imageType;

    public enum Origin {
        BOTTOM_LEFT,
        UPPER_LEFT
    }

    private ImageData(Common.ImageData sc2ApiImageData) {
        bitsPerPixel = tryGet(
                Common.ImageData::getBitsPerPixel, Common.ImageData::hasBitsPerPixel
        ).apply(sc2ApiImageData).orElseThrow(required("bits per pixel"));

        size = tryGet(
                Common.ImageData::getSize, Common.ImageData::hasSize
        ).apply(sc2ApiImageData).map(Size2dI::from).orElseThrow(required("size"));

        data = tryGet(
                Common.ImageData::getData, Common.ImageData::hasData
        ).apply(sc2ApiImageData).orElseThrow(required("data"));

        int expectedSize = expectedSize();
        if (!imageSizeIsValid(expectedSize)) {
            throw new IllegalArgumentException(
                    format("expected image size [%d] is not equal actual size [%d]", expectedSize, data.size()));
        }

        imageType = tryGetImageType();
    }

    private int expectedSize() {
        return size.getX() * size.getY() * bitsPerPixel / 8;
    }

    private boolean imageSizeIsValid(int expectedSize) {
        return expectedSize == data.size();
    }

    private int tryGetImageType() {
        switch (bitsPerPixel) {
            case 1:
                return BufferedImage.TYPE_BYTE_BINARY;
            case 8:
                return BufferedImage.TYPE_BYTE_GRAY;
            case 24:
                return BufferedImage.TYPE_3BYTE_BGR;
            case 32:
                return BufferedImage.TYPE_4BYTE_ABGR;
            default:
                throw new IllegalArgumentException(
                        format("Unsupported image type with bits per pixel [%d]. Expected {1, 8, 24, 32}.",
                                bitsPerPixel));
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
        return data.toByteArray();
    }

    @JsonIgnore
    public BufferedImage getImage() {
        return convertToImage(getData());
    }

    private BufferedImage convertToImage(byte[] imageBytes) {
        BufferedImage bufferedImage = new BufferedImage(size.getX(), size.getY(), imageType);
        byte[] imgData = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        System.arraycopy(imageBytes, 0, imgData, 0, imageBytes.length);
        return bufferedImage;
    }

    // TODO p.picheta refactor to version strategy
    public int sample(Point2d point, Origin origin) {
        int index;
        if (Origin.UPPER_LEFT.equals(origin)) {
            // Image data is stored with an upper left origin.
            index = (int) point.getX() + (size.getY() - 1 - (int) point.getY()) * size.getX();
        } else {
            // Image data is stored with an bottom left origin.
            index = (int) point.getX() + (int) point.getY() * size.getX();
        }

        if (bitsPerPixel == 1) {
            return (data.byteAt(index / 8) >> (index % 8)) & 0x1;
        } else {
            return data.byteAt(index) & 0xFF;
        }
    }

    public int sample(Point2d point) {
        return sample(point, Origin.UPPER_LEFT);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageData imageData = (ImageData) o;

        if (bitsPerPixel != imageData.bitsPerPixel) return false;
        if (!size.equals(imageData.size)) return false;
        return data.equals(imageData.data);
    }

    @Override
    public int hashCode() {
        int result = bitsPerPixel;
        result = 31 * result + size.hashCode();
        result = 31 * result + data.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}