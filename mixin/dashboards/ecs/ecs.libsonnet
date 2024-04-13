local g = import '../g.libsonnet';
local row = g.panel.row;
local panels = import './panels.libsonnet';
local variables = import './variables.libsonnet';
local queries = import './queries.libsonnet';

{
    # 增加dashboard文件
    grafanaDashboards+:: {
        'ecs-summary.json':
           g.dashboard.new('ecs总览')
           + g.dashboard.withDescription(|||
              ECS 指标监控
           |||)
           + g.dashboard.graphTooltip.withSharedCrosshair()
           + g.dashboard.withVariables([
             variables.datasource,
             variables.instanceid,
           ])
           + g.dashboard.withPanels(
             g.util.grid.makeGrid([
               row.new('CPU')
               + row.withPanels([
                 panels.timeSeries.cpuUsage('Load', queries.vmLoadAverage),
               ]),
               ], panelWidth=8)
             )

    }
}