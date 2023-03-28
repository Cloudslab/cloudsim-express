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

package org.crunchycookie.research.distributed.computing.cloudsim.workload.impl.helper;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;

import java.util.List;

/**
 * Represents a workload submitter in a given time. A Workload consists of the set of
 * Cloudlets (which represents the application/task itself), and the set of VMs that these tasks are associated with.
 */
public class Workload {

    private List<Cloudlet> cloudlets;
    private List<Vm> vms;
    private double time;

    public Workload(List<Cloudlet> cloudlets, List<Vm> vms, double time) {

        this.cloudlets = getListCopy(cloudlets);
        this.vms = getListCopy(vms);
        this.time = time;
    }

    private <T> List<T> getListCopy(List<T> list) {
        return list == null ? null : list.stream().toList();
    }

    public List<Cloudlet> getCloudlets() {

        return getListCopy(cloudlets);
    }

    public List<Vm> getVms() {

        return getListCopy(vms);
    }

    public double getTime() {

        return time;
    }
}
