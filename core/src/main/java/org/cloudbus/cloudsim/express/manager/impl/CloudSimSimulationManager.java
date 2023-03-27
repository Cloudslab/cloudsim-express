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

package org.cloudbus.cloudsim.express.manager.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.express.exceptions.CloudSimExpressRuntimeException;
import org.cloudbus.cloudsim.express.exceptions.constants.ErrorConstants.ErrorCode;
import org.cloudbus.cloudsim.express.manager.ScenarioManager;
import org.cloudbus.cloudsim.express.manager.SimulationManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Calendar;

public class CloudSimSimulationManager implements SimulationManager {

    private static Logger logger = LogManager.getLogger(CloudSimSimulationManager.class);

    ScenarioManager scenarioManager;

    @Override
    public void init(ScenarioManager scenarioManager, File simulationLogsFolder) {

        this.scenarioManager = scenarioManager;
        initializeCloudSim(getOutputStreamForSimulationLogs(simulationLogsFolder));
    }

    @Override
    public boolean waitForCompletion() {

        return scenarioManager.waitForScenarioCompletion();
    }

    @Override
    public void run() {

        // Build the scenario with CloudSim.
        scenarioManager.build();

        logger.atInfo().log("Starting CloudSim simulation");
        CloudSim.startSimulation();

        waitForCompletion();

        CloudSim.stopSimulation();
        logger.atInfo().log("CloudSim Simulation is completed");
    }

    private OutputStream getOutputStreamForSimulationLogs(File simulationLogsFolder) {

        OutputStream simulationLogsFileOutputStream;
        try {
            if (simulationLogsFolder != null && simulationLogsFolder.isDirectory()) {
                Files.createDirectories(Path.of(simulationLogsFolder.getAbsolutePath()));
            }
            File simulationLogsFile = new File(simulationLogsFolder,
                    "cloudsim-" + Instant.now().toEpochMilli() + ".log");
            boolean isFileNewlyCreated = simulationLogsFile.createNewFile();
            if (!isFileNewlyCreated) {
                throw new IOException("A simulation log file with the name: " + simulationLogsFile.getName() + " already exists");
            }
            simulationLogsFileOutputStream = new FileOutputStream(simulationLogsFile);
        } catch (IOException e) {
            throw new CloudSimExpressRuntimeException(ErrorCode.UNKNOWN_ERROR,
                    "Unable to create the log file", e);
        }
        return simulationLogsFileOutputStream;
    }

    private void initializeCloudSim(OutputStream simulationLogsFileOutputStream) {

        // First step: Initialize the CloudSim package. It should be called
        // before creating any entities.
        int num_user = 1;   // number of cloud users
        Calendar calendar = Calendar.getInstance();
        boolean trace_flag = false;  // mean trace events

        // Initialize the CloudSim library

        CloudSim.init(num_user, calendar, trace_flag);

        Log.setOutput(simulationLogsFileOutputStream);
    }
}
