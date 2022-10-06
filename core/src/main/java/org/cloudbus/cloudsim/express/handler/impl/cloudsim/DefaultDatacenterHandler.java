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

package org.cloudbus.cloudsim.express.handler.impl.cloudsim;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.express.exceptions.LowCodeSimulationRuntimeException;
import org.cloudbus.cloudsim.express.exceptions.constants.ErrorConstants.ErrorCode;
import org.cloudbus.cloudsim.express.handler.ElementHandler;
import org.cloudbus.cloudsim.express.handler.impl.BaseElementHandler;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.Datacenter;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.Extension;

import java.util.LinkedList;
import java.util.List;

import static org.cloudbus.cloudsim.express.exceptions.constants.ErrorConstants.ErrorCode.ELEMENT_NOT_AWARE_OF_SIMULATION;

/**
 * Creates and manages a CloudSim processing element.
 */
public class DefaultDatacenterHandler extends BaseElementHandler {

    public static final String KEY_DATACENTER = "DATACENTER";

    protected Datacenter datacenterDescription;
    protected org.cloudbus.cloudsim.Datacenter datacenter;

    @Override
    public void init(Object elementDescription, ExtensionsResolver extensionsResolver) {

        super.init(elementDescription, extensionsResolver);
        this.datacenterDescription = (Datacenter) elementDescription;
    }

    @Override
    public void handle() {

        try {
            Extension datacenterVariant = datacenterDescription.getVariant();

            String name = datacenterDescription.getName();

            org.cloudbus.cloudsim.DatacenterCharacteristics characteristics = getDatacenterCharacteristics();
            List<Host> hostList = characteristics.getHostList();

            VmAllocationPolicy vmAllocationPolicy = getVmAllocationPolicy(hostList);

            LinkedList<Storage> storageList = new LinkedList<>();
            int schedulingInterval = datacenterDescription.getSchedulingInterval();

            createDatacenter(datacenterVariant, name, characteristics, vmAllocationPolicy, storageList,
                    schedulingInterval);
        } catch (Exception e) {
            // TODO: 2022-03-28 handler error.
            throw new LowCodeSimulationRuntimeException(ErrorCode.UNKNOWN_ERROR,
                    "Please refer to the stacktrace", e);
        }
    }

    @Override
    public boolean isSimulated() {

        throw new LowCodeSimulationRuntimeException(ELEMENT_NOT_AWARE_OF_SIMULATION,
                "Cannot perform this task at this level. Please use a higher level element to handle this scenario");
    }

    @Override
    public boolean canHandle(Object elementDescription) {

        return elementDescription instanceof Datacenter;
    }

    @Override
    public Object getProperty(String key) {

        if (KEY_DATACENTER.equals(key)) {
            return this.datacenter;
        }
        return null;
    }

    protected void createDatacenter(Extension datacenterVariant, String name,
                                    org.cloudbus.cloudsim.DatacenterCharacteristics characteristics,
                                    VmAllocationPolicy vmAllocationPolicy, LinkedList<Storage> storageList,
                                    int schedulingInterval) {
        datacenter = (org.cloudbus.cloudsim.Datacenter) this.extensionsResolver.getExtension(
                datacenterVariant.getClassName(),
                new Class[]{String.class, org.cloudbus.cloudsim.DatacenterCharacteristics.class,
                        VmAllocationPolicy.class, List.class, double.class},
                new Object[]{name, characteristics, vmAllocationPolicy, storageList, schedulingInterval},
                getExtensionProperties(datacenterVariant)
        );
    }

    protected VmAllocationPolicy getVmAllocationPolicy(List<Host> hostList) {
        Extension vmAllocationPolicyExtension = datacenterDescription.getVmAllocationPolicy();
        VmAllocationPolicy vmAllocationPolicy = (VmAllocationPolicy) this.extensionsResolver.getExtension(
                vmAllocationPolicyExtension.getClassName(),
                new Class[]{List.class},
                new Object[]{hostList},
                getExtensionProperties(vmAllocationPolicyExtension)
        );
        return vmAllocationPolicy;
    }

    protected org.cloudbus.cloudsim.DatacenterCharacteristics getDatacenterCharacteristics() {
        ElementHandler characteristicsHandler = getHandler(datacenterDescription.getCharacteristics());
        characteristicsHandler.init(datacenterDescription.getCharacteristics(),
                this.extensionsResolver);
        characteristicsHandler.handle();
        org.cloudbus.cloudsim.DatacenterCharacteristics characteristics = (org.cloudbus.cloudsim.DatacenterCharacteristics) characteristicsHandler.getProperty(
                DefaultDatacenterCharacteristicsHandler.KEY_DATACENTER_CHARACTERISTICS);
        return characteristics;
    }
}
