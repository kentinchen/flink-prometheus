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
      {{instanceId}}
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
      {{instanceId}}
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
      {{instanceId}}
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
      {{instanceId}}
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
      {{instanceId}}
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
      {{instanceId}}-{{period}}
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
      {{instanceId}}
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
      {{instanceId}}
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
      {{instanceId}}
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
      {{instanceId}}-{{netname}}
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
      {{instanceId}}-{{state}}
    |||),
}