local g = import '../g.libsonnet';
local prometheusQuery = g.query.prometheus;

local variables = import './variables.libsonnet';

{
  active_session:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        active_session {instanceId=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  TPS:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        TPS {instanceId=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  QPS:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        QPS {instanceId=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  CpuUsage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        CpuUsage {instanceId=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  DiskUsage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        DiskUsage {instanceId=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  IOPSUsage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        IOPSUsage {instanceId=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  MemoryUsage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        MemoryUsage {instanceId=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),

  Queries_ps:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        Queries_ps {instanceId=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  Slow_queries_ps:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        Slow_queries_ps {instanceId=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  Key_usage_ratio:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        Key_usage_ratio {instanceId=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  Key_read_hit_ratio:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        Key_read_hit_ratio {instanceId=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
   Key_write_hit_ratio:
     prometheusQuery.new(
       '$' + variables.datasource.name,
       |||
         Key_write_hit_ratio {instanceId=~"$instances",job=~"pushgateway", product="rds"}
       |||
     )
     + prometheusQuery.withIntervalFactor(2)
     + prometheusQuery.withLegendFormat(|||
       {{instanceId}}
     |||),
}