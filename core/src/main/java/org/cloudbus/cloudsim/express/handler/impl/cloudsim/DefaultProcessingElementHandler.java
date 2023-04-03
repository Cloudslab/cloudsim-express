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
import org.cloudbus.cloudsim.express.exceptions.CloudSimExpressRuntimeException;
import org.cloudbus.cloudsim.express.handler.impl.BaseElementHandler;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.Extension;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.ProcessingElement;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.ProcessingElementProvisioner;
import org.cloudbus.cloudsim.provisioners.PeProvisioner;

import java.util.ArrayList;
import java.util.List;

import static org.cloudbus.cloudsim.express.constants.ErrorConstants.ErrorCode.ELEMENT_NOT_AWARE_OF_SIMULATION;

/**
 * This class handles the {@link ProcessingElement} DTO.
 */
public class DefaultProcessingElementHandler extends BaseElementHandler {

    public static final String KEY_PROCESSING_ELEMENTS_LIST = "PROCESSING_ELEMENTS_LIST";

    protected ProcessingElement processingElementDTO;
    protected List<Pe> processingElementsList;

    @Override
    public void init(Object componentDTO, ExtensionsResolver resolver) {

        super.init(componentDTO, resolver);
        this.processingElementDTO = (ProcessingElement) componentDTO;
    }

    @Override
    public void handle() {

        this.processingElementsList = new ArrayList<>();
        ProcessingElementProvisioner provisionerDTO = processingElementDTO.getProcessingElementProvisioner();
        Extension processingElementVariant = processingElementDTO.getVariant();

        int id = 0;
        for (int i = 0; i < processingElementVariant.getCopies(); i++) {
            id = isIdIncrementing(processingElementDTO.getId()) ?
                    ++id : Integer.parseInt(processingElementDTO.getId());
            PeProvisioner provisioner = (PeProvisioner) resolver.getExtension(
                    provisionerDTO.getVariant().getClassName(),
                    new Class[]{double.class},
                    new Object[]{provisionerDTO.getMips()}
            );

            this.processingElementsList.add((Pe) resolver.getExtension(
                    processingElementVariant.getClassName(),
                    new Class[]{int.class, PeProvisioner.class},
                    new Object[]{id, provisioner},
                    getExtensionProperties(processingElementVariant)
            ));
        }
    }

    @Override
    public boolean isSimulated() {

        // A processing element itself cannot be simulated.
        throw new CloudSimExpressRuntimeException(ELEMENT_NOT_AWARE_OF_SIMULATION,
                "Cannot simulate a Processing Element. Please use a higher level element to handle this scenario");
    }

    @Override
    public boolean canHandle(Object componentDTO) {

        return componentDTO instanceof ProcessingElement;
    }

    @Override
    public Object getProperty(String key) {

        if (KEY_PROCESSING_ELEMENTS_LIST.equals(key)) {
            return this.processingElementsList;
        }

        return null;
    }
}
