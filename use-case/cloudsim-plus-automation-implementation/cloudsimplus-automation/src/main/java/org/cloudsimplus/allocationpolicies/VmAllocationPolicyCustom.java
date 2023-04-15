package org.cloudsimplus.allocationpolicies;

import org.cloudsimplus.hosts.HostSuitability;
import org.cloudsimplus.vms.Vm;

public class VmAllocationPolicyCustom extends VmAllocationPolicySimple {
    private int numberOfTimesHostIsAllocated = 0;

    @Override
    public HostSuitability allocateHostForVm(Vm vm) {

        System.out.println("Host allocation count: " + (++numberOfTimesHostIsAllocated));
        return super.allocateHostForVm(vm);
    }
}
