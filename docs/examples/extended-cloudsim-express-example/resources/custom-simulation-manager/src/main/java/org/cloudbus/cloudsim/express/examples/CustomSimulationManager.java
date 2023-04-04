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

package org.cloudbus.cloudsim.express.examples;

import org.cloudbus.cloudsim.express.manager.impl.CloudSimSimulationManager;

/**
 * The CustomSimulationManager represents a manager that allows publishing CloudSim logs
 * to a custom location.
 */
public class CustomSimulationManager extends CloudSimSimulationManager {

    @Override
    public void run() {
        super.run();
        System.out.println("Simulation Management is Completed!");
    }
}
