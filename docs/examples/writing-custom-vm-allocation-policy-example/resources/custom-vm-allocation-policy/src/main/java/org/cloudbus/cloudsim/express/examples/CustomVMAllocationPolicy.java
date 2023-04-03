package org.cloudbus.cloudsim.express.examples;

import org.apache.commons.lang3.tuple.Pair;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.express.resolver.CloudSimExpressExtension;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;

import java.util.List;
import java.util.Map;

public class CustomVMAllocationPolicy extends VmAllocationPolicy implements CloudSimExpressExtension  {

    public CustomVMAllocationPolicy(List<? extends Host> list) {
        super(list);
    }

    @Override
    public void setProperties(List<Pair<String, String>> list) {
        System.out.println(list.toString());
    }

    @Override
    public void setExtensionResolver(ExtensionsResolver extensionsResolver) {
        System.out.println(extensionsResolver.getClass());
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {
        return false;
    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {
        return false;
    }

    @Override
    public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> list) {
        return null;
    }

    @Override
    public void deallocateHostForVm(Vm vm) {

    }

    @Override
    public Host getHost(Vm vm) {
        return null;
    }

    @Override
    public Host getHost(int i, int i1) {
        return null;
    }
}