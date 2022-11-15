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

package org.crunchycookie.research.distributed.computing.cloudsim.workload;

import org.crunchycookie.research.distributed.computing.cloudsim.workload.impl.helper.Workload;

// TODO: 2022-05-09 Add proper support from the framework for time series.
public interface CloudSimTimeSeriesWorkloadGenerator extends CloudSimWorkloadGenerator {

    Workload getNext(double currentSimulationTime, String zone);

    double getNextSubmissionTime(double currentSimulationTime, String zone);
}
