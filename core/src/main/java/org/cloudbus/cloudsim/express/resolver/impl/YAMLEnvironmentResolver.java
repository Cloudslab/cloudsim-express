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

package org.cloudbus.cloudsim.express.resolver.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloudbus.cloudsim.express.exceptions.CloudSimExpressRuntimeException;
import org.cloudbus.cloudsim.express.exceptions.constants.ErrorConstants;
import org.cloudbus.cloudsim.express.handler.ElementHandler;
import org.cloudbus.cloudsim.express.resolver.EnvironmentResolver;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * Expects simulation description file to be written in YAML.
 */
public class YAMLEnvironmentResolver implements EnvironmentResolver {

    ExtensionsResolver extensionsResolver;
    ElementHandler simulationHandler;

    @Override
    public void init(File simulationScenarioDescription, String scenarioClassName,
                     ExtensionsResolver extensionsResolver) {

        this.extensionsResolver = extensionsResolver;

        Object simulationElement;
        try (InputStream input = new FileInputStream(simulationScenarioDescription)) {
            simulationElement = getSimulationElementFromYAML(scenarioClassName, input);
        } catch (Throwable e) {
            // TODO: 2022-03-18 handle error
            throw new CloudSimExpressRuntimeException(ErrorConstants.ErrorCode.UNKNOWN_ERROR,
                    "Please refer to the stacktrace", e);
        }

        findAndInitSimulationHandler(extensionsResolver, simulationElement);
    }

    @Override
    public ElementHandler getSimulationHandler() {

        return this.simulationHandler;
    }

    private void findAndInitSimulationHandler(ExtensionsResolver extensionsResolver,
                                              Object simulationElement) {

        extensionsResolver.getElementHandlers().stream()
                .filter(handler -> handler.canHandle(simulationElement))
                .findFirst()
                .ifPresentOrElse(handler -> {
                    handler.init(simulationElement, extensionsResolver);
                    this.simulationHandler = handler;
                }, () -> {
                    throw new RuntimeException();
                });
    }

    private Object getSimulationElementFromYAML(String scenarioClassName, InputStream input)
            throws ClassNotFoundException {

        Object simulationElement;
        Yaml yaml = new Yaml();
        Map<String, Object> obj = yaml.load(input);
        ObjectMapper mapper = new ObjectMapper();

        Class<?> cls = Class.forName(scenarioClassName);
        simulationElement = mapper.convertValue(obj.get(cls.getSimpleName()), cls);
        return simulationElement;
    }
}
