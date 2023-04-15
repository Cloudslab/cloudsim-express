### GITS Transformation Algorithm Approach

GITS do not provide a released implementation. Therefore, we implement their transformation algorithm.

First, we build a vanilla implementation of the algorithm. For this evaluation, we omit handling user mapping
information through template (such as VM information). Because such information, and any additional information
still follows the same template structure, thus it becomes irrelevant for our comparison.

#### Pre-requisites

###### Generic Cloud UML Model

We define a generic cloud model for a datacenter with Hosts. We use a portion of the UML diagram that was originally
proposed in the paper [1] to represent the cloud model.

- A datacenter object
- A Rack object
- A Shelf object
- A Host object

![GITS-UML-MODEL.svg](GITS-UML-MODEL.svg)

###### JSON template

```json
{
  "name": "GITS template",
  "version": "0.0.1",
  "Objective": {
    "name": "scheduling"
  },
  
  "DC": {
    "name": "RegionalDC",
    "DC_count": 1
  },
  
  "Rack": {
    "name": "rack",
    "HostingDC": "DC",
    "Rack_count": 1
  },
  
  "Shelf": {
    "name": "shelf",
    "HostingRack": "rack",
    "Shelf_count": 1
  },
  
  "Server": {
    "name": "server",
    "Resources": [
      "CPU",
      "RAM",
      "Storage"
    ],
    "Server_count": 1,
    "HostingShelf": "shelf"
  }
}
```

#### Generic input template for cloud simulator transformation algorithm

1. User defines the input to be imported into CloudSim
   - This is the JSON template that we defined [above](#json-template)
2. Generate the XML model from graphical interface of the input model
   - We use textual model, thus omit this step
3. Parse the XML to the defined JSON template
   - We use textual model, thus omit this step
4. Map the JSON template to the generic cloud UML model
5. Populate the UML instance with the JSON input data
6. Simulator supports HA metrics?
    - We skip HA metrics information as it is irrelevant for the comparison. So the answer is No.
7. Discard populating HA metrics

#### Vanilla GITS-for-CloudSim Implementation

In GITS, the JSON template and the GITS library file is supplied to the CloudSim building environment.
The CloudSim building environment is then consume the GITS DCs and Hosts information to populate CloudSim
DCs and Hosts information.

To mimic this exact process, we implement the GITS library file for our comparison. This library performs 
steps 4 to 5 from [above](#generic-input-template-for-cloud-simulator-transformation-algorithm), and 
provide java objects that complies to the UML diagram. The CloudSim building environment then consume these
java objects to populate CloudSim DCs and Hosts information.

#### Implementing the use-case

We implement both custom classes and their integration with CloudSim platform by extending the CloudSim. 
This is because although we can use the GITS-for-CloudSim implementation to avoid implementing the initial modelling,
the mentioned customizations falls withing the scope of extending CloudSim.

[1] M. Jammal, H. Hawilo, A. Kanso, and A. Shami, “Generic input template for cloud simulators: A case study of
CloudSim,” Softw: Pract Exper, vol. 49, no. 5, pp. 720–747, May 2019, doi: 10.1002/spe.2674.
