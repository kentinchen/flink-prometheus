local g = import '../g.libsonnet';
local row = g.panel.row;
local panels = import './panels.libsonnet';
local variables = import './variables.libsonnet';
local queries = import './queries.libsonnet';

{
    grafanaDashboards+:: {
        'cms/slb-summary.json':
           g.dashboard.new('slb监控')
           + g.dashboard.withDescription(|||
              SLB监控
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
                   panels.timeSeries.connsPS('TCP新建连接数', queries.connsPS),
                   // panels.timeSeries.count('统计', queries.count),
                   panels.timeSeries.dropConnPS('丢失连接数', queries.dropConnPS),
                   panels.timeSeries.failConnPS('建连失败数', queries.failConnPS),
                   panels.timeSeries.inActConnPS('TCP非活跃连接数', queries.inActConnPS),
                   panels.timeSeries.inBitsPS('流入带宽(bit)', queries.inBitsPS),
                   panels.timeSeries.inDBitesPS('监听丢失入bit数', queries.inDBitesPS),
                   panels.timeSeries.inDPktsPS('丢失入包数(Count)', queries.inDPktsPS),
                   panels.timeSeries.inPktsPS('流入数据包数(Count)', queries.inPktsPS),
                   panels.timeSeries.maxConnsPs('最大并发连接数', queries.maxConnsPs),
                   panels.timeSeries.outBitsPS('流出带宽(bit)', queries.outBitsPS),
                   panels.timeSeries.outDBitesPS('监听丢失出bit数', queries.outDBitesPS),
                   panels.timeSeries.outDPktsPS('丢失出包数(Count)', queries.outDPktsPS),
                   panels.timeSeries.outPktsPS('流出数据包数(Count)', queries.outPktsPS),
                   panels.timeSeries.sumInBytes('流入带宽总和', queries.sumInBytes),
                   panels.timeSeries.sumOutBytes('流出带宽总和', queries.sumOutBytes),
                 ]),
               ], panelWidth=8)
             )
    }
}