layout: page
title: "Introducing a new re-usable component with CloudSim Express"
permalink: /examples/introducing-new-reusable-component

### Introducing a new re-usable component with CloudSim Express

Please refer to [README.md](..%2F..%2F..%2FREADME.md) for a detailed description about re-usable components.

In this example, we are going to introduce a new re-usable component and use it in the system model script.

We will create a new component to represent a zone monitor. It is a part of the zone, alongside wih the datacenter, 
broker, and workload generator.

Unlike in other examples that we worked with extensions, this task involves extending CloudSim Express. 
Therefore, it is recommended to avoid this approach as much as possible to avoid excess development overhead, if there 
is an alternative approach to define the scenario using existing components. 

#### Defining schematics and writing handlers

1. CloudSim Express needs to know the schematics of the new component. To add that information, open [simulation-elements.yaml](..%2F..%2F..%2Fcore%2Fsrc%2Fmain%2Fresources%2Fsimulation-elements.yaml)
file.
2. Add the following object.
   ```yaml
       ZoneMonitor:
          type: object
          properties:
             name:
                type: string
   ```
3. Add our `ZoneMonitor` as an attribute of the `Zone`.
   ```yaml
       Zone:
           ~ Everything else
           monitor:
             $ref: '#/components/schemas/ZoneMonitor'
   ```
4. Build the [core](..%2F..%2F..%2Fcore) component by executing `mvn clean install` inside the module folder. This will 
generate PoJo for the new component.
5. In our design, the `ZoneMonitor` will do nothing but print its name, for the simplicity. Even to do that, we need to 
integrate it into the system model building process. Here are the steps of system model building with default component 
and their handlers.
   - When a `Zone` component is set as the simulation system model, then at first, its handler's `handle` method is 
   called. With vanilla CloudSim Express, the `handle` method of [DefaultZoneHandler.java](..%2F..%2F..%2Fcore%2Fsrc%2Fmain%2Fjava%2Forg%2Fcloudbus%2Fcloudsim%2Fexpress%2Fhandler%2Fimpl%2Fcloudsim%2FDefaultZoneHandler.java)
   is called. Within that, the subsequent handlers are called to build the CloudSim system model in a top-down approach. 
   Feel free to traverse through the code of the [DefaultZoneHandler.java](..%2F..%2F..%2Fcore%2Fsrc%2Fmain%2Fjava%2Forg%2Fcloudbus%2Fcloudsim%2Fexpress%2Fhandler%2Fimpl%2Fcloudsim%2FDefaultZoneHandler.java).
6. Therefore, firstly we need to create a custom handler for our new component to convert YAML object into Java logic. 
Afterward, to integrate the custom handler, we need to override the default zone handler and call our custom handler 
in it's `handle` method. For both, we create a custom handler and an extended zone handler as extensions, in a new java project.
   1. Create a simple Maven project.
      1. Go to a new folder.
      2. Execute `mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-simple -DarchetypeVersion=1.4`
         - Pre-requisite: Maven
      3. Enter followings when prompted.
         1. groupId = `org.cloudbus.cloudsim.express.examples`
         2. artifactId = `custom-reusable-component`
         3. version = `1.0-SNAPSHOT`
         4. package = `org.cloudbus.cloudsim.express.examples`
         5. Press Enter to confirm.
      2. Configure dependencies.
         1. Go to the `custom-reusable-component` project folder.
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
         4. Create the custom component handler class in the `/src/main/java/org/cloudbus/cloudsim/express/examples/` folder
         by creating a new file `CustomZoneMonitorHandler.java`. Also create the extended zone handler by creating a new
         file `CustomZoneHandler.java`
            - Delete the generated `App.java` file.
         5. Implement the logic accordingly. 
            - The sample code is available in the [custom-reusable-component](resources%2Fcustom-reusable-component) project.
         6. Build the project by executing `mvn clean install` from the project root directory (i.e., where the `pom.xml` 
         file resides).

#### Deploying the new re-usable component

1. Build the CloudSim Express project by executing `mvn clean install` at its root.
2. Extract the `<cloudsim-express-root>/release-artifacts/cloudsim-express.zip`, and copy the updated `simulator.jar`
file to the CloudSim Express tool that we used for previous examples, and replace the existing `simulator.jar` with that.
   - Alternatively, you can get the whole new release, extract, and use. But then again we need to update the system model 
   script in that case. Therefore, we only update the simulator component to make it much easier.
2. Copy the custom-reusable-component jar file from `<custom-workload-generator>/target/custom-reusable-component-1.0-SNAPSHOT.jar`, to `cloudsim-express-tool-location/extensions`.
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
        className: "org.crunchycookie.research.distributed.computing.cloudsim.workload.impl.DummyWorkloadGenerator"   
     monitor:
      name: "This is the newly introduced re-usable component"
     ...
     ```
3. Register new element handlers with high priority. Open `configs.properties` file, and add both custom handlers before
the default zone handler.
From:
```properties
# priority list is used to determine which handler to select.
element.handler.priority.1=org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultDatacenterCharacteristicsHandler
element.handler.priority.2=org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultDatacenterHandler
element.handler.priority.3=org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultGlobalDatacenterNetworkHandler
element.handler.priority.4=org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultHostHandler
element.handler.priority.5=org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultProcessingElementHandler
element.handler.priority.6=org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultZoneHandler
```
To:
```properties
# priority list is used to determine which handler to select.
element.handler.priority.1=org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultDatacenterCharacteristicsHandler
element.handler.priority.2=org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultDatacenterHandler
element.handler.priority.3=org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultGlobalDatacenterNetworkHandler
element.handler.priority.4=org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultHostHandler
element.handler.priority.5=org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultProcessingElementHandler
element.handler.priority.6=org.cloudbus.cloudsim.express.examples.CustomZoneHandler
element.handler.priority.7=org.cloudbus.cloudsim.express.examples.CustomZoneMonitorHandler
element.handler.priority.8=org.cloudbus.cloudsim.express.handler.impl.cloudsim.DefaultZoneHandler
```
3. Executes the simulation via `sh ./cloudsim-express.sh`
4. Check the CloudSim Express logs and observe the printed message by the custom component handler.
   ```text
   ...
   This is the newly introduced re-usable component
   [main] INFO  org.cloudbus.cloudsim.express.manager.impl.CloudSimSimulationManager - Starting CloudSim simulation
   ...
   ```
   
The newly introduced component `ZoneMonitor`, can now be used to defined different system models.

#### Materials

---

The related materials are included in the [resources](resources) folder.