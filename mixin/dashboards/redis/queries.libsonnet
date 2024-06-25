local g = import '../g.libsonnet';
local prometheusQuery = g.query.prometheus;

local variables = import './variables.libsonnet';

{
  connectionUsage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        ConnectionUsage {instanceId=~"$instances", product="kv"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  cpuUsage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        CpuUsage {instanceId=~"$instances", product="kv"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  failedCount:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        FailedCount {instanceId=~"$instances", product="kv"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  intranetIn:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        IntranetIn {instanceId=~"$instances", product="kv"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  intranetInRatio:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        IntranetInRatio {instanceId=~"$instances", product="kv"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  intranetOut:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        IntranetOut {instanceId=~"$instances", product="kv"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  intranetOutRatio:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        IntranetOutRatio {instanceId=~"$instances", product="kv"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  memoryUsage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        MemoryUsage {instanceId=~"$instances", product="kv"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  qpsUsage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        QPSUsage {instanceId=~"$instances", product="kv"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  quotaBandWidth:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        QuotaBandWidth {instanceId=~"$instances", product="kv"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  quotaConnection:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        QuotaConnection {instanceId=~"$instances", product="kv"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  quotaMemory:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        QuotaMemory {instanceId=~"$instances", product="kv"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  quotaQPS:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        QuotaQPS {instanceId=~"$instances", product="kv"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  usedConnection:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        UsedConnection {instanceId=~"$instances", product="kv"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  usedMemory:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        UsedMemory {instanceId=~"$instances", product="kv"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  usedQPS:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        UsedQPS {instanceId=~"$instances", product="kv"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
}