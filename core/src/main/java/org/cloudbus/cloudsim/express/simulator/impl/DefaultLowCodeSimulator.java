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

package org.cloudbus.cloudsim.express.simulator.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cloudbus.cloudsim.express.exceptions.LowCodeSimulationRuntimeException;
import org.cloudbus.cloudsim.express.exceptions.constants.ErrorConstants;
import org.cloudbus.cloudsim.express.handler.ElementHandler;
import org.cloudbus.cloudsim.express.manager.ScenarioManager;
import org.cloudbus.cloudsim.express.manager.SimulationManager;
import org.cloudbus.cloudsim.express.resolver.EnvironmentResolver;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;
import org.cloudbus.cloudsim.express.resolver.impl.JARExtensionsResolver;
import org.cloudbus.cloudsim.express.simulator.LowCodeSimulator;

import java.io.File;
import java.io.FileReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DefaultLowCodeSimulator implements LowCodeSimulator {

    public static final String MODULE_SCENARIO_MANAGER = "module.scenario.manager";
    public static final String MODULE_SIMULATION_MANAGER = "module.simulation.manager";
    public static final String MODULE_ENVIRONMENT_RESOLVER = "module.environment.resolver";
    public static final String FOLDER_EXTENSIONS = "folder.extensions";
    public static final String SIMULATION_SCENARIO_FILE = "simulation.scenario.file";
    public static final String SIMULATION_SCENARIO_CLASS_NAME = "simulation.scenario.model.class";
    public static final String SIMULATION_OUTPUT_LOG_FOLDER = "simulation.output.log.folder";
    public static final String ELEMENT_HANDLER_PRIORITY_CLASS_PREFIX = "element.handler.priority.";
    private static final Logger logger = LogManager.getLogger(DefaultLowCodeSimulator.class);
    private SimulationManager simulationManager;

    @Override
    public void init(File configsFile) {

        logger.atInfo().log("Initializing the Low-code simulator...");

        Properties props = loadProps(configsFile);
        logger.atDebug().log(props.toString());

        ExtensionsResolver extensionsResolver = getExtensionsResolver(props);
        logger.atInfo().log("Using Extension Resolver: " + extensionsResolver.getClass());

        EnvironmentResolver environmentResolver = getEnvironmentResolver(props, extensionsResolver);
        logger.atInfo().log("Using Environment Resolver: " + environmentResolver.getClass());

        ElementHandler simulationHandler = environmentResolver.getSimulationHandler();
        logger.atInfo().log("Using Simulation Handler: " + simulationHandler.getClass());

        ScenarioManager scenarioManager = getScenarioManager(props, extensionsResolver,
                simulationHandler);
        logger.atInfo().log("Using Scenario Manager: " + scenarioManager.getClass());

        createAndInitSimulationManager(props, extensionsResolver, scenarioManager);
        logger.atInfo().log("Using Simulation Manager: " + this.simulationManager.getClass());
    }

    @Override
    public void simulate() {

        Instant start = Instant.now();
        simulationManager.run();
        logger.atInfo().log(
                "Low-code simulation is completed in " + Duration.between(start, Instant.now()).getSeconds()
                        + " seconds");
    }

    private void createAndInitSimulationManager(Properties props,
                                                ExtensionsResolver extensionsResolver,
                                                ScenarioManager scenarioManager) {

        simulationManager = (SimulationManager) extensionsResolver.getExtension(
                props.getProperty(MODULE_SIMULATION_MANAGER),
                new Class[]{},
                new Object[]{}
        );
        simulationManager.init(scenarioManager,
                new File(props.getProperty(SIMULATION_OUTPUT_LOG_FOLDER)));
    }

    private ScenarioManager getScenarioManager(Properties props,
                                               ExtensionsResolver extensionsResolver, ElementHandler simulationHandler) {

        ScenarioManager scenarioManager = (ScenarioManager) extensionsResolver.getExtension(
                props.getProperty(MODULE_SCENARIO_MANAGER), new Class[]{}, new Object[]{});
        scenarioManager.init(simulationHandler);
        return scenarioManager;
    }

    private EnvironmentResolver getEnvironmentResolver(Properties props,
                                                       ExtensionsResolver extensionsResolver) {

        EnvironmentResolver environmentResolver = (EnvironmentResolver) extensionsResolver.getExtension(
                props.getProperty(MODULE_ENVIRONMENT_RESOLVER), new Class[]{}, new Object[]{});
        environmentResolver.init(
                new File(props.getProperty(SIMULATION_SCENARIO_FILE)),
                props.getProperty(SIMULATION_SCENARIO_CLASS_NAME),
                extensionsResolver
        );
        return environmentResolver;
    }

    private ExtensionsResolver getExtensionsResolver(Properties props) {

        String extensionsFolderPath = props.getProperty(FOLDER_EXTENSIONS);
        List<String> prioritizedElementHandlerClassesList = new ArrayList<>();
        props.forEach((key, val) -> {
            if (key.toString().startsWith(ELEMENT_HANDLER_PRIORITY_CLASS_PREFIX)) {
                prioritizedElementHandlerClassesList.add((String) val);
            }
        });

        ExtensionsResolver extensionsResolver = new JARExtensionsResolver();
        extensionsResolver.init(new File(extensionsFolderPath), prioritizedElementHandlerClassesList);
        return extensionsResolver;
    }

    private Properties loadProps(File configsFile) {

        Properties props = new Properties();
        try {
            props.load(new FileReader(configsFile));
        } catch (Exception e) {
            // TODO: 2022-03-28 handle error
            throw new LowCodeSimulationRuntimeException(ErrorConstants.ErrorCode.UNKNOWN_ERROR,
                    "Please refer to the stacktrace", e);
        }
        return props;
    }
}
