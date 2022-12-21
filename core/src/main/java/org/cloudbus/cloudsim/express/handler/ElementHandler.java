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

package org.cloudbus.cloudsim.express.handler;

import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;

/**
 * Handles an element for the simulation.
 * <p>
 * Scenarios are composed of multiple elements that are defined in the definition files. For
 * example, geo-distributed datacenter network is a composition of multiple regional zones. Also,
 * different scenarios share same elements but with different compositions. Each such element needs to have a handler,
 * that is responsible for how the element is handled in the simulation. See
 * {@link org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultDatacenterHandler} or any other default
 * implementations for references.
 *
 * @see org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultDatacenterHandler
 */
public interface ElementHandler {

    /**
     * Initialize the component with the human-readable description.
     *
     * @param elementDescription Element description from the simulation scenario YAML file.
     * @param extensionsResolver Extensions resolver, that create any dependant extensions required.
     */
    void init(Object elementDescription, ExtensionsResolver extensionsResolver);

    /**
     * Perform the logic on handling the element.
     *
     * @see org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultDatacenterHandler
     */
    void handle();

    /**
     * Returns whether this element has completed the simulation.
     *
     * @return True is the element has completed its simulation, False otherwise.
     */
    boolean isSimulated();

    /**
     * Checks whether this handler is able to handle the provided element.
     *
     * @param elementDescription Element description from the simulation scenario YAML file.
     * @return True, if the element can be handled. False, otherwise.
     */
    boolean canHandle(Object elementDescription);

    /**
     * Returns any requested information.
     *
     * @param key A key of the property. Maintaining the key is a responsibility of the developer.
     * @return Corresponding information.
     */
    Object getProperty(String key);
}
