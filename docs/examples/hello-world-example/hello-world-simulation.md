layout: page
title: "Say "Hello World!" with CloudSim Express"
permalink: /examples/hello-world-simulation

### Say "Hello World!" with CloudSim Express

In this example, we are writing a basic simulation scenario to demonstrate simulations with
CloudSim Express.

#### Pre-requisites

---

1. Install Java.
    - CloudSim Express has been tested with JDK 17 on Ubuntu 22.10.
2. Install Maven.
3. Build the `cloudsim-express` repository by using the following command,
    - `mvn clean install`
4. Copy the CloudSim Express tool from `<project-home>/release-artifacts/cloudsim-express.zip`
5. Extract the zip file in a preferred location. This location is thereafter known as `cloudsim-express-tool`.

#### The simulation system model

---

For this example, we are simulating a cloud region with a datacenter and a broker.
The broker is offered with a fixed workload, and that workload is executed.

#### Scripting the simulation system model

---

1. Backup the default system model script, and create a new one.
    1. Go to the `cloudsim-express-tool` location.
    2. Go inside the `scenarios` folder.
        - It has the default system model shipped with the tool.
          Rename the existing `system-model.yaml` file into `system-model-backup.yaml`.
    3. Create an empty `system-model.yaml`. We are going to script our system model in this.
        - The `system-model.yaml` is a reserved file name. CloudSim Express executes the system model defined in it.
2. Let's build the system model from ground-up.
   1. Create a `processing element` component of a `Host`.
      - ```yaml
         # The component type, and a reference (We will reuse this reference later)
         ProcessingElement: &ProcessingElement
         variant:
           className: "org.cloudbus.cloudsim.Pe" # A processing element is an extension. We use default class. 
         id: -1 # -1 is a reserved value, means auto increment id.
         processingElementProvisioner:
           variant:
             className: "org.cloudbus.cloudsim.provisioners.PeProvisionerSimple" # Again, similar extension. Default class is used.
           mips: 2660 # mips value of the processing element.
         ```
   2. A `Host` have a list of `processing elements`.
      - ```yaml
        # The component type, and a reference (We will reuse this reference later)
        ProcessingElementList: &ProcessingElementList
        - <<: *ProcessingElement # Notice how we use the previously declared ProcessingElement reference.
          variant: # In YAML, attributes can be overridden. We use that to indicate that we need 5 copies of the same processing element.
            className: "org.cloudbus.cloudsim.Pe"
            copies: 5
        ```
   3. Let's create the `Host`.
      - ```yaml
        # The component type, and a reference (We will reuse this reference later)
        Host: &Host
        variant:
          className: "org.cloudbus.cloudsim.Host"
          copies: 1
        id: -1 # -1 is a reserved value, means auto increment id.
        ramProvisioner:
          className: "org.cloudbus.cloudsim.provisioners.RamProvisionerSimple"
          extensionProperties:
            - key: "ram"
              value: "515639"
        bwProvisioner:
          className: "org.cloudbus.cloudsim.provisioners.BwProvisionerSimple"
          extensionProperties:
            - key: "bw"
              value: "100000000"
        vmScheduler:
          className: "org.cloudbus.cloudsim.VmSchedulerTimeShared"
        storage: 1000000
        processingElementList: *ProcessingElementList  # Notice how we use the previously declared ProcessingElementList reference.
        ```
   4. A `Datacenter` have a list of `Hosts`.
      - ```yaml
        HostList: &HostList
        - <<: *Host
          variant:
            className: "org.cloudbus.cloudsim.Host"
            copies: 10
        ```
   5. Let's create `Datacenter Characterictics` of the datacenter. 
      - ```yaml
        Characteristics: &Characteristics
        arch: "x86"
        os: "Linux"
        vmm: "Xen"
        timeZone: 10.0
        cost: 3.0
        costPerMemory: 0.05
        costPerStorage: 0.01
        costPerBandwidth: 0.0
        hostList: *HostList
        ```
   6. Let's create the `Datacenter`.
      - ```yaml
        Datacenter: &Datacenter
         variant:
           className: "org.cloudbus.cloudsim.Datacenter"
         characteristics: *Characteristics
         vmAllocationPolicy:
           className: "org.cloudbus.cloudsim.VmAllocationPolicySimple"
         storage: ""
         schedulingInterval: 0
         name: "regional-datacenter"
        ```
   7. We are now ready to create the regional zone. We are using the in-built `DummyWorkloadGenerator` extension of CloudSim Express for the workload.
      - ```yaml
        Zone: &Zone
         name: "default zone"
         datacenter: *Datacenter
         broker:
           variant:
             className: "org.cloudbus.cloudsim.DatacenterBroker"
           name: "RegionalBroker"
         workloadGenerator:
           variant:
             className: "org.crunchycookie.research.distributed.computing.cloudsim.workload.impl.DummyWorkloadGenerator"
        ```
   8. Finally, we configure our system model using the reserved `SimulationSystemModel` component.
      - ```yaml
        SimulationSystemModel:
         name: 'Zone'
        ```

