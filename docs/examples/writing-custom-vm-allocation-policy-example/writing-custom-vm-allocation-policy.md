layout: page
title: "Writing Custom VM allocation Policy"
permalink: /examples/writing-custom-vm-allocation-policy

### Writing a custom VM allocation policy as an Extension of CloudSim Express

CloudSim Express introduces enhanced extensions, in which researchers can provide custom properties to the extension via
the system model script.

In this example, we are writing a classic CloudSim extension, a vm allocation policy, as a CloudSim Express extension. 
We explore configuring the vm allocation policy by feeding custom properties via the script.

A CloudSim Express extension needs to implement the [CloudSimExpressExtension](..%2F..%2F..%2Fcore%2Fsrc%2Fmain%2Fjava%2Forg%2Fcloudbus%2Fcloudsim%2Fexpress%2Fresolver%2FCloudSimExpressExtension.java).

#### Creating a java project for the vm allocation policy

We implement the vm allocation policy in a separate java project. We use Maven-based java project.

1. Create a simple Maven project.
   1. Go to a new folder.
   2. Execute `mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-simple -DarchetypeVersion=1.4`
      - Pre-requisite: Maven
   3. Enter followings when prompted.
      1. groupId = `org.cloudbus.cloudsim.express.examples`
      2. artifactId = `custom-vm-allocation-policy`
      3. version = `1.0-SNAPSHOT`
      4. package = `org.cloudbus.cloudsim.express.examples`
      5. Press Enter to confirm.
2. Configure dependencies.
   1. Go to the `custom-vm-allocation-policy` project folder.
   2. Open `pom.xml`
   3. Within the `<dependencies>` tag, copy the following to declare CloudSim Express as a dependency
      - Pre-requisite: You must build `cloudsim-express` project at least once.
      - Copy and paste the following.
        ```xml
        <dependency>
            <artifactId>cloudsim-express-core</artifactId>
            <groupId>org.cloudbus.cloudsim.cloudsim-express</groupId>
            <version>0.1-SNAPSHOT</version>
        </dependency> 
        ```
   4. Create the custom vm allocation policy class in the `/src/main/java/org/cloudbus/cloudsim/express/examples/` folder
   by creating a new file `CustomVMAllocationPolicy.java`.
      - Delete the generated `App.java` file.
   5. Implement the vm allocation policy, which implements the `CloudSimExpressExtension` interface. 
      - The policy,
        - Prints out the extension resolver class name.
        - Prints out the property as it reads.
      - The sample code is available in the [custom-vm-allocation-policy](resources%2Fcustom-vm-allocation-policy) project.
   6. Build the project by executing `mvn clean install` from the project root directory (i.e., where the `pom.xml` 
   file resides).

#### Deploying the custom vm allocation policy

1. Copy the vm allocation policy jar file from `<custom-vm-allocation-policy>/target/custom-vm-allocation-policy-1.0-SNAPSHOT.jar`, to `cloudsim-express-tool-location/extensions`.
2. Modify the `system-model.yaml` to use the vm allocation policy.
   - From: 
     ```yaml
     ...
     Datacenter: &Datacenter
     variant:
      className: "org.cloudbus.cloudsim.Datacenter"
     characteristics: *Characteristics
     vmAllocationPolicy:
      className: "org.cloudbus.cloudsim.VmAllocationPolicySimple"
     storage: ""
     schedulingInterval: 0
     name: "regional-datacenter" 
     ...
     ```
   - To:
     ```yaml
     ...
     Datacenter: &Datacenter
     variant:
      className: "org.cloudbus.cloudsim.Datacenter"
     characteristics: *Characteristics
     vmAllocationPolicy:
      className: "org.cloudbus.cloudsim.express.examples.CustomVMAllocationPolicy"
      extensionProperties:
        - key: "PROPERTY_KEY"
          value: "PROPERTY_VALUE"
     storage: ""
     schedulingInterval: 0
     name: "regional-datacenter" 
     ...
     ```
3. Executes the simulation via `sh ./cloudsim-express.sh`
4. Check logs. You can observe that the custom vm allocation policy component printing
out the property.
   ```shell
   ...
   [main] INFO  org.cloudbus.cloudsim.express.manager.impl.DefaultScenarioManager - Building the scenario using class org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultZoneHandler
   class org.cloudbus.cloudsim.express.resolver.impl.JARExtensionsResolver
   [(PROPERTY_KEY,PROPERTY_VALUE)]
   [main] INFO  org.cloudbus.cloudsim.express.manager.impl.CloudSimSimulationManager - Starting CloudSim simulation
   ...
   ```

#### Materials

---

The related materials are included in the [resources](resources) folder.