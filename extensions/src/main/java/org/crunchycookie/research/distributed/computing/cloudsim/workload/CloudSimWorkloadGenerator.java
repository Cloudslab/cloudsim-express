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

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;

import java.util.List;

/**
 * The CloudSimWorkloadGenerator defines a generator to provide workload for CloudSim. The cloudlets (i.e.,
 * representation of tasks in CloudSim), and the associated VMs that executes the providing tasks can be obtained via
 * this generator.
 */
public interface CloudSimWorkloadGenerator {

    /**
     * Returns the {@link Cloudlet} list.
     *
     * @return The list of Cloudlets.
     */
    List<Cloudlet> getCloudletsList();

    /**
     * Returns the VM list corresponds to the providing Cloudlets.
     *
     * @param brokerId Id of the broker that handles providing workload. // TODO: 29/3/23 This needs to be moved to CloudSim itself.
     * @return The list of VMs.
     */
    List<Vm> getVmList(int brokerId);
}
