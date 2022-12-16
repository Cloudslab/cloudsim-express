# CloudSimExpress: Low-Code Simulations with CloudSim Toolkit

CloudSim toolkit is designed to be used as a Java library. Let's take a look at how a simple CloudSim simulation
scenario take
place, from the perspective of the user.

1. Create a new project, and integrate CloudSim as a dependency.
    - If a Maven is used for the dependency management, CloudSim project needs to be built and the corresponding Jar has
      to be installed in the local Maven repository.
    - If an IDE is used, then the similar CloudSim Jar needs to be configured for the IDE.
2. Import corresponding libraries in the code, initialize the toolkit, and write the simulation scenario in Java.

```java
// Import libraries
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
...

// Initialize the toolkit
int num_user=1;   // number of cloud users
Calendar calendar=Calendar.getInstance();
boolean trace_flag=false;  // mean trace events

CloudSim.init(num_user,calendar,trace_flag);

// Simulation scenario
String arch="x86";      // system architecture
String os="Linux";          // operating system
String vmm="Xen";
double time_zone=10.0;         // time zone this resource located
double cost=3.0;              // the cost of using processing in this resource
double costPerMem=0.05;       // the cost of using memory in this resource
...
```
3. Finally, the project is built, and the simulation is executed.

##  Development Challenges
1. Difficult learning curve
   - Moderate knowledge in Java language, and its dependency management is mandatory
2. Boilerplate coding
   - In most scenarios, the initial system design and the toolkit initiation is common. Researchers spent time on implementing the same code snippets
3. Re-usability of simulation scenarios
   - In most cases, researchers implement their own ways of implementing the simulation scenarios. This makes re-usability of CloudSim scenarios extremely difficult
   - Most simulation scenarios share common components, with different policies. Current way of writing simulation do not provide an easier approach to share the common scenarios

## CloudSimExpress: A Low-Code Approach
Let's take a look at how CloudSimExpress simulation scenario take place, from the perspective of the user.

1. Design the simulation scenario in a human-readable script (YAML file).
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
      extensionProperties:
        - key: "workloadPercentage"
          value: "11"
        - key: "isRandomPercentage"
          value: "true"
        - key: "dataFolder"
          value: "../external-dependencies/data/workload/bitbrain-dataset/fastStorage"
GlobalDatacenterNetwork:
  zoneCount: 1
  interZoneNetworkDescriptionFilePath: "./sample-data/aws-geo-distributed-datacenter-inter-network-data.csv"
  zones:
    - <<: *Zone
      name: "REGIONAL_ZONE_VIRGINIA"
