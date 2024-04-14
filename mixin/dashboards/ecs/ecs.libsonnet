local g = import '../g.libsonnet';
local row = g.panel.row;
local panels = import './panels.libsonnet';
local variables = import './variables.libsonnet';
local queries = import './queries.libsonnet';

{
    # 增加dashboard文件
    grafanaDashboards+:: {
        'ecs-summary.json':
           g.dashboard.new('ecs主机监控')
           + g.dashboard.withDescription(|||
              ECS主机监控
           |||)
           + g.dashboard.graphTooltip.withSharedCrosshair()
           + g.dashboard.withVariables([
             variables.datasource,
             variables.instanceid,
           ])
           + g.dashboard.withPanels(
             g.util.grid.makeGrid([
               row.new('使用率')
               + row.withPanels([
                   panels.timeSeries.cpuUsage('CPU', queries.cpuUsage),
                   panels.timeSeries.memUsage('MEM', queries.memUsage),
                   panels.timeSeries.diskUsage('Disk',   queries.diskUsage),
                   panels.timeSeries.diskInodeUsage('Inode',   queries.diskInodeUsage),
                 ]),
               row.new('饱和度')
               + row.withPanels([
                   panels.timeSeries.vmLoadAverage('Load', queries.vmLoadAverage),
                   panels.timeSeries.vmDiskIORead('磁盘读', queries.vmDiskIORead),
                   panels.timeSeries.vmDiskIOWrite('磁盘写', queries.vmDiskIOWrite),
                   panels.timeSeries.vmInternetNetworkRX('网络接收', queries.vmInternetNetworkRX),
                   panels.timeSeries.vmInternetNetworkTX('网络发送', queries.vmInternetNetworkTX),
                 ]),
               row.new('错误')
               + row.withPanels([
                   panels.timeSeries.vmProcessCount('进程数', queries.vmProcessCount),
                   panels.timeSeries.vmTcpConn('连接数', queries.vmTcpConn),
                 ]),
               ], panelWidth=8)
             )
    }
}