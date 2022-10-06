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
 * Manages extensions supplied to the framework.
 */
public interface ExtensionsResolver {

    /**
     * Make the resolver aware about the folder where extensions reside, as well as the priority order
     * of the available element handlers.
     *
     * @param extensionsFolder                  Folder where extensions reside.
     * @param elementHandlerClassesPriorityList Priority order of the available element handlers
     */
    void init(File extensionsFolder, List<String> elementHandlerClassesPriorityList);

    /**
     * Get the extension instance by its class name.
     *
     * @param className                 The name of the class. It is assumed that the corresponding
     *                                  class has a constructor which accepts no arguments.
     * @param constructorParameterTypes Defines the parameters of the constructor.
     * @return The extension instance.
     */
    Object getExtension(String className, Class<?>[] constructorParameterTypes,
                        Object[] constructorParameters);

    /**
     * Get the extension instance by its class name, and supplying its parameters.
     *
     * @param className                 The name of the class. It is assumed that the corresponding
     *                                  class has a constructor which accepts no arguments.
     * @param constructorParameterTypes Defines the parameters of the constructor.
     * @param extensionProperties       Properties of the extension.
     * @return The extension instance.
     */
    Object getExtension(String className, Class<?>[] constructorParameterTypes,
                        Object[] constructorParameters, List<Pair<String, String>> extensionProperties);

    /**
     * Get the extension class by its class name.
     *
     * @param className The name of the class. It is assumed that the corresponding class has a
     *                  constructor which accepts no arguments.
     * @return The extension class.
     */
    Class<?> getExtensionClass(String className);

    /**
     * Return registered element handlers.
     *
     * @return Element handlers list in their prioritized order.
     */
    List<ElementHandler> getElementHandlers();
}
