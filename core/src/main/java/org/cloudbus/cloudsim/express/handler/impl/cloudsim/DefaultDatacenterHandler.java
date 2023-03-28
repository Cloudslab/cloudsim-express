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

package org.cloudbus.cloudsim.express.handler.impl.cloudsim;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.express.exceptions.CloudSimExpressRuntimeException;
import org.cloudbus.cloudsim.express.handler.ElementHandler;
import org.cloudbus.cloudsim.express.handler.impl.BaseElementHandler;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.Datacenter;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.Extension;

import java.util.LinkedList;
import java.util.List;

import static org.cloudbus.cloudsim.express.constants.ErrorConstants.ErrorCode.ELEMENT_NOT_AWARE_OF_SIMULATION;

/**
 * This class handles the {@link Datacenter} DTO.
 */
public class DefaultDatacenterHandler extends BaseElementHandler {

    public static final String KEY_DATACENTER = "DATACENTER";

    protected Datacenter datacenterDTO;
    protected org.cloudbus.cloudsim.Datacenter datacenter;

    @Override
    public void init(Object componentDTO, ExtensionsResolver resolver) {

        super.init(componentDTO, resolver);
        this.datacenterDTO = (Datacenter) componentDTO;
    }

    @Override
    public void handle() {

        Extension datacenterVariant = datacenterDTO.getVariant();

        String name = datacenterDTO.getName();

        org.cloudbus.cloudsim.DatacenterCharacteristics characteristics = getDatacenterCharacteristics();
        List<Host> hostList = characteristics.getHostList();

        VmAllocationPolicy vmAllocationPolicy = getVmAllocationPolicy(hostList);

        LinkedList<Storage> storageList = new LinkedList<>();
        int schedulingInterval = datacenterDTO.getSchedulingInterval();

        createDatacenter(datacenterVariant, name, characteristics, vmAllocationPolicy, storageList,
                schedulingInterval);
    }

    @Override
    public boolean isSimulated() {

        // Datacenter itself cannot be simulated.
        throw new CloudSimExpressRuntimeException(ELEMENT_NOT_AWARE_OF_SIMULATION,
                "Cannot simulate a " + KEY_DATACENTER + ". Please use a higher level component");
    }

    @Override
    public boolean canHandle(Object componentDTO) {

        return componentDTO instanceof Datacenter;
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

        datacenter = (org.cloudbus.cloudsim.Datacenter) this.resolver.getExtension(
                datacenterVariant.getClassName(),
                new Class[]{String.class, org.cloudbus.cloudsim.DatacenterCharacteristics.class,
                        VmAllocationPolicy.class, List.class, double.class},
                new Object[]{name, characteristics, vmAllocationPolicy, storageList, schedulingInterval},
                getExtensionProperties(datacenterVariant)
        );
    }

    protected VmAllocationPolicy getVmAllocationPolicy(List<Host> hostList) {

        Extension allocationPolicyVariant = datacenterDTO.getVmAllocationPolicy();
        return (VmAllocationPolicy) this.resolver.getExtension(
                allocationPolicyVariant.getClassName(),
                new Class[]{List.class},
                new Object[]{hostList},
                getExtensionProperties(allocationPolicyVariant)
        );
    }

    protected org.cloudbus.cloudsim.DatacenterCharacteristics getDatacenterCharacteristics() {

        ElementHandler characteristicsHandler = getHandler(datacenterDTO.getCharacteristics());
        characteristicsHandler.init(datacenterDTO.getCharacteristics(),
                this.resolver);
        characteristicsHandler.handle();
        return (org.cloudbus.cloudsim.DatacenterCharacteristics) characteristicsHandler.getProperty(
                DefaultDatacenterCharacteristicsHandler.KEY_DATACENTER_CHARACTERISTICS);
    }
}
