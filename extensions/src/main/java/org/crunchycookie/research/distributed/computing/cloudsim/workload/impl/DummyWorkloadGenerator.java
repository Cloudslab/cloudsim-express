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

package org.crunchycookie.research.distributed.computing.cloudsim.workload.impl;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
import org.crunchycookie.research.distributed.computing.cloudsim.workload.CloudSimWorkloadGenerator;

import java.util.ArrayList;
import java.util.List;

public class DummyWorkloadGenerator implements CloudSimWorkloadGenerator {

    @Override
    public List<Cloudlet> getCloudletsList(int brokerId) {
        return new ArrayList<>();
    }

    @Override
    public List<Vm> getVmList(int vmsNumber, int brokerId) {
        return new ArrayList<>();
    }
}
