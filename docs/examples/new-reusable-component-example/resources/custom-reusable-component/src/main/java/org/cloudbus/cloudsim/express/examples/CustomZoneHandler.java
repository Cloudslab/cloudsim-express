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

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultZoneHandler;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.ZoneMonitor;

/**
 * The CustomZoneHandler integrates {@link CustomZoneMonitorHandler} into the simulation
 * building process.
 */
public class CustomZoneHandler extends DefaultZoneHandler {

    @Override
    public void handle() {

        // Preserve default flow.
        super.handle();

        /*
         The zoneDTO is the parsed PoJo of YAML zone object. Since we did add our component as an attribute,
         we can obtain its DTO.

         Q: How did we know that there is a property `zoneDTO`?
         A: Refer to the original code of the `DefaultZoneHandler`
        */
        ZoneMonitor monitorDTO = this.zoneDTO.getMonitor();

        // Let's obtain the handler, initialize, and trigger handling. The following pattern needs to be followed.
        CustomZoneMonitorHandler handler = (CustomZoneMonitorHandler) getHandler(monitorDTO);
        handler.init(monitorDTO, this.resolver);
        handler.handle();
    }
}
