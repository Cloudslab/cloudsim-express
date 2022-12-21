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

package org.cloudbus.cloudsim.express.simulator;

import java.io.File;

/**
 * The CloudSimExpress Simulator.
 * <p>
 * The CloudSimExpress simulator consumes a configuration file (hereafter knows as Configs) to initialize its internal
 * components, and executes the simulation scenario specified in the configs.
 */
public interface CloudSimExpressSimulator {

    /**
     * Consumes the Configs, and prepare the simulator.
     * <p>
     * The Configs is a standard properties file (.properties). The simplest example to execute the default scenario is described below. The inline comments
     * explain each config.
     * <pre>
     * # The simulator can load external extensions. The extensions are packaged into JARs and copied to the folder specified with this config.
     * folder.extensions=./extensions
     * # The specific simulation scenario is described using a human readable YAML file (hereafter known as YAML). The YAML is specified with this config.
     * simulation.scenario.file=./geo-distributed-datacenter-network.yaml
     * # The YAML describes simulation scenario with an object model (eg:A Datacenter network has multiple zones). Such object model can contain child object models to
     * # define its inner components (eg: A zone has a Datacenter). The object models are firstly defined in a separate file using human-readable YAML (See 'resources/simulation-elements.yaml').
     * # Then during the CloudSimExpress build, these object models are converted to JAVA model class files. Finally, the  JAVA model class file of the root simulation scenario is specified with the following config.
     * simulation.scenario.model.class=org.cloudbus.cloudsim.express.resolver.environment.definitions.model.GlobalDatacenterNetwork
     * # The log files (including CloudSim toolkit logs) are published to the folder specified with this config.
     * simulation.output.log.folder=./logs
     * # Following configs specify implementations of the extensible internal components.
     * module.environment.resolver=org.cloudbus.cloudsim.express.resolver.impl.YAMLEnvironmentResolver
     * module.scenario.manager=org.cloudbus.cloudsim.express.manager.impl.DefaultScenarioManager
     * module.simulation.manager=org.cloudbus.cloudsim.express.manager.impl.CloudSimSimulationManager
     * # For each object model, a handler is expected. During the runtime, the following priority list is examined to load the first handler that can handle the object model.
     * element.handler.priority.1=org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultDatacenterCharacteristicsHandler
     * element.handler.priority.2=org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultDatacenterHandler
     * element.handler.priority.3=org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultGlobalDatacenterNetworkHandler
     * element.handler.priority.4=org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultHostHandler
     * element.handler.priority.5=org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultProcessingElementHandler
     * element.handler.priority.6=org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultZoneHandler
     * </pre>
     *
     * @param configs Configs file.
     */
    void init(File configs);

    /**
     * Starts the simulation based on the Configs.
     */
    void simulate();
}
