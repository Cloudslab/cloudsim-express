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

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.express.handler.ElementHandler;
import org.cloudbus.cloudsim.express.handler.impl.BaseElementHandler;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.Broker;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.Extension;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.WorkloadGenerator;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.Zone;
import org.crunchycookie.research.distributed.computing.cloudsim.workload.CloudSimWorkloadGenerator;

import java.util.List;

/**
 * This class handles the {@link Zone} DTO.
 */
public class DefaultZoneHandler extends BaseElementHandler {

    public static final String KEY_ZONE_ID = "DATACENTER";
    public static final String KEY_ZONE_NAME = "ZONE_NAME";
    public static final String RECEIVED_CLOUDLETS = "RECEIVED_CLOUDLETS";

    protected Zone zoneDTO;
    protected int zoneId;
    protected String name;
    protected DatacenterBroker broker;
    protected CloudSimWorkloadGenerator generator;
    protected Datacenter datacenter;

    protected List<Cloudlet> completedCloudlets;

    @Override
    public void init(Object componentDTO, ExtensionsResolver resolver) {

        super.init(componentDTO, resolver);
        this.zoneDTO = (Zone) componentDTO;
    }

    @Override
    public void handle() {

        this.name = zoneDTO.getName();
        this.generator = getWorkloadGenerator();
        this.broker = getBroker();
        this.datacenter = getDatacenter();

        List<Cloudlet> cloudletList = generator.getCloudletsList();
        cloudletList.forEach(cloudlet -> cloudlet.setUserId(broker.getId()));
        // // TODO: 27/3/23 Broker ID should be handled by the zone handler, rather than passing it to the generator.
        List<Vm> vmList = generator.getVmList(broker.getId());

        // Submit to the broker.
        broker.submitVmList(vmList);
        broker.submitCloudletList(cloudletList);
    }

    @Override
    public boolean isSimulated() {

        // A blocking operation.
        this.completedCloudlets = this.broker.getCloudletReceivedList();

        return true;
    }

    @Override
    public boolean canHandle(Object componentDTO) {

        return componentDTO instanceof Zone;
    }

    @Override
    public Object getProperty(String key) {

        return switch (key) {
            case KEY_ZONE_ID -> this.zoneId;
            case KEY_ZONE_NAME -> this.name;
            case RECEIVED_CLOUDLETS -> this.completedCloudlets;
            default -> null;
        };
    }

    protected CloudSimWorkloadGenerator getWorkloadGenerator() {

        WorkloadGenerator generatorDTO = zoneDTO.getWorkloadGenerator();
        Extension generatorVariant = generatorDTO.getVariant();
        return (CloudSimWorkloadGenerator) this.getResolver().getExtension(
                generatorVariant.getClassName(),
                new Class[]{},
                new Object[]{},
                getExtensionProperties(generatorVariant)
        );
    }

    protected DatacenterBroker getBroker() {

        Broker brokerDTO = zoneDTO.getBroker();
        Extension brokerVariant = brokerDTO.getVariant();
        return (DatacenterBroker) this.getResolver().getExtension(
                brokerVariant.getClassName(),
                new Class[]{String.class},
                new Object[]{brokerDTO.getName()},
                getExtensionProperties(brokerVariant)
        );
    }

    protected Datacenter getDatacenter() {

        ElementHandler handler = getHandler(zoneDTO.getDatacenter());
        handler.init(zoneDTO.getDatacenter(), this.resolver);
        handler.handle();

        org.cloudbus.cloudsim.Datacenter datacenter = (org.cloudbus.cloudsim.Datacenter) handler.getProperty(
                DefaultDatacenterHandler.KEY_DATACENTER
        );
        this.zoneId = datacenter.getId();

        return datacenter;
    }
}
