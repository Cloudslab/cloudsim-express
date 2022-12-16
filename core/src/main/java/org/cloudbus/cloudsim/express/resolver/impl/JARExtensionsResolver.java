/*
 * CloudSim Express
 * Copyright (C) 2022  CloudsLab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.cloudbus.cloudsim.express.resolver.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.cloudbus.cloudsim.express.exceptions.CloudSimExpressRuntimeException;
import org.cloudbus.cloudsim.express.exceptions.constants.ErrorConstants;
import org.cloudbus.cloudsim.express.handler.ElementHandler;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;
import org.cloudbus.cloudsim.express.resolver.impl.helper.CloudSimExpressExtension;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JARExtensionsResolver implements ExtensionsResolver {

    List<String> elementHandlerClassesPriorityList;
    private URLClassLoader extensionsClassLoader;

    @Override
    public void init(File extensionsFolder, List<String> elementHandlerClassesPriorityList) {

        prepareClassloaderForExtensions(extensionsFolder);
        this.elementHandlerClassesPriorityList = elementHandlerClassesPriorityList;
    }

    public Object getExtension(String className, Class<?>[] constructorParameterTypes,
                               Object[] constructorParameters) {

        return getExtension(className, constructorParameterTypes, constructorParameters, null);
    }

    @Override
    public Object getExtension(String className, Class<?>[] constructorParameterTypes,
                               Object[] constructorParameters, List<Pair<String, String>> extensionProperties) {

        Class<?> clazz = getExtensionClass(className);
        try {
            Object instance = clazz.getConstructor(constructorParameterTypes)
                    .newInstance(constructorParameters);
            handleCloudSimExpressExtensions(clazz, instance, extensionProperties);
            return instance;
        } catch (IllegalAccessException | NoSuchMethodException | InstantiationException |
                 InvocationTargetException e) {
            // TODO: 2022-03-20 handle error
            throw new CloudSimExpressRuntimeException(ErrorConstants.ErrorCode.UNKNOWN_ERROR,
                    "Please refer to the stacktrace", e);
        }
    }


    @Override
    public Class<?> getExtensionClass(String className) {

        Class<?> clazz = null;
        try {
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                clazz = Class.forName(className, true, extensionsClassLoader);
            }
        } catch (ClassNotFoundException e) {
            // TODO: 2022-03-20 handle error
            throw new CloudSimExpressRuntimeException(ErrorConstants.ErrorCode.UNKNOWN_ERROR,
                    "Please refer to the stacktrace", e);
        }
        return clazz;
    }

    @Override
    public List<ElementHandler> getElementHandlers() {

        // Initialized priority list should not be mutable during the runtime.
        return new ArrayList<>(getPrioritizedElementHandlers(this.elementHandlerClassesPriorityList));
    }

    private void handleCloudSimExpressExtensions(Class<?> clazz, Object instance,
                                                 List<Pair<String, String>> extensionProperties) {

        if (isCloudSimExpressExtension(clazz)) {
            CloudSimExpressExtension extension = (CloudSimExpressExtension) instance;

            // Supply extension resolver.
            extension.setExtensionResolver(this);

            // Inject properties.
            if (extensionProperties != null && extensionProperties.size() > 0) {
                extension.setProperties(extensionProperties);
            }
        }
    }

    private boolean isCloudSimExpressExtension(Class<?> clazz) {
        return CloudSimExpressExtension.class.isAssignableFrom(clazz);
    }

    private List<ElementHandler> getPrioritizedElementHandlers(
            List<String> elementHandlerClassesPriorityList) {

        return elementHandlerClassesPriorityList.stream()
                .map(clsName -> (ElementHandler) getExtension(clsName, new Class[]{}, new Object[]{}))
                .collect(Collectors.toList());
    }

    private void prepareClassloaderForExtensions(File extensionsFolder) {

        List<File> jars = Arrays.asList(Objects.requireNonNull(extensionsFolder.listFiles()));
        URL[] files = jars.stream().map(file -> {
            if (!file.isFile() || !file.getName().contains(".jar")) {
                return null;
            }
            try {
                return file.toURI().toURL();
            } catch (Exception e) {
                // TODO: 2022-03-24 handle error
                throw new CloudSimExpressRuntimeException(ErrorConstants.ErrorCode.UNKNOWN_ERROR,
                        "Please refer to the stacktrace", e);
            }
        }).filter(Objects::nonNull).collect(Collectors.toList()).toArray(new URL[]{});

        URL[] urls = {};
        if (files.length > 0) {
            urls = (URL[]) files;
        }
        extensionsClassLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
    }
}
