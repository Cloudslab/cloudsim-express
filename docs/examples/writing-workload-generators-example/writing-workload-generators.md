layout: page
title: "Writing Workload Generators with CloudSim Express"
permalink: /examples/writing-workload-generators

### Writing Workload Generators with CloudSim Express

CloudSim Express introduces a new approach to connect workloads. In research, the workloads are available as traces, and
they can be submitted all-at-once, or dynamically. To cater these requirements, CloudSim Express introduces two new 
interfaces,
- [CloudSimWorkloadGenerator](..%2F..%2F..%2Fextensions%2Fsrc%2Fmain%2Fjava%2Forg%2Fcrunchycookie%2Fresearch%2Fdistributed%2Fcomputing%2Fcloudsim%2Fworkload%2FCloudSimWorkloadGenerator.java)
- [CloudSimTimeSeriesWorkloadGenerator](..%2F..%2F..%2Fextensions%2Fsrc%2Fmain%2Fjava%2Forg%2Fcrunchycookie%2Fresearch%2Fdistributed%2Fcomputing%2Fcloudsim%2Fworkload%2FCloudSimTimeSeriesWorkloadGenerator.java)

By default, an implementation of `CloudSimWorkloadGenerator` is available with the class, 
[DummyWorkloadGenerator](..%2F..%2F..%2Fextensions%2Fsrc%2Fmain%2Fjava%2Forg%2Fcrunchycookie%2Fresearch%2Fdistributed%2Fcomputing%2Fcloudsim%2Fworkload%2Fimpl%2FDummyWorkloadGenerator.java).

In this example, we are extending this `DummyWorkloadGenerator` to write a custom workload generator
and deploy it with the system model that we built with [Say "Hello World!" with CloudSim Express](..%2Fhello-world-example%2Fhello-world-simulation.md) example.

#### Creating a java project for the workload generator

We implement the workload generator in a separate java project first. We use Maven-based 
java project.

1. Create a simple Maven project.
   1. Go to a new folder.
   2. Execute `mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-simple -DarchetypeVersion=1.4`
      - Pre-requisite: Maven
   3. Enter followings when prompted.
      1. groupId = `org.cloudbus.cloudsim.express.examples`
      2. artifactId = `custom-workload-generator`
      3. version = `1.0-SNAPSHOT`
      4. package = `org.cloudbus.cloudsim.express.examples`
      5. Press Enter to confirm.
2. Configure dependencies.
   1. Go to the `custom-workload-generator` project folder.
   2. Open `pom.xml`
   3. Within the `<dependencies>` tag, copy the following to declare CloudSim Express as a dependency
      - Pre-requisite: You must build `cloudsim-express` project at least once.
      - Copy and paste the following.
        ```xml
        <dependency>
            <artifactId>cloudsim-express-extensions</artifactId>
            <groupId>org.cloudbus.cloudsim.cloudsim-express</groupId>
            <version>0.1-SNAPSHOT</version>
        </dependency> 
        ```
   4. Create the custom workload generator class in the `/src/main/java/org/cloudbus/cloudsim/express/examples/` folder
   by creating a new file `CustomWorkloadGenerator.java`.
      - Delete the generated `App.java` file.
   5. Implement the workload generator to return a single cloudlet. 
      - The sample code is available in the [custom-workload-generator](resources%2Fcustom-workload-generator) project.
   6. Build the project by executing `mvn clean install` from the project root directory (i.e., where the `pom.xml` 
   file resides).

#### Deploying the custom workload generator

1. Copy the workload generator jar file from `<custom-workload-generator>/target/custom-workload-generator-1.0-SNAPSHOT.jar`, to `cloudsim-express-tool-location/extensions`.
2. Modify the `system-model.yaml` to use the custom workload generator.
   - From: 
     ```yaml
     ...
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
     ...
     ```
   - To:
     ```yaml
     ...
     Zone: &Zone
     name: "default zone"
     datacenter: *Datacenter
     broker:
      variant:
         className: "org.cloudbus.cloudsim.DatacenterBroker"
      name: "RegionalBroker"
     workloadGenerator:
      variant:
        className: "org.cloudbus.cloudsim.express.examples.CustomWorkloadGenerator"   
     ...
     ```
3. Executes the simulation via `sh ./cloudsim-express.sh`
4. Check the latest CloudSim log file from `logs`. You can observe that only one Cloudlet has been created
during the simulation.
   ```text
   ...
   0.01: RegionalBroker: VM #1 has been created in Datacenter #3, Host #1
   0.01: RegionalBroker: Sending cloudlet 1 to VM #1
   10000.01: RegionalBroker: Cloudlet 1 received
   10000.01: RegionalBroker: All Cloudlets executed. Finishing...
   10000.01: RegionalBroker: Destroying VM #1
   RegionalBroker is shutting down...
   ...
   ```

#### Materials

---

The related materials are included in the [resources](resources) folder.