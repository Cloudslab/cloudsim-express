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
import org.cloudbus.cloudsim.express.exceptions.CloudSimExpressRuntimeException;
import org.cloudbus.cloudsim.express.exceptions.constants.ErrorConstants.ErrorCode;
import org.cloudbus.cloudsim.express.handler.impl.BaseElementHandler;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.ProcessingElement;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.ProcessingElementProvisioner;
import org.cloudbus.cloudsim.provisioners.PeProvisioner;

import java.util.ArrayList;
import java.util.List;

import static org.cloudbus.cloudsim.express.exceptions.constants.ErrorConstants.ErrorCode.ELEMENT_NOT_AWARE_OF_SIMULATION;

/**
 * Creates and manages a CloudSim processing element.
 */
public class DefaultProcessingElementHandler extends BaseElementHandler {

    public static final String KEY_PROCESSING_ELEMENTS_LIST = "PROCESSING_ELEMENTS_LIST";

    protected ProcessingElement processingElementDescription;
    protected List<Pe> peList;

    @Override
    public void init(Object elementDescription, ExtensionsResolver extensionsResolver) {

        super.init(elementDescription, extensionsResolver);
        this.processingElementDescription = (ProcessingElement) elementDescription;
    }

    @Override
    public void handle() {

        this.peList = new ArrayList<>();
        ProcessingElementProvisioner provisionerDescription = processingElementDescription.getProcessingElementProvisioner();
        try {
            int id = 0;
            for (int i = 0; i < processingElementDescription.getVariant().getCopies(); i++) {
                id = isIdIncrementing(processingElementDescription.getId()) ? ++id
                        : Integer.parseInt(processingElementDescription.getId());
                PeProvisioner peProvisioner = (PeProvisioner) extensionsResolver.getExtension(
                        provisionerDescription.getVariant().getClassName(),
                        new Class[]{double.class},
                        new Object[]{provisionerDescription.getMips()}
                );

                this.peList.add((Pe) extensionsResolver.getExtension(
                        processingElementDescription.getVariant().getClassName(),
                        new Class[]{int.class, PeProvisioner.class},
                        new Object[]{id, peProvisioner},
                        getExtensionProperties(processingElementDescription.getVariant())
                ));
            }
        } catch (Exception e) {
            // TODO: 2022-03-28 handler error.
            throw new CloudSimExpressRuntimeException(ErrorCode.UNKNOWN_ERROR,
                    "Please refer to the stacktrace", e);
        }
    }

    @Override
    public boolean isSimulated() {

        throw new CloudSimExpressRuntimeException(ELEMENT_NOT_AWARE_OF_SIMULATION,
                "Cannot perform this task at this level. Please use a higher level element to handle this scenario");
    }

    @Override
    public boolean canHandle(Object elementDescription) {

        return elementDescription instanceof ProcessingElement;
    }

    @Override
    public Object getProperty(String key) {

        if (KEY_PROCESSING_ELEMENTS_LIST.equals(key)) {
            return this.peList;
        }

        return null;
    }
}
