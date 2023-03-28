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
import org.cloudbus.cloudsim.express.exceptions.CloudSimExpressRuntimeException;
import org.cloudbus.cloudsim.express.handler.ElementHandler;
import org.cloudbus.cloudsim.express.handler.impl.BaseElementHandler;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.DatacenterCharacteristics;

import java.util.ArrayList;
import java.util.List;

import static org.cloudbus.cloudsim.express.constants.ErrorConstants.ErrorCode.ELEMENT_NOT_AWARE_OF_SIMULATION;

/**
 * This class handles the {@link DatacenterCharacteristics} DTO.
 */
public class DefaultDatacenterCharacteristicsHandler extends BaseElementHandler {

    public static final String KEY_DATACENTER_CHARACTERISTICS = "DATACENTER_CHARACTERISTICS";

    protected DatacenterCharacteristics characteristicsDTO;
    protected org.cloudbus.cloudsim.DatacenterCharacteristics datacenterCharacteristics;

    @Override
    public void init(Object componentDTO, ExtensionsResolver resolver) {

        super.init(componentDTO, resolver);
        this.characteristicsDTO = (DatacenterCharacteristics) componentDTO;
    }

    @Override
    public void handle() {

        String arch = characteristicsDTO.getArch();
        String os = characteristicsDTO.getOs();
        String vmm = characteristicsDTO.getVmm();
        double timeZone = characteristicsDTO.getTimeZone();
        double cost = characteristicsDTO.getCost();
        double costPerMemory =
                characteristicsDTO.getCostPerMemory();
        double costPerStorage =
                characteristicsDTO.getCostPerStorage();
        double costPerBandwidth =
                characteristicsDTO.getCostPerBandwidth();

        List<Host> hostsList = new ArrayList<>();
        characteristicsDTO.getHostList().forEach(host -> {
            ElementHandler hostHandler = getHandler(host);
            hostHandler.init(host, resolver);
            hostHandler.handle();
            hostsList.addAll((List<Host>) hostHandler.getProperty(DefaultHostHandler.KEY_HOST_LIST));
        });

        this.datacenterCharacteristics = (org.cloudbus.cloudsim.DatacenterCharacteristics) this.resolver.getExtension(
                org.cloudbus.cloudsim.DatacenterCharacteristics.class.getName(),
                new Class[]{String.class, String.class, String.class, List.class, double.class,
                        double.class, double.class, double.class, double.class},
                new Object[]{arch, os, vmm, hostsList, timeZone, cost, costPerMemory, costPerStorage,
                        costPerBandwidth}
        );
    }

    @Override
    public boolean isSimulated() {

        // Datacenter characteristics itself cannot be simulated.
        throw new CloudSimExpressRuntimeException(ELEMENT_NOT_AWARE_OF_SIMULATION,
                "Cannot simulate a " + KEY_DATACENTER_CHARACTERISTICS + ". Please use a higher level component");
    }

    @Override
    public boolean canHandle(Object componentDTO) {

        return componentDTO instanceof DatacenterCharacteristics;
    }

    @Override
    public Object getProperty(String key) {

        if (KEY_DATACENTER_CHARACTERISTICS.equals(key)) {
            return this.datacenterCharacteristics;
        }
        return null;
    }
}
