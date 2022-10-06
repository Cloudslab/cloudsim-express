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
import org.cloudbus.cloudsim.express.handler.ElementHandler;
import org.cloudbus.cloudsim.express.manager.ScenarioManager;

public class DefaultScenarioManager implements ScenarioManager {

    private static Logger logger = LogManager.getLogger(DefaultScenarioManager.class);

    ElementHandler scenarioElementHandler;

    @Override
    public void init(ElementHandler scenarioElementHandler) {

        this.scenarioElementHandler = scenarioElementHandler;
    }

    @Override
    public void build() {

        logger.atInfo().log("Building the scenario using " + scenarioElementHandler.getClass());
        scenarioElementHandler.handle();
    }

    @Override
    public boolean waitForScenarioCompletion() {

        logger.atInfo().log("Waiting for scenario to be completed...");
        return scenarioElementHandler.isSimulated();
    }
}
