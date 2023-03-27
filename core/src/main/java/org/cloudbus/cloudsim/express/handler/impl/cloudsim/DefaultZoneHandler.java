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

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.express.exceptions.CloudSimExpressRuntimeException;
import org.cloudbus.cloudsim.express.exceptions.constants.ErrorConstants.ErrorCode;
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
 * Creates and manages a CloudSim processing element.
 */
public class DefaultZoneHandler extends BaseElementHandler {

    public static final String KEY_ZONE_ID = "DATACENTER";
    public static final String KEY_ZONE_NAME = "ZONE_NAME";
    public static final String RECEIVED_CLOUDLETS = "RECEIVED_CLOUDLETS";

    protected Zone zoneDescription;
    protected int zoneId;
    protected String name;
    protected DatacenterBroker broker;
    protected CloudSimWorkloadGenerator workloadGenerator;
    protected Datacenter datacenter;

    protected List<Cloudlet> receivedCloudlets;

    @Override
    public void init(Object elementDescription, ExtensionsResolver extensionsResolver) {

        super.init(elementDescription, extensionsResolver);
        this.zoneDescription = (Zone) elementDescription;
    }

    @Override
    public void handle() {

        try {
            this.name = zoneDescription.getName();

            this.workloadGenerator = initAndGetWorkloadGenerator();
            this.broker = getBroker();
            this.datacenter = getDatacenter();

            // Create a set of VMs equal to the number of cloudlets.
            // // TODO: 27/3/23 Broker ID should be handled by the zone handler, rather than passing it to the generator.
            List<Vm> vmList = workloadGenerator.getVmList(broker.getId());

            // Obtain cloudlet list meant for the zone.
            List<Cloudlet> cloudletList = workloadGenerator.getCloudletsList();
            cloudletList.forEach(cloudlet -> cloudlet.setUserId(broker.getId()));

            // Submit to the broker.
            broker.submitVmList(vmList);
            broker.submitCloudletList(cloudletList);
        } catch (Exception e) {
            throw new CloudSimExpressRuntimeException(ErrorCode.UNKNOWN_ERROR,
                    "Please refer to the stacktrace", e);
            // TODO: 2022-03-28 handler error.
        }
    }

    @Override
    public boolean isSimulated() {

        try {
            this.receivedCloudlets = this.broker.getCloudletReceivedList();
            return true;
        } catch (Exception e) {
            // TODO: 2022-03-29 handle error
            throw new CloudSimExpressRuntimeException(ErrorCode.UNKNOWN_ERROR,
                    "Please refer to the stacktrace", e);
        }
    }

    @Override
    public boolean canHandle(Object elementDescription) {

        return elementDescription instanceof Zone;
    }

    @Override
    public Object getProperty(String key) {

        return switch (key) {
            case KEY_ZONE_ID -> this.zoneId;
            case KEY_ZONE_NAME -> this.name;
            case RECEIVED_CLOUDLETS -> this.receivedCloudlets;
            default -> null;
        };
    }

    protected CloudSimWorkloadGenerator initAndGetWorkloadGenerator() {

        WorkloadGenerator workloadGeneratorDescription = zoneDescription.getWorkloadGenerator();
        Extension workloadGeneratorVariant = workloadGeneratorDescription.getVariant();
        CloudSimWorkloadGenerator workloadGenerator = (CloudSimWorkloadGenerator) this.getExtensionsResolver()
                .getExtension(
                        workloadGeneratorVariant.getClassName(),
                        new Class[]{},
                        new Object[]{},
                        getExtensionProperties(workloadGeneratorVariant)
                );
        return workloadGenerator;
    }

    protected DatacenterBroker getBroker() {
        Broker brokerDescription = zoneDescription.getBroker();
        return (DatacenterBroker) this.getExtensionsResolver().getExtension(
                brokerDescription.getVariant().getClassName(),
                new Class[]{String.class},
                new Object[]{brokerDescription.getName()},
                getExtensionProperties(brokerDescription.getVariant())
        );
    }

    protected Datacenter getDatacenter() {

        ElementHandler datacenterHandler = getHandler(zoneDescription.getDatacenter());
        datacenterHandler.init(zoneDescription.getDatacenter(), this.extensionsResolver);
        datacenterHandler.handle();
        org.cloudbus.cloudsim.Datacenter datacenter = (org.cloudbus.cloudsim.Datacenter) datacenterHandler.getProperty(
                DefaultDatacenterHandler.KEY_DATACENTER);
        this.zoneId = datacenter.getId();
        return datacenter;
    }
}
