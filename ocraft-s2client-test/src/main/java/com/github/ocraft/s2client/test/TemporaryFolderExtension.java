package com.github.ocraft.s2client.test;

/*-
 * #%L
 * ocraft-s2client-api
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
