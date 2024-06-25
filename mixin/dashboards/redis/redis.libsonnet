local g = import '../g.libsonnet';
local row = g.panel.row;
local panels = import './panels.libsonnet';
local variables = import './variables.libsonnet';
local queries = import './queries.libsonnet';

{
    grafanaDashboards+:: {
        'cms/redis-summary.json':
           g.dashboard.new('redis监控')
           + g.dashboard.withDescription(|||
              Redis监控
           |||)
           + g.dashboard.graphTooltip.withSharedCrosshair()
           + g.dashboard.withVariables([
             variables.datasource,
             variables.instances,
           ])
           + g.dashboard.withPanels(
             g.util.grid.makeGrid([
               row.new('监控项')
               + row.withPanels([
                   panels.timeSeries.connectionUsage('连接数使用率', queries.connectionUsage),
                   panels.timeSeries.cpuUsage('CPU使用率', queries.cpuUsage),
                   panels.timeSeries.failedCount('操作失败数', queries.failedCount),
                   panels.timeSeries.intranetIn('内网网络入流量(kb)', queries.intranetIn),
                   panels.timeSeries.intranetInRatio('写入带宽使用率', queries.intranetInRatio),
                   panels.timeSeries.intranetOut('内网网络出流量(kb)', queries.intranetOut),
                   panels.timeSeries.intranetOutRatio('读取带宽使用率', queries.intranetOutRatio),
                   panels.timeSeries.memoryUsage('内存使用率', queries.memoryUsage),
                   panels.timeSeries.qpsUsage('QPS使用率', queries.qpsUsage),
                   panels.timeSeries.quotaBandWidth('带宽配额', queries.quotaBandWidth),
                   panels.timeSeries.quotaConnection('连接配额', queries.quotaConnection),
                   panels.timeSeries.quotaMemory('内存配额(byte)', queries.quotaMemory),
                   panels.timeSeries.quotaQPS('QPS配额', queries.quotaQPS),
                   panels.timeSeries.usedConnection('已用连接数', queries.usedConnection),
                   panels.timeSeries.usedMemory('已用内存(byte)', queries.usedMemory),
                   panels.timeSeries.usedQPS('已用QPS数量', queries.usedQPS),
                 ]),
               ], panelWidth=8)
             )
    }
}