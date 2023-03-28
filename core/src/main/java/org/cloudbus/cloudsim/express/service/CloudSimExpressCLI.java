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

package org.cloudbus.cloudsim.express.service;

import org.cloudbus.cloudsim.express.simulator.CloudSimExpressSimulator;
import org.cloudbus.cloudsim.express.simulator.impl.DefaultCloudSimExpressSimulator;

import java.io.File;

/**
 * The CloudSimExpressCLI represents a command line tool to execute the CloudSim Express.
 */
public class CloudSimExpressCLI {

    private static final CloudSimExpressCLI cloudSimExpressCLI = new CloudSimExpressCLI();

    /**
     * CloudSim Express CLI entry point.
     *
     * @param args External parameter list. First argument is the CloudSim Express configuration file.
     */
    public static void main(String[] args) {

        cloudSimExpressCLI.run(args[0]);
    }

    /**
     * Executes CloudSim Express simulator.
     *
     * @param CloudSimExpressConfigFile CloudSim Express configuration file.
     */
    public void run(String CloudSimExpressConfigFile) {

        CloudSimExpressSimulator simulator = new DefaultCloudSimExpressSimulator();
        simulator.init(new File(CloudSimExpressConfigFile));
        simulator.simulate();
    }
}
