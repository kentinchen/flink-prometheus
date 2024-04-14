local grafana = import '../g.libsonnet';
local dashboard = grafana.dashboard;
local row = grafana.row;
local prometheus = grafana.prometheus;
local template = grafana.template;
local graphPanel = grafana.graphPanel;
local tablePanel = grafana.tablePanel;
local annotation = grafana.annotation;
local singlestat = grafana.singlestat;
local panel = grafana.panel;
local query = grafana.query;

{
  grafanaDashboards+:: {
    'system/gen-dashboard.json':
       dashboard.new('Gen Dashboard')
       + dashboard.withUid('gen-prom')
       + dashboard.withDescription('Dashboard for gen-prom')
       + dashboard.graphTooltip.withSharedCrosshair()
       + dashboard.withPanels([
         panel.timeSeries.new('Random')
         + panel.timeSeries.queryOptions.withTargets([
           query.prometheus.new(
             'default',
             'gen_metric{}',
           ),
         ])
         + panel.timeSeries.standardOptions.withUnit('')
         + panel.timeSeries.gridPos.withW(24)
         + panel.timeSeries.gridPos.withH(8),
       ])
  }
}
