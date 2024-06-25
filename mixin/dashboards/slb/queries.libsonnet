local g = import '../g.libsonnet';
local prometheusQuery = g.query.prometheus;

local variables = import './variables.libsonnet';

{
  connsPS:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        connsPS {lbId=~"$instances", product="slb"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{lbId}}-{{vport}}
    |||),
  count:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        count {lbId=~"$instances", product="slb"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{lbId}}-{{vport}}
    |||),
  dropConnPS:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        dropConnPS {lbId=~"$instances", product="slb"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{lbId}}-{{vport}}
    |||),
  failConnPS:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        failConnPS {lbId=~"$instances", product="slb"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{lbId}}-{{vport}}
    |||),
  inActConnPS:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        inActConnPS {lbId=~"$instances", product="slb"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{lbId}}-{{vport}}
    |||),
  inBitsPS:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        inBitsPS {lbId=~"$instances", product="slb"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{lbId}}-{{vport}}
    |||),
  inDBitesPS:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        inDBitesPS {lbId=~"$instances", product="slb"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{lbId}}-{{vport}}
    |||),
  inDPktsPS:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        inDPktsPS {lbId=~"$instances", product="slb"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{lbId}}-{{vport}}
    |||),
  inPktsPS:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        inPktsPS {lbId=~"$instances", product="slb"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{lbId}}-{{vport}}
    |||),
  maxConnsPs:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        maxConnsPs {lbId=~"$instances", product="slb"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{lbId}}-{{vport}}
    |||),
  outBitsPS:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        outBitsPS {lbId=~"$instances", product="slb"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{lbId}}-{{vport}}
    |||),
  outDBitesPS:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        outDBitesPS {lbId=~"$instances", product="slb"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{lbId}}-{{vport}}
    |||),
  outDPktsPS:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        outDPktsPS {lbId=~"$instances", product="slb"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{lbId}}-{{vport}}
    |||),
  outPktsPS:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        outPktsPS {lbId=~"$instances", product="slb"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{lbId}}-{{vport}}
    |||),
  sumInBytes:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        sumInBytes {lbId=~"$instances", product="slb"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{lbId}}-{{vport}}
    |||),
  sumOutBytes:
    prometheusQuery.new(
      '$' + variables.datasource.name,
      |||
        sumOutBytes {lbId=~"$instances", product="slb"}
      |||
    )
    + prometheusQuery.withIntervalFactor(2)
    + prometheusQuery.withLegendFormat(|||
      {{lbId}}-{{vport}}
    |||),
}