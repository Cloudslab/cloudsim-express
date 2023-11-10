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

### Execution Times

Use linux 'time' command with execution.

TIME from tool start, until starting cloudsim platform.
- GITS:
  - start: 1699632186203
  - end:   1699632186234
  - Gap (ms): 31
- CloudSimPlus Automation: --> time until starting cloudsim plus platform.
  - Start: 1699630774129
  - End:   1699630774400
  - Gap (ms): 271
- CloudSim Express
  - Started:  2023-11-11 02:35:53,496
  - End:      2023-11-11 02:35:53,602
  - Gap:                          106