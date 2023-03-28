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

import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.express.exceptions.CloudSimExpressRuntimeException;
import org.cloudbus.cloudsim.express.handler.ElementHandler;
import org.cloudbus.cloudsim.express.handler.impl.BaseElementHandler;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.Extension;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.Host;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;

import java.util.ArrayList;
import java.util.List;

import static org.cloudbus.cloudsim.express.constants.ErrorConstants.ErrorCode.ELEMENT_NOT_AWARE_OF_SIMULATION;

/**
 * This class handles the {@link Host} DTO.
 */
public class DefaultHostHandler extends BaseElementHandler {

    public static final String KEY_HOST_LIST = "HOSTS_LIST";
    protected static final String RAM_PROVISIONER_PROPS_RAM = "ram";
    protected static final String BW_PROVISIONER_PROPS_BW = "bw";

    protected Host hostDTO;
    protected List<org.cloudbus.cloudsim.Host> hostList;

    @Override
    public void init(Object componentDTO, ExtensionsResolver resolver) {

        super.init(componentDTO, resolver);
        this.hostDTO = (Host) componentDTO;
    }

    @Override
    public void handle() {

        this.hostList = new ArrayList<>();
        Extension hostVariant = hostDTO.getVariant();
        int id = 0;
        for (int i = 0; i < hostVariant.getCopies(); i++) {
            id = isIdIncrementing(String.valueOf(hostDTO.getId())) ? ++id
                    : hostDTO.getId();
            int storage = hostDTO.getStorage();

            Extension ramProvisionerDetails = hostDTO.getRamProvisioner();
            RamProvisioner ramProvisioner = getRamProvisioner(ramProvisionerDetails);

            Extension bwProvisionerDetails = hostDTO.getBwProvisioner();
            BwProvisioner bwProvisioner = getBwProvisioner(bwProvisionerDetails);

            List<Pe> processingElementList = new ArrayList<>();
            populateProcessingElementList(processingElementList);

            Extension vmSchedulerDetails = hostDTO.getVmScheduler();
            VmScheduler vmScheduler = getVmScheduler(processingElementList, vmSchedulerDetails);

            populateHostList(hostVariant, id, ramProvisioner, bwProvisioner, storage,
                    processingElementList, vmScheduler);
        }
    }

    @Override
    public boolean isSimulated() {

        // A host itself cannot be simulated.
        throw new CloudSimExpressRuntimeException(ELEMENT_NOT_AWARE_OF_SIMULATION,
                "Cannot simulate a Host. Please use a higher level component");
    }

    @Override
    public boolean canHandle(Object componentDTO) {

        return componentDTO instanceof Host;
    }

    @Override
    public Object getProperty(String key) {

        if (KEY_HOST_LIST.equals(key)) {
            return this.hostList;
        }

        return null;
    }

    protected void populateHostList(Extension hostVariant, int id, RamProvisioner ramProvisioner,
                                    BwProvisioner bwProvisioner, int storage, List<Pe> processingElementList,
                                    VmScheduler vmScheduler) {
        this.hostList.add((org.cloudbus.cloudsim.Host) resolver.getExtension(
                hostVariant.getClassName(),
                new Class[]{int.class, RamProvisioner.class, BwProvisioner.class, long.class,
                        List.class, VmScheduler.class},
                new Object[]{id, ramProvisioner, bwProvisioner, storage, processingElementList,
                        vmScheduler},
                getExtensionProperties(hostVariant)
        ));
    }

    protected VmScheduler getVmScheduler(List<Pe> processingElementList, Extension vmSchedulerDetails) {

        return (VmScheduler) resolver.getExtension(
                vmSchedulerDetails.getClassName(),
                new Class[]{List.class},
                new Object[]{processingElementList},
                getExtensionProperties(vmSchedulerDetails)
        );
    }

    protected void populateProcessingElementList(List<Pe> processingElementList) {

        hostDTO.getProcessingElementList().forEach(processingElement -> {
            ElementHandler processingElementHandler = getHandler(processingElement);
            processingElementHandler.init(processingElement, this.resolver);
            processingElementHandler.handle();
            processingElementList.addAll(
                    (List<Pe>) processingElementHandler.getProperty(DefaultProcessingElementHandler
                            .KEY_PROCESSING_ELEMENTS_LIST)
            );
        });
    }

    protected BwProvisioner getBwProvisioner(Extension bwProvisionerDetails) {

        return (BwProvisioner) resolver.getExtension(
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

        return (RamProvisioner) resolver.getExtension(
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
