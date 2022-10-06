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

package org.cloudbus.cloudsim.express.resolver;

import org.cloudbus.cloudsim.express.handler.ElementHandler;

import java.io.File;


/**
 * Reads environment definition config files and convert to environment description objects.
 */
public interface EnvironmentResolver {

    /**
     * Read the simulation scenario from a human-readable file, and initialize the corresponding
     * simulation handler.
     *
     * @param simulationScenarioDescription File which defines simulation scenario in a human-readable
     *                                      format.
     * @param scenarioClassName             The class name of the model file which represents the
     *                                      simulation scenario.
     * @param extensionsResolver            Extensions resolver.
     */
    void init(File simulationScenarioDescription, String scenarioClassName,
              ExtensionsResolver extensionsResolver);

    /**
     * Get the initialized simulation handler.
     *
     * @return Simulation handler.
     */
    ElementHandler getSimulationHandler();
}
