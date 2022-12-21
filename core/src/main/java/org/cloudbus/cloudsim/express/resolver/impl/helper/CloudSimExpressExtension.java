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

package org.cloudbus.cloudsim.express.resolver.impl.helper;

import org.apache.commons.lang3.tuple.Pair;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;

import java.util.List;

/**
 * An extension of CloudSimExpress.
 * <p>
 * An extension of this type is able to consume properties via human-readable simulation scenario description, and
 * also to create dependant objects from classes available in CloudSimExpress extensions.
 */
public interface CloudSimExpressExtension {

    /**
     * Prior to the simulation, the CloudSimExpress tool invokes this method and provide any available properties. For
     * an example, if a custom {@link org.cloudbus.cloudsim.Datacenter} implements {@link CloudSimExpressExtension},
     * this method is invoked and any properties that are defined for this specific custom class is provided.
     *
     * @param properties Properties described in the simulation description for this specific class.
     */
    void setProperties(List<Pair<String, String>> properties);

    /**
     * The CloudSimExpress tool invokes this method and provide the {@link ExtensionsResolver} prior simulation begins.
     *
     * @param resolver The {@link ExtensionsResolver}.
     */
    void setExtensionResolver(ExtensionsResolver resolver);
}
