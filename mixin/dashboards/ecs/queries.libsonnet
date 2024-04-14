local g = import '../g.libsonnet';
local prometheusQuery = g.query.prometheus;

local variables = import './variables.libsonnet';

{
  cpuUsage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        vm_CPUUtilization
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
  memUsage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        vm_MemoryUtilization
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
  diskUsage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        vm_DiskUtilization
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
  diskInodeUsage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        vm_DiskIusedUtilization
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
  vmProcessCount:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        vm_ProcessCount
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
  vmLoadAverage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        vm_LoadAverage
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
  vmDiskIORead:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        vm_DiskIORead
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
  vmDiskIOWrite:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        vm_DiskIOWrite
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
  vmInternetNetworkRX:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        vm_InternetNetworkRX
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
  vmInternetNetworkTX:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        vm_InternetNetworkTX
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
  vmTcpConn:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        vm_LoadAverage
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
}