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

package org.cloudbus.cloudsim.express.handler;

import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;

/**
 * The ElementHandler interface defines a handler that converts a simulation component written in human-readable
 * scripting language, to a simulation implementation for CloudSim, that is written in Java. For example, a datacenter
 * description written in YAML might look like this.
 * <pre>{@code
 *      ...
 *      Datacenter: &Datacenter
 *          variant:
 *              className: "org.cloudbus.cloudsim.Datacenter"
 *          characteristics: *Characteristics
 *          vmAllocationPolicy:
 *              className: "org.cloudbus.cloudsim.VmAllocationPolicySimple"
 *          storage: ""
 *          schedulingInterval: 0
 *      ...
 * }</pre>
 * The corresponding CloudSim implementation in Java partially looks similar to the one below.
 * <pre>{@code
 *      ...
 * 		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
 * 				arch, os, vmm, hostList, time_zone, cost, costPerMem,
 * 				costPerStorage, costPerBw);
 * 		Datacenter datacenter = null;
 * 		try {
 * 			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
 *      } catch (Exception e) {
 *          e.printStackTrace();
 *      }
 *      ...
 * }</pre>
 * A handler implementing this interface is responsible to perform such conversions.
 * <p/>
 * <p>Usage:</p>
 * <p>
 * Before using a simulation component in the script file (i.e. 'system-model.yaml'), the schematic of this component
 * (such as attributes and associated data types, etc) is defined in the 'simulation-elements.yaml' file. Afterwards,
 * a corresponding handler is implemented to convert scripted information to a Java-based CloudSim implementation
 * (available in the 'org.cloudbus.cloudsim.express.resolver.environment.definitions.model' package).
 * </p>
 *
 * @see org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultGlobalDatacenterNetworkHandler
 */
public interface ElementHandler {

    /**
     * Initialize the handler with simulation component information.
     *
     * @param componentDTO Simulation component DTO parsed from the scripted file.
     * @param resolver     {@link ExtensionsResolver} that creates objects from extension classes.
     */
    void init(Object componentDTO, ExtensionsResolver resolver);

    /**
     * Converts simulation component into a CloudSim implementation.
     *
     * @see org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultGlobalDatacenterNetworkHandler
     */
    void handle();

    /**
     * Returns whether the handler's CloudSim implementation completed the simulation. If the CloudSim implementation
     * is not able to obtain that information, this method throws a
     * {@link org.cloudbus.cloudsim.express.exceptions.CloudSimExpressRuntimeException} with a specific
     * {@link org.cloudbus.cloudsim.express.constants.ErrorConstants.ErrorCode#ELEMENT_NOT_AWARE_OF_SIMULATION} error
     * code.
     *
     * @return True if the CloudSim implementation has completed its simulation, False otherwise.
     */
    boolean isSimulated();

    /**
     * Returns if this handler is able to handle the provided simulation component DTO.
     *
     * @param componentDTO Simulation component DTO parsed from the scripted file.
     * @return True, if the component can be handled. False, otherwise.
     */
    boolean canHandle(Object componentDTO);

    /**
     * Returns additional information. This API allows external components to obtain any use case specific information
     * from the handler.
     *
     * @param key The identifier to the property.
     * @return The property.
     */
    Object getProperty(String key);
}