...
```
2. If needed, write Java code for only the extensions that are specific to the researcher's task.
3. Copy aforementioned extensions Jars to CloudSimExpress tool, alongside with the simulation design YAML file.
4. Configure `cloudsim-express-configs.properties` file with the root element of the scenario (root element in the YAML file)
5. Execute the tool (execute the JAR file).

## Benefits
1. Difficult learning curve
   - Zero coding knowledge is required to run simulations with re-usable components
2. Boilerplate coding
   - Simulation description covers common components
   - Researcher only write their specific CloudSim extensions
3. Re-usability of simulation scenarios
   - The simulation design YAML file with corresponding JARs are easily sharable

## Does CloudSimExpress support every possible simulation scenarios via the script?

Simply put, no. But the moment a specific scenario is written in the 'CloudSimExpress manner', that 
scenario can be scripted and re-used.

## Writing a scenario for CloudSimExpress

CloudSimExpress follows a modularized simulation approach. A large simulation scenario consists of 
smaller set of components. Each such component may be decomposed into even smaller components. Let's take a 
look at writing a scenario with CloudSimExpress.

### Break down the simulation into components

Firstly, break down the simulation into smaller components. For example, a global datacenter network is consists of 
multiple zones. Each zone has a broker, and a datacenter. Each datacenter has hosts. 

### Generate YAML objects for the components
In order to use each component in the script, YAML objects needs to be generated. For this, the researcher describe the
components in the `<cloudsimexpress-repository>/core/src/ain/resources/simulation-elements.yaml` file. If the component
is already available, then researcher can re-use it. After that, the CloudSimExpress respository needs to be build using 
`mvn clean install` command. This will generate the Java PoJo files for any new components.

### Write handlers for new components
For any new component, a handler must be included. The `<cloudsimexpress-repository>/core/src/main/java/handler/impl` 
package include all existing handlers. Any new handler needs to be included in this package, and every handler extends 
the base element handler class.

### Formulate the scenario in the simulation description
Then researcher writes the end-to-end simulation scenario in the YAML file using the components. Then during the tool
execution, the CloudSimExpress tool interpret scenarios using the YAML objects and the handlers and executes the 
simulation.

## Isn't this too much work?
The beauty of CloudSimExpress depends on the wide availability of different simulation components (i.e YAML objects 
and their handlers). Once the eco-system is matured, re-usable components are readily available in a human-readable 
script, thus writing a new scenario might just be in matter seconds using the script.

## Inner workings of CloudSimExpress
![CloudSimExpress System Architecture](low-code-simulation-system-architecture.svg)
- **Environment Resolver**: Reads the simulation elements file and create an in-memory object to represent the scenario, and find a suitable handler to handler that element. Handlers are user defined and injected during runtime.
- **Extensions Resolver**: Read and create instances from injected classes during runtime.
- **Element Handler**: Implement the scenario defined in the element with CloudSim.
- **Scenario Manager**: Execute the simulation by calling the root element handler.
- **Simulation manager**: Does the initial cloudsim preparation and run the simulation with the scenario manager.
### How does CloudSimExpress tool work?
- The tool  identifies a root element for each scenario. For example, a global datacenter network scenario is 
identified with the scenario element, 'GlobalDatacenterNetwork'.
- An element can be composed with multiple other elements. The 'GlobalDatacenterNetwork' element is composed with a list of 'Zone' elements. In which, a 'Zone' element is composed with ' Datacenter', 'Broker', and 'WorkloadGenerator' elements. Likewise, the scenarios can be defined in this top-down approach.
- Each of the element is a structured data object(you can think of them as similar to popular JSON objects!). We define them using the human-readable data serialization language, YAML, which is extremely easy to understand.
- You can find the definition of these elements in the simulation-elements.yaml file. This file is written according to the industry accepted standard, the Open API 3.0.
- During compile time, all YAML elements are automatically converted to plain old java objects(also known as POJOs) having the same name. For example, the 'Datacenter' YAML object converts to the class file 'Datacenter.java', and becomes available in the runtime for consumption.
- The tool introduces a new class named as 'ElementHandler' to handler elements. It also packs element handlers for many default elements including 'GlobalDatacenter', 'Zone', etc. These handlers are then registered in the low-code-simulator-configs.properties file as a list, according to their priority, where the earliest handler in the list gets the highest priority.

1. During the execution, the tool reads its own configuration file, low-code-simulator-configs.properties . It locates the simulation definition file, and then parse the root element into an object called the simulation element. As we discussed earlier, this root element can contain many other elements, and even custom extension elements. Therefore, during the parsing, the tool loads extensions by dynamically loading jars in the extension folder that the user had defined in the config file. Extension loading is managed by the ExtensionsResolver module and element parsing is managed by the EnvironmentResolver module.
2. The simulation element is then passed on to the ScenarioManager module. Hereafter, this module will manage the simulation element.
3. Afterwards, the tool asks the SimulationManager module to run the simulation. The default implementation of this module implements CloudSim simulation where it manages initiation and termination of CloudSim simulation. Once CloudSim initiation is done, it then calls the Scenario Manager to build the scenario.
4. Scenario Manager calls the 'handle()' method of the simulation element's handler. The simulation element perform logic to build the scenario with CloudSim in a top-down approach by calling subsequent 'handle' methods of element handlers for each sub-elements it contains. It is worth noting that, the user is allowed to develop their own element handler jars and include them in the extension folder. The user can then override the default element handler by putting the class name of their own customer element handler with high priority in the config file.
5. By the end of the 'handle' method execution of the simulation element, simulation scenario is built with CloudSim.
6. Once returned to the Simulation Manager, it then runs the CloudSim simulation. The CloudSim log messages are forwarded to a desired log file as mentioned in the tool's configs.
7. Upon the completion of the simulation, the tool ends its execution.

## Releasing CloudSimExpress

Any changes in the code (including new additions or modifications of YAML elements, or their handlers) require
building and releasing the CloudSimExpress tool. For this, the `perform-release.sh` shell script needs to be executed.