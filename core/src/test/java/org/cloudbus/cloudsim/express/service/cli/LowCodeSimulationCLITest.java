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

package org.cloudbus.cloudsim.express.service.cli;

import org.cloudbus.cloudsim.express.exceptions.LowCodeSimulationRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.cloudbus.cloudsim.express.exceptions.constants.ErrorConstants.ErrorCode.ELEMENT_NOT_AWARE_OF_SIMULATION;

public class LowCodeSimulationCLITest {

    /**
     * Execute low-code cli for a simple simulation scenario and expect zero errors during a
     * successful test.
     */
    @Test
    void PrepareProcessingElementsListTest() {

        String LOW_CODE_SIMULATOR_CONFIGS_PROPERTIES_FILE_PATH = "./src/test/resources/processing-element-test-low-code-simulator-configs.properties";
        try {
            String[] args = {LOW_CODE_SIMULATOR_CONFIGS_PROPERTIES_FILE_PATH};
            CloudSimExpressCLI.main(args);
        } catch (LowCodeSimulationRuntimeException e) {
            Assertions.assertEquals(ELEMENT_NOT_AWARE_OF_SIMULATION.getErrorCode(),
                    e.getCode().getErrorCode(), "Incorrect error");
            return;
        }
        Assertions.fail("Running a simulation with just preparing a processing element should fail, "
                + "because processing elements are unaware about simulation thus no actual simulation would be run");
    }

    /**
     * Execute low-code cli for a global datacenter network scenario.
     */
    @Test
    void GlobalDatacenterNetworkTest() {

        try {
            String LOW_CODE_SIMULATOR_CONFIGS_PROPERTIES_FILE_PATH = "./src/test/resources/"
                    + "global-datacenter-network-test-test-low-code-simulator-configs.properties";
            CloudSimExpressCLI.main(new String[]{LOW_CODE_SIMULATOR_CONFIGS_PROPERTIES_FILE_PATH});
        } catch (Exception e) {
            Assertions.fail("Something went wrong. Simulation did not end well", e);
        }
    }
}
