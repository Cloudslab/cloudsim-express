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

package org.crunchycookie.research.distributed.computing.cloudsim.workload;

import org.crunchycookie.research.distributed.computing.cloudsim.workload.impl.helper.Workload;

/**
 * This interface defines a {@link CloudSimWorkloadGenerator} that provides time series workloads. The main purpose
 * is to serve dynamic workload across the simulation time. In a multi-region cloud deployment, identifying the region
 * might also benefit in determining location-aware workload submission. This interface provides APIs to support that.
 */
public interface CloudSimTimeSeriesWorkloadGenerator extends CloudSimWorkloadGenerator {

    /**
     * Returns the next available workload.
     *
     * @param time   The current simulation time.
     * @param region The identifier of the region.
     * @return The next workload.
     */
    Workload getNext(double time, String region);

    /**
     * Returns the next workload submission time.
     *
     * @param time The current simulation time.
     * @param region The identifier of the region.
     * @return The time that the next workload will be submitted.
     */
    double getNextSubmissionTime(double time, String region);
}
