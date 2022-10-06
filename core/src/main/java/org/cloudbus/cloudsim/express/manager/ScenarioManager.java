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

import org.cloudbus.cloudsim.express.handler.ElementHandler;

/**
 * Manages scenario specific logic. T represents the environment object, and U represents the
 * simulation framework's base entities.
 */
public interface ScenarioManager {

    /**
     * Initialize the scenario manager with the corresponding simulation handler.
     *
     * @param scenarioElementHandler Handler that is responsible for the simulation.
     */
    void init(ElementHandler scenarioElementHandler);

    /**
     * Build the scenario using the initialized simulation handler.
     */
    void build();

    /**
     * A blocking call to wait until scenario completion.
     *
     * @return
     */
    boolean waitForScenarioCompletion();
}
