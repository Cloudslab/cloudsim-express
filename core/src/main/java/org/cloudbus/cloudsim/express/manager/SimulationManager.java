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

package org.cloudbus.cloudsim.express.manager;

import java.io.File;

/**
 * Manages simulation execution.
 */
public interface SimulationManager {

    /**
     * Initialize the component.
     *
     * @param scenarioManager      The {@link SimulationManager}.
     * @param simulationLogsFolder Specific folder to publish logs.
     */
    void init(ScenarioManager scenarioManager, File simulationLogsFolder);

    /**
     * Executes the simulation.
     */
    void run();

    /**
     * Blocks current thread until simulation completes.
     *
     * @return True, if the simulation completed as expected.
     */
    boolean waitForCompletion();
}
