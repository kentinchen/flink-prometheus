local g = import '../g.libsonnet';
local prometheusQuery = g.query.prometheus;

local variables = import './variables.libsonnet';

{
  cpuUsage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        vm_CPUUtilization {instanceid=~"$instances",job=~"pushgateway", product="ecs"}
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
        vm_MemoryUtilization {instanceid=~"$instances",job=~"pushgateway", product="ecs"}
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
        vm_DiskUtilization {instanceid=~"$instances",job=~"pushgateway", product="ecs"}
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
        vm_DiskIusedUtilization {instanceid=~"$instances",job=~"pushgateway", product="ecs"}
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
        vm_ProcessCount {instanceid=~"$instances",job=~"pushgateway", product="ecs"}
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
        vm_LoadAverage {instanceid=~"$instances",job=~"pushgateway", product="ecs"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}-{{period}}
    |||),
  vmDiskIORead:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        vm_DiskIORead {instanceid=~"$instances",job=~"pushgateway", product="ecs"}
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
        vm_DiskIOWrite {instanceid=~"$instances",job=~"pushgateway", product="ecs"}
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
        vm_InternetNetworkRX {instanceid=~"$instances",job=~"pushgateway", product="ecs"}
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
        vm_InternetNetworkTX {instanceid=~"$instances",job=~"pushgateway", product="ecs"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}-{{netname}}
    |||),
  vmTcpConn:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        vm_TcpCount {instanceid=~"$instances",job=~"pushgateway", product="ecs"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}-{{state}}
    |||),
}