local g = import '../g.libsonnet';
local prometheusQuery = g.query.prometheus;

local variables = import './variables.libsonnet';

{
  cpuUsage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        vm_CPUUtilization {instanceId=~"$instances",job=~"pushgateway", product="ecs"}
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
        vm_MemoryUtilization {instanceId=~"$instances",job=~"pushgateway", product="ecs"}
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
        vm_DiskUtilization {instanceId=~"$instances",job=~"pushgateway", product="ecs"}
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
        vm_DiskIusedUtilization {instanceId=~"$instances",job=~"pushgateway", product="ecs"}
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
        vm_ProcessCount {instanceId=~"$instances",job=~"pushgateway", product="ecs"}
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
        vm_LoadAverage {instanceId=~"$instances",job=~"pushgateway", product="ecs"}
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
        vm_DiskIORead {instanceId=~"$instances",job=~"pushgateway", product="ecs"}
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
        vm_DiskIOWrite {instanceId=~"$instances",job=~"pushgateway", product="ecs"}
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
        vm_InternetNetworkRX {instanceId=~"$instances",job=~"pushgateway", product="ecs"}
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
        vm_InternetNetworkTX {instanceId=~"$instances",job=~"pushgateway", product="ecs"}
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
        vm_TcpCount {instanceId=~"$instances",job=~"pushgateway", product="ecs"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}-{{state}}
    |||),
}