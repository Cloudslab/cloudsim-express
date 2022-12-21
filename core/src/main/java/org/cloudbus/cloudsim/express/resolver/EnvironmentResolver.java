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
 * The resolver which builds the simulation handler.
 * <p>
 * This resolver reads human-readable simulation scenario description, and the name of the model class that is supposed
 * to match it. It then works with the {@link ExtensionsResolver} to build a handler {@link ElementHandler}
 * component that is capable of handling the simulation scenario.
 *
 * @see ElementHandler
 */
public interface EnvironmentResolver {

    /**
     * Interpret the simulation scenario and builds the simulation handler.
     *
     * @param simulationScenarioDescription    File which defines simulation scenario in a human-readable
     *                                         format.
     * @param simulationScenarioModelClassName The class name of the model file which represents the
     *                                         simulation scenario.
     * @param extensionsResolver               Extensions resolver.
     */
    void init(File simulationScenarioDescription, String simulationScenarioModelClassName,
              ExtensionsResolver extensionsResolver);

    /**
     * Returns the simulation handler.
     *
     * @return Corresponding simulation handler configured for the specific simulation scenario.
     */
    ElementHandler getSimulationHandler();
}
