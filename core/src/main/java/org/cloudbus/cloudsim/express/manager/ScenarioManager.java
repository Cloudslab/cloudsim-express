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

package org.cloudbus.cloudsim.express.manager;

import org.cloudbus.cloudsim.express.handler.ElementHandler;

/**
 * The ScenarioManager interface defines a manager. This manager overlooks creation of the simulation scenario.
 * Firstly, the manager is initialized with a special {@link ElementHandler}, which corresponds to the simulation
 * component that represents the system model of the simulation. For example, the following simulation script has two
 * simulation components; 'Zone' and 'GlobalDatacenterNetwork'. It also has the reserved component,
 * 'SimulationSystemModel'.
 *
 * <pre>{@code
 * Zone: &Zone
 *   datacenter: *Datacenter
 *   broker:
 *     variant:
 *       className: "org.cloudbus.cloudsim.DatacenterBroker"
 *   workloadGenerator:
 *     variant:
 *       className: "org.crunchycookie.research.distributed.computing.cloudsim.workload.impl.DummyWorkloadGenerator"
 * GlobalDatacenterNetwork:
 *   zoneCount: 2
 *   interZoneNetworkDescriptionFilePath: "./network-data.csv"
 *   zones:
 *     - <<: *Zone
 *       name: "REGIONAL_ZONE_SAOPAULO"
 * SimulationSystemModel:
 *   name: 'GlobalDatacenterNetwork'
 * }</pre>
 * <p>
 * The 'SimulationSystemModel' component points that the component corresponds to the simulation system model is
 * 'GlobalDatacenterNetwork' (i.e. value of the 'name' attribute). Therefore, the handler corresponds to
 * 'GlobalDatacenterNetwork' component is used to initialize the ScenarioManager.
 * <p>
 * Afterward, ScenarioManager builds the scenario through the supplied system model handler.
 *
 * @see org.cloudbus.cloudsim.express.manager.impl.DefaultScenarioManager
 */
public interface ScenarioManager {

    /**
     * Initializes with the {@link ElementHandler} corresponds to simulation system model.
     *
     * @param systemModelHandler {@link ElementHandler} corresponds to simulation system model.
     */
    void init(ElementHandler systemModelHandler);

    /**
     * Builds the scenario.
     */
    void build();

    /**
     * Makes a blocking API call until scenario completion, and then returns the status.
     *
     * @return true if successful completion. False, otherwise.
     */
    boolean waitForScenarioCompletion();
}
