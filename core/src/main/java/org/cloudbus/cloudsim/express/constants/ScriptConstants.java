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

package org.cloudbus.cloudsim.express.constants;

/**
 * Provide various constants support for managing the script parsing.
 */
public class ScriptConstants {

    // If the symbol is present, then the corresponding 'id' field is identified as auto-incrementing. In which, the
    // CloudSim Express will handle incrementing IDs. This is useful in cases such as, when declaring multiple hosts
    // with different IDs.
    public static final String AUTOGENERATE_ID_SYMBOL = "-1";
    public static final String SIMULATION_SYSTEM_MODEL_COMPONENT = "SimulationSystemModel";
}
