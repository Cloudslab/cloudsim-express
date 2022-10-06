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
 * This class represents the low code simulation layer, which includes its various modules and
 * interconnections between them.
 */
public interface LowCodeSimulator {

    /**
     * Initialize the various modules and interconnections between them.
     *
     * @param configsFile Path to the configuration file.
     */
    void init(File configsFile);

    /**
     * Starts the simulation.
     */
    void simulate();
}
