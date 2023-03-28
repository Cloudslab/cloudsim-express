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

package org.cloudbus.cloudsim.express.resolver;

import org.cloudbus.cloudsim.express.handler.ElementHandler;

import java.io.File;


/**
 * The EnvironmentResolver parses human-readable simulation scenario script. It converts the described system model into
 * DTO objects, including the system model DTO specified by:
 * {@link org.cloudbus.cloudsim.express.constants.ScriptConstants#SIMULATION_SYSTEM_MODEL_COMPONENT}. The resolver then
 * interprets the {@link ElementHandler} corresponds to system model DTO and provide APIs to access that
 * (i.e. {@link #getSystemModelHandler()}).
 *
 * @see org.cloudbus.cloudsim.express.resolver.impl.YAMLEnvironmentResolver
 */
public interface EnvironmentResolver {

    /**
     * Initialize with the resolver.
     *
     * @param simulationScript   Human-readable simulation scenario script file.
     * @param extensionsResolver {@link ExtensionsResolver}.
     */
    void init(File simulationScript, ExtensionsResolver extensionsResolver);

    /**
     * Returns the {@link ElementHandler} corresponds to system model DTO.
     *
     * @return {@link ElementHandler} corresponds to system model DTO.
     */
    ElementHandler getSystemModelHandler();
}
