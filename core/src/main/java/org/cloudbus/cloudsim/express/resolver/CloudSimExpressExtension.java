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

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * This interface defines an extension for the CloudSim Express with enhanced capabilities, such as consuming custom
 * properties via human-readable simulation scenario script, and loading extensions during runtime.
 */
public interface CloudSimExpressExtension {

    /**
     * This method is called and provided with its properties during initialization.
     *
     * @param properties Properties defined via human-readable simulation scenario script.
     */
    void setProperties(List<Pair<String, String>> properties);

    /**
     * This method is called and provided with the {@link ExtensionsResolver} during initialization.
     *
     * @param resolver The {@link ExtensionsResolver} of the runtime.
     */
    void setExtensionResolver(ExtensionsResolver resolver);
}
