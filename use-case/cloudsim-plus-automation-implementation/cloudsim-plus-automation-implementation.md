### CloudSim Plus Automation Approach

! Using latest CloudSim Plus Automation master branch, as of 14-04-2023 

- Fork the CloudSim Plus Automation project
  - This project only supports VM allocation policies and datacenters available in the CloudSim Plus project
  - Therefore, we need to modify the project itself and recompile
- Add custom classes and fix the code to pick our custom classes
  - Change default datacenter to the custom one
    - Create the custom class under the CloudSim Plus package to extend the simple datacenter
    - Modify the platform to switch to the custom datacenter
  - Change VM allocation policy to the custom one
    - Create the custom class under the CloudSim Plus package to extend the simple VM allocation policy
- Compile the project and create a jar file
- Modify the yaml file to use the custom VM allocation policy
- Execute the simulation