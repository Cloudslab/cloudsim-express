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

import org.cloudbus.cloudsim.express.handler.impl.BaseElementHandler;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.ZoneMonitor;

/**
 * The CustomZoneMonitorHandler represents the handler of the Zone Monitor.
 */
public class CustomZoneMonitorHandler extends BaseElementHandler {

    private String name;

    @Override
    public void init(Object componentDTO, ExtensionsResolver resolver) {

        super.init(componentDTO, resolver);

        // We know that the component object is the ZoneMonitor PoJo type.
        this.name = ((ZoneMonitor) componentDTO).getName();
    }

    @Override
    public void handle() {
        System.out.println(this.name);
    }

    @Override
    public boolean isSimulated() {
        return false;
    }

    @Override
    public boolean canHandle(Object o) {
        return o instanceof ZoneMonitor;
    }

    @Override
    public Object getProperty(String s) {
        return null;
    }
}
