local g = import '../g.libsonnet';
local prometheusQuery = g.query.prometheus;

local variables = import './variables.libsonnet';

{
  active_session:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        active_session {instanceid=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
  TPS:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        TPS {instanceid=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
  QPS:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        QPS {instanceid=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
  CpuUsage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        CpuUsage {instanceid=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
  DiskUsage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        DiskUsage {instanceid=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
  IOPSUsage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        IOPSUsage {instanceid=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
  MemoryUsage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        MemoryUsage {instanceid=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),

  Queries_ps:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        Queries_ps {instanceid=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
  Slow_queries_ps:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        Slow_queries_ps {instanceid=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
  Key_usage_ratio:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        Key_usage_ratio {instanceid=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
  Key_read_hit_ratio:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        Key_read_hit_ratio {instanceid=~"$instances",job=~"pushgateway", product="rds"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceid}}
    |||),
   Key_write_hit_ratio:
     prometheusQuery.new(
       '$' + variables.datasource.name,
       |||
         Key_write_hit_ratio {instanceid=~"$instances",job=~"pushgateway", product="rds"}
       |||
     )
     + prometheusQuery.withIntervalFactor(2)
     + prometheusQuery.withLegendFormat(|||
       {{instanceid}}
     |||),
}