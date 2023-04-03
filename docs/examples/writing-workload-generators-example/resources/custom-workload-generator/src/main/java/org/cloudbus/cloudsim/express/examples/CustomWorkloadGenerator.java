package org.cloudbus.cloudsim.express.examples;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.crunchycookie.research.distributed.computing.cloudsim.workload.CloudSimWorkloadGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * The CustomWorkloadGenerator represents a workload generator that submits a single Cloudlet, and an associated
 * Vm.
 */
public class CustomWorkloadGenerator implements CloudSimWorkloadGenerator {

    private final Cloudlet cloudlet;

    public CustomWorkloadGenerator() {
        this.cloudlet = new Cloudlet(
                1, 100000, 2, 20,
                20,
                new UtilizationModelFull(),
                new UtilizationModelFull(),
                new UtilizationModelFull()
        );
    }

    @Override
    public List<Cloudlet> getCloudletsList() {

        List<Cloudlet> list = new ArrayList<>();
        list.add(this.cloudlet);
        return list;
    }

    @Override
    public List<Vm> getVmList(int i) {

        List<Vm> list = new ArrayList<Vm>();
        list.add(new Vm(1, i, 10, 2, 100, 100, 100, "",
                new CloudletSchedulerTimeShared()));
        return list;
    }
}