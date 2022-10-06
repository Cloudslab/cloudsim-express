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

import org.cloudbus.cloudsim.express.exceptions.LowCodeSimulationRuntimeException;
import org.cloudbus.cloudsim.express.exceptions.constants.ErrorConstants;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;

/**
 * Scenarios are composed of multiple elements that are defined in the definition files. For
 * example, geo-distributed datacenter network is a composition of multiple regional zones. Also,
 * different scenarios share same elements but with different compositions. Therefore, an element is
 * handled with this class independently such that they can be reused in multiple scenarios.
 */
public interface ElementHandler {

    /**
     * Initialize the handler with the corresponding description.
     *
     * @param elementDescription Describes the element.
     * @param extensionsResolver Extensions resolver, that realizes any extension required.
     */
    void init(Object elementDescription, ExtensionsResolver extensionsResolver);

    /**
     * Perform element handling.
     */
    void handle();

    /**
     * Returns whether this element has undergone the simulation.
     *
     * @return True is the element has completed its simulation.
     */
    boolean isSimulated();

    /**
     * Checks whether the provided description object can be handled by this handler.
     *
     * @param elementDescription Element description.
     * @return True, if the element can be handled. False, otherwise.
     */
    boolean canHandle(Object elementDescription);

    /**
     * Obtain handler specific properties.
     *
     * @param key Identifies the property.
     * @return Corresponding value as an object.
     */
    Object getProperty(String key);

    /**
     * Supports hyper-parameter tuning by providing the objective function output.
     *
     * @return Output of the objective function based on the simulation.
     */
    default double getObjectiveFunctionOutput() {

        throw new LowCodeSimulationRuntimeException(ErrorConstants.ErrorCode.UNKNOWN_ERROR, "Method not implemented");
    }
}
