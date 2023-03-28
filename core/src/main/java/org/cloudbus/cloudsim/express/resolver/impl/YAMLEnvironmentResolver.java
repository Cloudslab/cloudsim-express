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

package org.cloudbus.cloudsim.express.resolver.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloudbus.cloudsim.express.constants.ErrorConstants;
import org.cloudbus.cloudsim.express.constants.ScriptConstants;
import org.cloudbus.cloudsim.express.exceptions.CloudSimExpressRuntimeException;
import org.cloudbus.cloudsim.express.handler.ElementHandler;
import org.cloudbus.cloudsim.express.resolver.EnvironmentResolver;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.SimulationSystemModel;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * The YAMLEnvironmentResolver class represents a resolver that parses human-readable simulation script file written
 * in YAML.
 */
public class YAMLEnvironmentResolver implements EnvironmentResolver {

    public static final String MODEL_CLASS_NAME_PREFIX = "org.cloudbus.cloudsim.express.resolver.environment.definitions.model.";

    // SystemModelHandler is a special case of an element handler, which corresponds to the DTO that describes the
    // system model of the entire simulation.
    ElementHandler systemModelHandler;

    @Override
    public void init(File simulationScript, ExtensionsResolver extensionsResolver) {

        Object systemModelDTO;
        try (var stream = new FileInputStream(simulationScript)) {
            systemModelDTO = getSystemModelFromScript(stream);
        } catch (ClassNotFoundException e) {
            throw new CloudSimExpressRuntimeException(ErrorConstants.ErrorCode.SCRIPT_ERROR,
                    "Could not obtain system model due to a missing class", e);
        } catch (IOException e) {
            throw new CloudSimExpressRuntimeException(ErrorConstants.ErrorCode.SCRIPT_ERROR,
                    "Could not read the script file: " + simulationScript, e);
        }

        // Initialize corresponding scenario handler.
        initHandler(extensionsResolver, systemModelDTO);
    }

    @Override
    public ElementHandler getSystemModelHandler() {

        return this.systemModelHandler;
    }

    private void initHandler(ExtensionsResolver extensionsResolver, Object systemModelDTO) {

        extensionsResolver.getElementHandlers().stream()
                .filter(handler -> handler.canHandle(systemModelDTO))
                .findFirst()
                .ifPresentOrElse(handler -> {
                    handler.init(systemModelDTO, extensionsResolver);
                    this.systemModelHandler = handler;
                }, () -> {
                    throw new CloudSimExpressRuntimeException(ErrorConstants.ErrorCode.MISSING_HANDLER,
                            "Could not find a handler for the component: " + systemModelDTO.getClass());
                });
    }

    private Object getSystemModelFromScript(InputStream scriptFileStream) throws ClassNotFoundException {

        Object systemModelDTO;
        Yaml yaml = new Yaml();
        Map<String, Object> obj = yaml.load(scriptFileStream);
        ObjectMapper mapper = new ObjectMapper();

        // Capture system model information through the reserved component.
        SimulationSystemModel systemModelInformationDTO = mapper.convertValue(
                obj.get(ScriptConstants.SIMULATION_SYSTEM_MODEL_COMPONENT),
                SimulationSystemModel.class
        );

        Class<?> cls = Class.forName(MODEL_CLASS_NAME_PREFIX + systemModelInformationDTO.getName());
        systemModelDTO = mapper.convertValue(obj.get(cls.getSimpleName()), cls);
        return systemModelDTO;
    }
}