#### Executing the simulation

---

Since we only used the standard components without any extensions, we can execute the simulation straight away.
Notice that we did not even touch Java yet.
1. Go to `cloudsim-express-tool`.
2. Execute the simulation.
   - ```shell
     sh cloudsim-express.sh
     ``` 
3. Observe the CloudSim Express logs in the terminal. 
   - ```shell
     sh cloudsim-express.sh 
     [main] INFO  org.cloudbus.cloudsim.express.simulator.impl.DefaultCloudSimExpressSimulator - Initializing the Low-code simulator...
     [main] INFO  org.cloudbus.cloudsim.express.simulator.impl.DefaultCloudSimExpressSimulator - Using Extension Resolver: class org.cloudbus.cloudsim.express.resolver.impl.JARExtensionsResolver
     [main] INFO  org.cloudbus.cloudsim.express.simulator.impl.DefaultCloudSimExpressSimulator - Using Environment Resolver: class org.cloudbus.cloudsim.express.resolver.impl.YAMLEnvironmentResolver
     [main] INFO  org.cloudbus.cloudsim.express.simulator.impl.DefaultCloudSimExpressSimulator - Using Simulation Handler: class org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultZoneHandler
     [main] INFO  org.cloudbus.cloudsim.express.simulator.impl.DefaultCloudSimExpressSimulator - Using Scenario Manager: class org.cloudbus.cloudsim.express.manager.impl.DefaultScenarioManager
     Initialising...
     [main] INFO  org.cloudbus.cloudsim.express.simulator.impl.DefaultCloudSimExpressSimulator - Using Simulation Manager: class org.cloudbus.cloudsim.express.manager.impl.CloudSimSimulationManager
     [main] INFO  org.cloudbus.cloudsim.express.manager.impl.CloudSimSimulationManager - Building the scenario
     [main] INFO  org.cloudbus.cloudsim.express.manager.impl.DefaultScenarioManager - Building the scenario using class org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultZoneHandler
     [main] INFO  org.cloudbus.cloudsim.express.manager.impl.CloudSimSimulationManager - Starting CloudSim simulation
     [main] INFO  org.cloudbus.cloudsim.express.manager.impl.DefaultScenarioManager - Waiting for scenario to be completed...
     [main] INFO  org.cloudbus.cloudsim.express.manager.impl.CloudSimSimulationManager - CloudSim Simulation is completed
     [main] INFO  org.cloudbus.cloudsim.express.simulator.impl.DefaultCloudSimExpressSimulator - Low-code simulation is completed in 0 seconds
     ```
4. The corresponding CloudSim logs are available in the `logs` folder.
   - ```shell
     ...
     RegionalBroker is starting...
     Starting CloudSim version 3.0
     regional-datacenter is starting...
     Entities started.
     0.0: RegionalBroker: Cloud Resource List received with 1 resource(s)
     0.0: RegionalBroker: Trying to Create VM #0 in regional-datacenter
     0.0: RegionalBroker: Trying to Create VM #1 in regional-datacenter
     ...
     ```
     
   
#### Materials

---

The related materials are included in the [resources](resources) folder.