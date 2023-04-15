package org.cloudsimplus.datacenters;

import org.cloudsimplus.allocationpolicies.VmAllocationPolicy;
import org.cloudsimplus.core.Simulation;
import org.cloudsimplus.hosts.Host;

import java.util.List;

public class CustomDatacenterSimple extends DatacenterSimple {
    private int numberOfTimesCloudletsUpdated = 0;

    public CustomDatacenterSimple(Simulation simulation, List<? extends Host> hostList, VmAllocationPolicy vmAllocationPolicy) {
        super(simulation, hostList, vmAllocationPolicy);
    }

    @Override
    protected double updateCloudletProcessing() {

        System.out.println("Cloudlets updated count: " + (++numberOfTimesCloudletsUpdated));
        return super.updateCloudletProcessing();
    }
}
