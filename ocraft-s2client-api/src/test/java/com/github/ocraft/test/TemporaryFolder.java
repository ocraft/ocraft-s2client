/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
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
 */
package com.github.ocraft.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import static java.lang.String.format;

public class TemporaryFolder {
    private Path rootFolder;

    public Path newFile(Path newFilePath, String fileName) {
        try {
            Path fileDir = rootFolder.resolve(newFilePath);
            Files.createDirectories(fileDir);
            File newFile = new File(fileDir.toFile(), fileName);
            if (!newFile.createNewFile()) {
                throw new IllegalArgumentException(format("File [%s] already exists", fileName));
            }
            return newFile.toPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Path newDir(Path newDirPath) {
        try {
            return Files.createDirectories(rootFolder.resolve(newDirPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void prepare() {
        try {
            rootFolder = Files.createTempDirectory("ocraft_tmp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void cleanUp() {
        if (!Files.exists(rootFolder)) return;
        try (Stream<Path> walk = Files.walk(rootFolder)) {
            walk.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Path getRootFolder() {
        return rootFolder;
    }

}