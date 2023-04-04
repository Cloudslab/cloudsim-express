layout: page
title: "Extending CloudSim express platform"
permalink: /examples/extending-cloudsim-express-platform

### Extending CloudSim express platform

The CloudSim Express is a modularized platform, as described in its architecture (refer to the [README.md](..%2F..%2F..%2FREADME.md)).

In this example, we extend its simulation manager to print a message once the simulation is completed.
#### Implementing custom simulation manager

1. Create a simple Maven project.
      1. Go to a new folder.
      2. Execute `mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-simple -DarchetypeVersion=1.4`
         - Pre-requisite: Maven
      3. Enter followings when prompted.
         1. groupId = `org.cloudbus.cloudsim.express.examples`
         2. artifactId = `custom-simulation-manager`
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
                  <artifactId>cloudsim-express-core</artifactId>
                  <groupId>org.cloudbus.cloudsim.cloudsim-express</groupId>
                  <version>0.1-SNAPSHOT</version>
              </dependency> 
              ```
         4. Create the custom simulation manager class in the `/src/main/java/org/cloudbus/cloudsim/express/examples/` folder
         by creating a new file `CustomSimulationManager.java`.
            - Delete the generated `App.java` file.
         5. Implement the logic accordingly. 
            - The sample code is available in the [resources](resources) project.
         6. Build the project by executing `mvn clean install` from the project root directory (i.e., where the `pom.xml` 
         file resides).

#### Deploying the custom simulation manager

2. Copy the custom-simualtion-manager jar file from `<custom-simulation-manager>/target/custom-simulation-manager-1.0-SNAPSHOT.jar`, to `cloudsim-express-tool-location/extensions`.
3. Switch the simulation manager to the custom one. Open `configs.properties` file, and change the corresponding value.
From:
```properties
module.simulation.manager=org.cloudbus.cloudsim.express.manager.impl.CloudSimSimulationManager
```
To:
```properties
module.simulation.manager=org.cloudbus.cloudsim.express.examples.CustomSimulationManager
```
3. Executes the simulation via `sh ./cloudsim-express.sh`
4. Check the CloudSim Express logs and observe the printed message by the custom simulation manager.
   ```text
   ...
   [main] INFO  org.cloudbus.cloudsim.express.manager.impl.CloudSimSimulationManager - CloudSim Simulation is completed
   Simulation Management is Completed!
   ...
   ```
   
#### Materials

---

The related materials are included in the [resources](resources) folder.