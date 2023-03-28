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

package org.crunchycookie.research.distributed.computing.cloudsim.workload.impl;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.crunchycookie.research.distributed.computing.cloudsim.workload.CloudSimWorkloadGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Dummy workload generator demonstrates submitting workloads with Cloudsim express, by submitting hardcoded set of
 * {@link Cloudlet}s and assigning each of the {@link Cloudlet} to a hardcoded {@link Vm}.
 * <p>
 * Approach: First, create the {@link Cloudlet}s during the generator initialization based on the requirements, or
 * the data trace. This list is returned in {@link #getCloudletsList()}. Afterwards, in the {@link #getVmList(int)}
 * method, the VM list corresponds to the list of {@link Cloudlet}s must be created and returned.
 * <p>
 * // TODO: 27/3/23 Due to a limitation in Vm class, setting broker id in the Vm is handled in the generator. This needs to be avoided.
 */
public class DummyWorkloadGenerator implements CloudSimWorkloadGenerator {

    public static final int TASK_COUNT = 100;
    private final List<Cloudlet> cloudletList;

    public DummyWorkloadGenerator() {

        cloudletList = new ArrayList<>();
        for (int id = 0; id < TASK_COUNT; id++) {
            cloudletList.add(getDummyCloudlet(id));
        }
    }

    @Override
    public List<Cloudlet> getCloudletsList() {
        return this.cloudletList;
    }

    @Override
    public List<Vm> getVmList(int brokerId) {

        // We assign each task to its own VM.
        var vmList = new ArrayList<Vm>();
        for (int vmId = 0; vmId < cloudletList.size(); vmId++) {
            // Create VM.
            vmList.add(getDummyVM(vmId, brokerId));
            // Assign a cloudlet to the VM.
            cloudletList.get(vmId).setVmId(vmId);
        }
        return vmList;
    }

    private static Cloudlet getDummyCloudlet(int id) {

        var cloudlet = new Cloudlet(id, 100000, 2, 20,
                20, new UtilizationModelFull(), new UtilizationModelFull(),
                new UtilizationModelFull());

        // Set unknown values to -1.
        cloudlet.setUserId(-1);
        cloudlet.setVmId(-1);

        return cloudlet;
    }

    private static Vm getDummyVM(int vmId, int brokerId) {
        return new Vm(vmId, brokerId, 10, 2, 100, 100, 100, "", new CloudletSchedulerTimeShared());
    }
}
