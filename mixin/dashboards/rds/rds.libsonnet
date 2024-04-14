local g = import '../g.libsonnet';
local row = g.panel.row;
local panels = import './panels.libsonnet';
local variables = import './variables.libsonnet';
local queries = import './queries.libsonnet';

{
    grafanaDashboards+:: {
        'cms/rds-summary.json':
           g.dashboard.new('rds主机监控')
           + g.dashboard.withDescription(|||
              RDS主机监控
           |||)
           + g.dashboard.graphTooltip.withSharedCrosshair()
           + g.dashboard.withVariables([
             variables.datasource,
             variables.instances,
           ])
           + g.dashboard.withPanels(
             g.util.grid.makeGrid([
               row.new('延迟')
               + row.withPanels([
                   panels.timeSeries.active_session('活动连接数', queries.active_session),
                   panels.timeSeries.Queries_ps('查询数', queries.Queries_ps),
                   panels.timeSeries.Slow_queries_ps('慢sql数', queries.Slow_queries_ps),
                 ]),
               row.new('流量')
               + row.withPanels([
                    panels.timeSeries.TPS('事务数', queries.TPS),
                    panels.timeSeries.QPS('事务数', queries.QPS),
                 ]),
               row.new('使用率')
               + row.withPanels([
                   panels.timeSeries.CpuUsage('CPU使用率', queries.CpuUsage),
                   panels.timeSeries.MemoryUsage('内存使用率', queries.MemoryUsage),
                   panels.timeSeries.DiskUsage('磁盘使用率', queries.DiskUsage),
                   panels.timeSeries.IOPSUsage('IO使用率', queries.IOPSUsage),
                 ]),
               row.new('饱和度')
               + row.withPanels([
                   panels.timeSeries.Key_usage_ratio('索引使用率', queries.Key_usage_ratio),
                   panels.timeSeries.Key_read_hit_ratio('索引查询使用率', queries.Key_read_hit_ratio),
                   panels.timeSeries.Key_write_hit_ratio('索引写入使用率', queries.Key_write_hit_ratio),
                 ]),
               ], panelWidth=8)
             )
    }
}