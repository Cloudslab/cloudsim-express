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

package org.cloudbus.cloudsim.express.resolver.impl.helper;

import org.apache.commons.lang3.tuple.Pair;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;

import java.util.List;

/**
 * This defines an extension for low code simulation.
 */
public interface LowCodeSimulationExtension {

    /**
     * Provide properties from the simulation description.
     *
     * @param properties Properties described in the description file.
     */
    void setProperties(List<Pair<String, String>> properties);

    /**
     * Provide the reference to the extension resolver, because extensions might want to load
     * additional instances of custom classes.
     *
     * @param resolver Reference to the resolver.
     */
    void setExtensionResolver(ExtensionsResolver resolver);
}
