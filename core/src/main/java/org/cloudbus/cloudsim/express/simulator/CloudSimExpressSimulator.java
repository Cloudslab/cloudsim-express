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

package org.cloudbus.cloudsim.express.simulator;

import java.io.File;

/**
 * The CloudSimExpressSimulator represents a simulator that reads multiple configuration options, and manages executing
 * a CloudSim simulation. It reads the simulation via a human-readable script, inject externally provided extensions,
 * and publish corresponding logs.
 */
public interface CloudSimExpressSimulator {

    /**
     * Initialize the simulator.
     *
     * @param configs Configuration file. Refer module resources for an example config file.
     */
    void init(File configs);

    /**
     * Executes the simulation.
     */
    void simulate();
}
