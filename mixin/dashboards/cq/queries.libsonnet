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
}