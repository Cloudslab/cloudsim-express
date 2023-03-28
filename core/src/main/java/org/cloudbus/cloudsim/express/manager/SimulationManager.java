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

import java.io.File;

/**
 * The SimulationManager executes the simulation. It uses {@link ScenarioManager} to build a scenario. Afterwards, it
 * executes the simulation and waits for the completion.
 * <p/>
 * Typically, a SimulationManager uses a simulation platform,
 * such as CloudSim. However, the interface is designed, such that internal implementation is completely in the hands
 * of the developer.
 *
 * @see org.cloudbus.cloudsim.express.manager.impl.CloudSimSimulationManager
 */
public interface SimulationManager {

    /**
     * Initialize the manager.
     *
     * @param scenarioManager The {@link ScenarioManager} that manages creation of the simulation scenario.
     * @param logsFolder      The folder to publish logs.
     */
    void init(ScenarioManager scenarioManager, File logsFolder);

    /**
     * Executes the simulation.
     */
    void run();

    /**
     * Waits for the completion of the scenario. This is a blocking call.
     *
     * @return True, if the simulation completed as expected. False, otherwise.
     */
    boolean waitForCompletion();
}
