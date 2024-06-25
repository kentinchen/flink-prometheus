local g = import '../g.libsonnet';
local prometheusQuery = g.query.prometheus;

local variables = import './variables.libsonnet';

{
  storage:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        storage {instanceId=~"$buckets", product="oss"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{instanceId}}
    |||),
  recv_size:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        recv_size {bucket_name=~"$buckets", product="oss"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{bucket_name}}
    |||),
  send_size:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        send_size {bucket_name=~"$buckets", product="oss"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{bucket_name}}
    |||),
  error_403:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        error_403 {bucket_name=~"$buckets", product="oss"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{bucket_name}}
    |||),
  error_404:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        error_404 {bucket_name=~"$buckets", product="oss"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{bucket_name}}
    |||),
  error_408:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        error_408 {bucket_name=~"$buckets", product="oss"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{bucket_name}}
    |||),
  error_499:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        error_499 {bucket_name=~"$buckets", product="oss"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{bucket_name}}
    |||),
  error_4xx:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        error_4xx {bucket_name=~"$buckets", product="oss"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{bucket_name}}
    |||),
  error_503:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        error_503 {bucket_name=~"$buckets", product="oss"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{bucket_name}}
    |||),
  error_5xx:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        error_5xx {bucket_name=~"$buckets", product="oss"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{bucket_name}}
    |||),
  success_203:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        success_203 {bucket_name=~"$buckets", product="oss"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{bucket_name}}
    |||),
  success_2xx:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        success_2xx {bucket_name=~"$buckets", product="oss"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{bucket_name}}
    |||),
  success_3xx:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        success_3xx {bucket_name=~"$buckets", product="oss"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{bucket_name}}
    |||),
  intran_recv_size:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        intran_recv_size {bucket_name=~"$buckets", product="oss"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{bucket_name}}
    |||),
  intran_send_size:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        intran_send_size {bucket_name=~"$buckets", product="oss"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{bucket_name}}
    |||),
}