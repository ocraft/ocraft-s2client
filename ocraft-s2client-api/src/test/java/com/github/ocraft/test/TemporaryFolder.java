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