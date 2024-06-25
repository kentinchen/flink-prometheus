local g = import '../g.libsonnet';
local row = g.panel.row;
local panels = import './panels.libsonnet';
local variables = import './variables.libsonnet';
local queries = import './queries.libsonnet';

{
    # 增加dashboard文件
    grafanaDashboards+:: {
        'cq/ecs-summary.json':
           g.dashboard.new('ecs主机监控')
           + g.dashboard.withDescription(|||
              ECS主机监控
           |||)
           + g.dashboard.graphTooltip.withSharedCrosshair()
           + g.dashboard.withVariables([
             variables.datasource,
             variables.instances,
           ])
           + g.dashboard.withPanels(
             g.util.grid.makeGrid([
               row.new('使用率')
               + row.withPanels([
                   panels.timeSeries.cpuUsage('CPU', queries.cpuUsage),
                 ]),
               ], panelWidth=8)
             )
    }
}