package com.github.ocraft.test;

import org.junit.jupiter.api.extension.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.stream;

public class TemporaryFolderExtension implements AfterEachCallback, TestInstancePostProcessor, ParameterResolver {

    private final Collection<TemporaryFolder> tempFolders;

    public TemporaryFolderExtension() {
        tempFolders = new ArrayList<>();
    }

    @Override
    public void afterEach(ExtensionContext context) throws IOException {
        tempFolders.forEach(TemporaryFolder::cleanUp);
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
        stream(testInstance.getClass().getDeclaredFields())
                .filter(field -> field.getType() == TemporaryFolder.class)
                .forEach(field -> injectTemporaryFolder(testInstance, field));
    }

    private void injectTemporaryFolder(Object instance, Field field) {
        field.setAccessible(true);
        try {
            field.set(instance, createTempFolder());
        } catch (IllegalAccessException iae) {
            throw new RuntimeException(iae);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == TemporaryFolder.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return createTempFolder();
    }

    private TemporaryFolder createTempFolder() {
        TemporaryFolder result = new TemporaryFolder();
        result.prepare();
        tempFolders.add(result);
        return result;
    }

}