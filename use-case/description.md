## CloudSim Use Case Scenario : [Writing a Custom Simulation Scenario]

### System Model

A local datacenter region with 1 datacenter having 5 servers. 

- Everytime the datacenter update its cloudlets, a log message should be displayed with the overall count.
- Everytime VM allocation policy allocates a host, a log message should be displayed with the overall count.

### CloudSim Customizations

- Custom VM allocation policy
  - _Extends Simple VM allocation policy to print the count that it allocates hosts_
- Custom Datacenter Class
  - _Extends default datacenter class to print the count that it updated the cloudlets_