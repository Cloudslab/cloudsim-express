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

package org.cloudbus.cloudsim.express.resolver;

import org.apache.commons.lang3.tuple.Pair;
import org.cloudbus.cloudsim.express.handler.ElementHandler;

import java.io.File;
import java.util.List;

/**
 * The resolver which manages loading externally provided extensions.
 */
public interface ExtensionsResolver {

    /**
     * Initialize the component with the folder that has extensions, as well as the priorities of
     * {@link ElementHandler} components.
     *
     * @param extensionsFolder                  Folder containing external extensions.
     * @param elementHandlerClassesPriorityList Priority order of the available element handlers.
     */
    void init(File extensionsFolder, List<String> elementHandlerClassesPriorityList);

    /**
     * Builds and returns an object of the given class and its constructor parameters.
     *
     * @param className                 The name of the class.
     * @param constructorParameterTypes Data types of the constructor parameters.
     * @param constructorParameters     Constructor parameters.
     * @return The instantiated object.
     */
    Object getExtension(String className, Class<?>[] constructorParameterTypes,
                        Object[] constructorParameters);

    /**
     * Builds and returns an object of the given class and its constructor parameters. If the object is an instance of
     * {@link org.cloudbus.cloudsim.express.resolver.impl.helper.CloudSimExpressExtension}, then the provided
     * extension properties are also supplied via
     * {@link org.cloudbus.cloudsim.express.resolver.impl.helper.CloudSimExpressExtension#setProperties(List)}.
     *
     * @param className                 The name of the class.
     * @param constructorParameterTypes Data types of the constructor parameters.
     * @param constructorParameters     Constructor parameters.
     * @param extensionProperties       The properties for the {@link org.cloudbus.cloudsim.express.resolver.impl.helper.CloudSimExpressExtension}.
     * @return The instantiated object.
     */
    Object getExtension(String className, Class<?>[] constructorParameterTypes,
                        Object[] constructorParameters, List<Pair<String, String>> extensionProperties);

    /**
     * Returns the corresponding class for the given class name.
     *
     * @param className The name of the class.
     * @return The class.
     */
    Class<?> getExtensionClass(String className);

    /**
     * Returns the registered {@link ElementHandler}s.
     *
     * @return The list of {@link ElementHandler}s in their priority order.
     */
    List<ElementHandler> getElementHandlers();
}
