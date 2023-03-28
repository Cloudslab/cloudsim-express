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

package org.cloudbus.cloudsim.express.manager.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cloudbus.cloudsim.express.handler.ElementHandler;
import org.cloudbus.cloudsim.express.manager.ScenarioManager;

/**
 * This class represents a {@link ScenarioManager} implementation.
 */
public class DefaultScenarioManager implements ScenarioManager {

    private static final Logger logger = LogManager.getLogger(DefaultScenarioManager.class);

    ElementHandler systemModelHandler;

    @Override
    public void init(ElementHandler systemModelHandler) {

        this.systemModelHandler = systemModelHandler;
    }

    @Override
    public void build() {

        logger.atInfo().log("Building the scenario using " + systemModelHandler.getClass());
        systemModelHandler.handle();
    }

    @Override
    public boolean waitForScenarioCompletion() {

        logger.atInfo().log("Waiting for scenario to be completed...");
        return systemModelHandler.isSimulated();
    }
}
