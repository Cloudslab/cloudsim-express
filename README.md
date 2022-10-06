# CloudSim Express: Low-code Distributed Computing Simulations With CloudSim

![Maven Build](https://github.com/crunchycookie/research-distributed-computing-low-code-simulation/actions/workflows/maven.yml/badge.svg)

### Why?

Distributed computing simulation framework, CloudSim, has rather a steep learning curve and also
tends to be more developer-friendly.

But the framework is mostly used by the researchers.

We all might agree, that the intersection between researchers and developers would not be that
much...

### How?

This project aims to reduce the effort on framework-specific coding when simulating with CloudSim.
Traditionally, a CloudSim simulation involves followings.

- Use it as a library
- Implementing the simulation environment involves a lot of boilerplate code, however, as for many
  simulations, the scenario stays the same. For example, a single datacenter and a broker can
  cover a lots of simulation scenarios. But each time, researchers have to implement the same
  design again, and again.
- Visualizing the simulation as a whole is not possible. Therefore, it is complex to understand the
  simulation environment without having a good coding knowledge with Java.

With this project, we are introducing a layer that runs on top of the CloudSim. It,

- Wraps CloudSim as a tool. Thus, no code is involved.
- Researchers can define simulation scenarios by combining several standard components packed
  with the tool.
- Define the simulation scenario in a human-readable, structured data format (YAML).
- Visualize the simulation as a whole in the human-readable YAML file.
- Ability to extend this layer and create custom scenarios.
- Ability to inject CloudSim extensions as jars during runtime, for various algorithms that needs to
  be simulated.

### System Architecture

![system design](./low-code-simulation-system-architecture.svg)

1. Environment Resolver: Reads the human-readable file and create an in-memory object to represent
   the scenario, and find a suitable handler to handler that element. Handlers are user defined and
   injected during runtime.
2. Extensions Resolver: Read and create instances from injected classes during runtime.
3. Element Handler: Implement the scenario defined in the element with CloudSim.
4. Scenario Manager: Execute the simulation by calling the root element handler.
5. Simulation manager: Does the initial cloudsim preparation and run the simulation with the
   scenario manager.

### User journey

1. Define the simulation scenario in a YAML file. Mention any custom algorithmic extension classes
   in this file.
2. Develop CloudSim algorithmic extensions separately and build them into jar files.
    1. If the user wants to define any additional properties in the scenario definition file for
       extensions and dynamically consume, then they need to
       implement `LowCodeSimulationExtension`
       interface (it's in this repository).
3. Configure Low-code simulation tool using its config file to point simulation scenario file path
   and the folder path which contains extension jar files.
4. Run the tool using a simple one-liner command.

### Significant improvements over the traditional user journey

- No need to implement CloudSim library initiation, or scenario definitions
- Cleaner simulation projects and improved maintainability
- Focus development on CloudSim extensions, thus rendering faster theory to simulation times
- Significantly less boilerplate code

### Usage

A comprehensive usage examples will be added later. Until then, please refer to the tests in the
project.

### How does it work?

Low-code simulation tool can cover most of the simulation scenarios.

At the same time, the system architecture of the tool is designed in such a way that it can be
easily extended with custom components.

- This tool executes a scenario in a top-down approach, and identifies a root element for each
  scenario. For example, a global datacenter network scenario is identified with the scenario
  element, 'GlobalDatacenterNetwork'.
- An element can be composed with multiple other elements. The 'GlobalDatacenterNetwork' element is
  composed with a list of 'Zone' elements. In which, a 'Zone' element is composed with '
  Datacenter', 'Broker', and 'WorkloadGenerator' elements. Likewise, the scenarios can be defined in
  this top-down approach.
- Each of the element is a structured data object(you can think of them as similar to popular JSON
  objects!). We define them using the human-readable data serialization language, YAML, which is
  extremely easy to understand.
- You can find the definition of these elements in
  the [simulation-elements.yaml](core/src/main/resources/simulation-elements.yaml)
  file. This file is written according to the industry accepted standard, the Open API 3.0.
- During compile time, all YAML elements are automatically converted to plain old java objects(also
  known as POJOs) having the same name. For example, the 'Datacenter' YAML object converts to the
  class file 'Datacenter.java', and becomes available in the runtime for consumption.
- The tool introduces a new class named as 'ElementHandler' to handler elements. It also packs
  element handlers for many default elements including 'GlobalDatacenter', 'Zone', etc. These
  handlers are then registered in
  the [low-code-simulator-configs.properties](core/src/main/resources/low-code-simulator-configs.properties)
  file as a list, according to their priority, where the earliest handler in the list gets the
  highest priority.

With that knowledge, lets walk through the low-code interpretation process.

- The user needs to define the simulation scenario in their own YAML file. It's important to
  understand that this is not the same file that was mentioned earlier. Previously we talked about
  such file written according to the Open API 3.0 standard, but it was to define the object schema(
  which defines information like data type of the parameters, etc). The simulation scenario file
  includes the values for element instances such as the amount of ram available in a host, and it is
  written in pure YAML. Also, this file should be an instance of a root element which wraps our
  scenario, that is already defined in that schema file. However, if you feel like you are lost at
  this moment, feel free to examine this
  nice [global datacenter network](core/src/main/resources/scenarios/geo-distributed-datacenter-network-full.yaml)
  example!
- During the execution, the tool reads its own configuration
  file, [low-code-simulator-configs.properties](core/src/main/resources/low-code-simulator-configs.properties)
  . It locates the simulation definition file, and then parse the root element into an object called
  the simulation element. As we discussed earlier, this root element can contain many other
  elements, and even custom extension elements. Therefore, during the parsing, the tool loads
  extensions by dynamically loading jars in the extension folder that the user had defined in the
  config file. Extension loading is managed by
  the [ExtensionsResolver](core/src/main/java/org/crunchycookie/research/distributed/computing/low/code/simulation/resolver/ExtensionsResolver.java)
  module and element parsing is managed by
  the [EnvironmentResolver](core/src/main/java/org/crunchycookie/research/distributed/computing/low/code/simulation/resolver/EnvironmentResolver.java)
  module.
- The simulation element is then passed on to
  the [ScenarioManager](core/src/main/java/org/crunchycookie/research/distributed/computing/low/code/simulation/manager/ScenarioManager.java)
  module. Hereafter, this module will manage the simulation element.
- Afterwards, the tool asks
  the [SimulationManager](core/src/main/java/org/crunchycookie/research/distributed/computing/low/code/simulation/manager/SimulationManager.java)
  module to run the simulation. The default implementation of this module implements CloudSim
  simulation where it manages initiation and termination of CloudSim simulation. Once CloudSim
  initiation is done, it then calls the Scenario Manager to build the scenario.
- Scenario Manager calls the 'handle()' method of the simulation element's handler. The simulation
  element perform logic to build the scenario with CloudSim in a top-down approach by calling
  subsequent 'handle' methods of element handlers for each sub-elements it contains. It is worth
  noting that, the user is allowed to develop their own element handler jars and include them in the
  extension folder. The user can then override the default element handler by putting the class name
  of their own customer element handler with high priority in the config file.
- By the end of the 'handle' method execution of the simulation element, simulation scenario is
  built with CloudSim.
- Once returned to the Simulation Manager, it then runs the CloudSim simulation. The CloudSim log
  messages are forwarded to a desired log file as mentioned in the low-code tool's configs.
- Upon the completion of the simulation, low-code tool ends its execution.

### Extending the tool

- For new variants of the above-mentioned modules, and for the new simulation element schemas, the
  tool needs to be re-compiled, and re-built. During the runtime, the user can set the variant of
  the module to be used, using the config file.
- New variants of the element handlers, and CloudSim extensions can be injected at the runtime
  without re-compiling the tool. Just put the jar files in the extension folder. For new element
  handlers, it is additionally required to register them in the config file with a preferred
  priority order. Highest priority variant will handle a certain element(Tip: There is a method in
  all element handlers known as 'canHandle()'. Whenever a new element handler is required, POJO
  object of the element is passed to this method, and the first handler returns true is used).

### Issues and improvements

In many religions, and cultures around the world, it is believed that most things in life will not
be permanent. Therefore, why should this project be the perfect version where we would never need
any fixes?

Feel free to report bugs, and improvements by creating issues.

As any open-source, community-driven project would do, we all can then discuss and fix such issues.

### Project structure

- ./external-dependencies
    - Contains dependencies required to compile, and also the data to run the tests.
- ./external-extensions
    - Contains a CloudSim extension to introduce a new concept called workload generator. This
      introduces workload generation as a separate module and should ideally come from the CloudSim
      library as it is not related to the simulation tool.

### Project release process

1. Create a release folder named, 'low-code-simulation-tool-${release-version}'.
2. Build 'low-code-simulation-management' module and copy '
   low-code-simulation-management-${release-version}-jar-with-dependencies.jar' from the build
   outputs to the release folder.
    1. Rename it as 'simulator.jar'.
3. Copy '
   low-code-simulation-management/src/main/resources/scenarios/geo-distributed-datacenter-network.yaml'
   file to the release folder.
    1. Create a new folder named 'scenarios', and move the file to there.
4. Copy 'external-extensions/target/external-extensions-0.3-SNAPSHOT.jar' file to the release
   folder.
    1. Create a new folder named 'extensions', and move the file to there.
    2. Rename the file as 'workload-generators-${release-version}.jar'
5. Create a new folder named "logs" in the release folder.
6. Copy 'low-code-simulation-management/src/main/resources/low-code-simulator-configs.properties'
   file to the release folder.
    1. Rename the file as 'configs.properties'
7. Copy log4j2.properties file.
8. Compress the release folder to 'low-code-simulation-tool-${release-version}.zip'.
9. Proceed to github release and upload the zip file as the artifact.
10. Update the snapshot version to the next development version in the project and commit to
    upstream.

Happy simulating!

