/*
 * cloudsim-express
 * Copyright (C) 2023 CLOUDS Lab
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
 * The ExtensionsResolver loads extensions required for the simulation. The extensions are packaged as jars, and
 * available in a specific folder. The resolver loads them into the runtime, and provide APIs to create extension
 * objects.
 * <p/>
 * It further capable of providing custom properties for extensions implementing
 * {@link CloudSimExpressExtension} interface (See
 * {@link #getExtension(String, Class[], Object[], List)}). This can be useful to configure extensions via simulation
 * script file. For example, the following snippet shows an extended 'workloadGenerator', with two properties.
 * <pre>{@code
 * ...
 * workloadGenerator:
 *     variant:
 *       className: "org.crunchycookie.research.distributed.computing.cloudsim.workload.impl.DummyWorkloadGenerator"
 *       extensionProperties:
 *         - key: "workloadPercentage"
 *           value: "11"
 *         - key: "isRandomPercentage"
 *           value: "true"
 * ...
 * }</pre>
 * If the extension implements the said interface, these two properties will be supplied to it during instantiation.
 *
 * @see org.cloudbus.cloudsim.express.resolver.impl.JARExtensionsResolver
 */
public interface ExtensionsResolver {

    /**
     * Initialize the resolver with the location of the extensions.
     * <p/>
     * The element handlers are a special case of extensions. Users can supply multiple element handlers, and specify
     * the priority order in case there are multiple handlers for the same component. This priority order too, is
     * supplied during initialization.
     *
     * @param extensionsFolder            Folder with extension jars.
     * @param elementHandlerPriorityOrder A list in the order of priority of element handlers.
     */
    void init(File extensionsFolder, List<String> elementHandlerPriorityOrder);

    /**
     * Creates and returns an extension object.
     *
     * @param className                 Extension class name.
     * @param constructorParameterTypes Data types of the constructor's formal parameters.
     * @param constructorParameters     Arguments for the constructor's formal parameters.
     * @return The created extension object.
     */
    Object getExtension(String className, Class<?>[] constructorParameterTypes, Object[] constructorParameters);

    /**
     * Creates and returns an extension object. If the object is an instance of
     * {@link CloudSimExpressExtension}, then the provided
     * extension properties are also supplied via
     * {@link CloudSimExpressExtension#setProperties(List)}.
     *
     * @param className                 Extension class name.
     * @param constructorParameterTypes Data types of the constructor's formal parameters.
     * @param constructorParameters     Arguments for the constructor's formal parameters.
     * @param extensionProperties       The properties for the {@link CloudSimExpressExtension}.
     * @return The created extension object.
     */
    Object getExtension(String className, Class<?>[] constructorParameterTypes, Object[] constructorParameters,
                        List<Pair<String, String>> extensionProperties);

    /**
     * Returns the extension class for the extension class name.
     *
     * @param className Extension class name.
     * @return The class.
     */
    Class<?> getExtensionClass(String className);

    /**
     * Returns prioritized {@link ElementHandler}s.
     *
     * @return The list of {@link ElementHandler}s in their priority order.
     */
    List<ElementHandler> getElementHandlers();
}
