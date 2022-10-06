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

import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.express.exceptions.LowCodeSimulationRuntimeException;
import org.cloudbus.cloudsim.express.exceptions.constants.ErrorConstants.ErrorCode;
import org.cloudbus.cloudsim.express.handler.ElementHandler;
import org.cloudbus.cloudsim.express.handler.impl.BaseElementHandler;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.Extension;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.Host;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;

import java.util.ArrayList;
import java.util.List;

import static org.cloudbus.cloudsim.express.exceptions.constants.ErrorConstants.ErrorCode.ELEMENT_NOT_AWARE_OF_SIMULATION;

/**
 * Creates and manages a CloudSim processing element.
 */
public class DefaultHostHandler extends BaseElementHandler {

    public static final String KEY_HOST_LIST = "HOSTS_LIST";
    protected static final String RAM_PROVISIONER_PROPS_RAM = "ram";
    protected static final String BW_PROVISIONER_PROPS_BW = "bw";

    protected Host hostDescription;
    protected List<org.cloudbus.cloudsim.Host> hostList;

    @Override
    public void init(Object elementDescription, ExtensionsResolver extensionsResolver) {

        super.init(elementDescription, extensionsResolver);
        this.hostDescription = (Host) elementDescription;
    }

    @Override
    public void handle() {

        this.hostList = new ArrayList<>();
        Extension hostVariantDetails = hostDescription.getVariant();
        try {
            int id = 0;
            for (int i = 0; i < hostVariantDetails.getCopies(); i++) {
                id = isIdIncrementing(String.valueOf(hostDescription.getId())) ? ++id
                        : hostDescription.getId();
                int storage = hostDescription.getStorage();

                Extension ramProvisionerDetails = hostDescription.getRamProvisioner();
                RamProvisioner ramProvisioner = getRamProvisioner(ramProvisionerDetails);

                Extension bwProvisionerDetails = hostDescription.getBwProvisioner();
                BwProvisioner bwProvisioner = getBwProvisioner(bwProvisionerDetails);

                List<Pe> processingElementList = new ArrayList<>();
                populateProcessingElementList(processingElementList);

                Extension vmSchedulerDetails = hostDescription.getVmScheduler();
                VmScheduler vmScheduler = getVmScheduler(processingElementList, vmSchedulerDetails);

                populateHostList(hostVariantDetails, id, ramProvisioner, bwProvisioner, storage,
                        processingElementList, vmScheduler);
            }
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

        return elementDescription instanceof Host;
    }

    @Override
    public Object getProperty(String key) {

        if (KEY_HOST_LIST.equals(key)) {
            return this.hostList;
        }

        return null;
    }

    protected void populateHostList(Extension hostVariantDetails, int id, RamProvisioner ramProvisioner,
                                    BwProvisioner bwProvisioner, int storage, List<Pe> processingElementList,
                                    VmScheduler vmScheduler) {
        this.hostList.add((org.cloudbus.cloudsim.Host) extensionsResolver.getExtension(
                hostVariantDetails.getClassName(),
                new Class[]{int.class, RamProvisioner.class, BwProvisioner.class, long.class,
                        List.class, VmScheduler.class},
                new Object[]{id, ramProvisioner, bwProvisioner, storage, processingElementList,
                        vmScheduler},
                getExtensionProperties(hostVariantDetails)
        ));
    }

    protected VmScheduler getVmScheduler(List<Pe> processingElementList, Extension vmSchedulerDetails) {
        VmScheduler vmScheduler = (VmScheduler) extensionsResolver.getExtension(
                vmSchedulerDetails.getClassName(),
                new Class[]{List.class},
                new Object[]{processingElementList},
                getExtensionProperties(vmSchedulerDetails)
        );
        return vmScheduler;
    }

    protected void populateProcessingElementList(List<Pe> processingElementList) {
        hostDescription.getProcessingElementList().forEach(processingElement -> {
            ElementHandler processingElementHandler = getHandler(processingElement);
            processingElementHandler.init(processingElement, this.extensionsResolver);
            processingElementHandler.handle();
            processingElementList.addAll((List<Pe>) processingElementHandler.getProperty(
                    DefaultProcessingElementHandler.KEY_PROCESSING_ELEMENTS_LIST));
        });
    }

    protected BwProvisioner getBwProvisioner(Extension bwProvisionerDetails) {
        return (BwProvisioner) extensionsResolver.getExtension(
                bwProvisionerDetails.getClassName(),
                new Class[]{long.class},
                new Object[]{Integer.parseInt(
                        getPropertyValue(bwProvisionerDetails.getExtensionProperties(),
                                BW_PROVISIONER_PROPS_BW)
                )},
                getExtensionProperties(bwProvisionerDetails)
        );
    }

    protected RamProvisioner getRamProvisioner(Extension ramProvisionerDetails) {
        return (RamProvisioner) extensionsResolver.getExtension(
                ramProvisionerDetails.getClassName(),
                new Class[]{int.class},
                new Object[]{Integer.parseInt(
                        getPropertyValue(ramProvisionerDetails.getExtensionProperties(),
                                RAM_PROVISIONER_PROPS_RAM)
                )},
                getExtensionProperties(ramProvisionerDetails)
        );
    }
}
