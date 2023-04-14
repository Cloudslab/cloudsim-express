### CloudSim Express Approach

- Create a new java project and implement the following classes:
  - Implement the custom VM allocation policy class
  - Implement the custom Datacenter class
- Compile the project and create a jar file
- Add the jar file to the `extensions` folder of CloudSim Express tool
- Open and edit the `system-model.yaml` file to define the regional zone with one datacenter having 5 hosts
  - Change the datacenter class name to our custom class
  - Change the vm allocation policy class name to our custom class
- Executes the tool