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
import org.cloudbus.cloudsim.express.exceptions.LowCodeSimulationRuntimeException;
import org.cloudbus.cloudsim.express.exceptions.constants.ErrorConstants.ErrorCode;
import org.cloudbus.cloudsim.express.handler.ElementHandler;
import org.cloudbus.cloudsim.express.handler.impl.BaseElementHandler;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.DatacenterCharacteristics;

import java.util.ArrayList;
import java.util.List;

import static org.cloudbus.cloudsim.express.exceptions.constants.ErrorConstants.ErrorCode.ELEMENT_NOT_AWARE_OF_SIMULATION;

/**
 * Creates and manages a CloudSim processing element.
 */
public class DefaultDatacenterCharacteristicsHandler extends BaseElementHandler {

    public static final String KEY_DATACENTER_CHARACTERISTICS = "DATACENTER_CHARACTERISTICS";

    protected DatacenterCharacteristics datacenterCharacteristicsDescription;
    protected org.cloudbus.cloudsim.DatacenterCharacteristics datacenterCharacteristics;

    @Override
    public void init(Object elementDescription, ExtensionsResolver extensionsResolver) {

        super.init(elementDescription, extensionsResolver);
        this.datacenterCharacteristicsDescription = (DatacenterCharacteristics) elementDescription;
    }

    @Override
    public void handle() {

        try {
            String arch = datacenterCharacteristicsDescription.getArch();
            String os = datacenterCharacteristicsDescription.getOs();
            String vmm = datacenterCharacteristicsDescription.getVmm();
            double timeZone = datacenterCharacteristicsDescription.getTimeZone();
            double cost = datacenterCharacteristicsDescription.getCost();
            double costPerMemory =
                    datacenterCharacteristicsDescription.getCostPerMemory();
            double costPerStorage =
                    datacenterCharacteristicsDescription.getCostPerStorage();
            double costPerBandwidth =
                    datacenterCharacteristicsDescription.getCostPerBandwidth();

            List<Host> hostsList = new ArrayList<>();
            datacenterCharacteristicsDescription.getHostList().forEach(host -> {
                ElementHandler hostHandler = getHandler(host);
                hostHandler.init(host, extensionsResolver);
                hostHandler.handle();
                hostsList.addAll((List<Host>) hostHandler.getProperty(DefaultHostHandler.KEY_HOST_LIST));
            });

            this.datacenterCharacteristics = (org.cloudbus.cloudsim.DatacenterCharacteristics) this.extensionsResolver.getExtension(
                    org.cloudbus.cloudsim.DatacenterCharacteristics.class.getName(),
                    new Class[]{String.class, String.class, String.class, List.class, double.class,
                            double.class, double.class, double.class, double.class},
                    new Object[]{arch, os, vmm, hostsList, timeZone, cost, costPerMemory, costPerStorage,
                            costPerBandwidth}
            );
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

        return elementDescription instanceof DatacenterCharacteristics;
    }

    @Override
    public Object getProperty(String key) {

        if (KEY_DATACENTER_CHARACTERISTICS.equals(key)) {
            return this.datacenterCharacteristics;
        }
        return null;
    }
}
