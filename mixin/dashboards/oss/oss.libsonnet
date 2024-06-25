local g = import '../g.libsonnet';
local row = g.panel.row;
local panels = import './panels.libsonnet';
local variables = import './variables.libsonnet';
local queries = import './queries.libsonnet';

{
    grafanaDashboards+:: {
        'cms/oss-summary.json':
           g.dashboard.new('oss Bucket监控')
           + g.dashboard.withDescription(|||
              oss Bucket监控
           |||)
           + g.dashboard.graphTooltip.withSharedCrosshair()
           + g.dashboard.withVariables([
             variables.datasource,
             variables.buckets,
           ])
           + g.dashboard.withPanels(
             g.util.grid.makeGrid([
               row.new('存储')
               + row.withPanels([
                   panels.timeSeries.storage('已使用存储', queries.storage),
                  ]),
               row.new('流量')
               + row.withPanels([
                   panels.timeSeries.recv_size('公网流入流量(byte)', queries.recv_size),
                   panels.timeSeries.send_size('公网流出流量(byte)', queries.send_size),
                   panels.timeSeries.intran_recv_size('内网流入流量(byte)', queries.intran_recv_size),
                   panels.timeSeries.intran_send_size('内网流出流量(byte)', queries.intran_send_size),
                 ]),
               row.new('成功请求')
               + row.withPanels([
                    panels.timeSeries.success_203('203返回数', queries.success_203),
                    panels.timeSeries.success_2xx('2xx返回数', queries.success_2xx),
                    panels.timeSeries.success_3xx('3xx返回数', queries.success_3xx),
                 ]),
               row.new('错误请求')
               + row.withPanels([
                   panels.timeSeries.error_403('403返回量', queries.error_403),
                   panels.timeSeries.error_404('404返回量', queries.error_404),
                   panels.timeSeries.error_408('408返回量', queries.error_408),
                   panels.timeSeries.error_499('499返回量', queries.error_499),
                   panels.timeSeries.error_4xx('4xx返回量', queries.error_4xx),
                   panels.timeSeries.error_503('503返回量', queries.error_503),
                   panels.timeSeries.error_5xx('5xx返回量', queries.error_5xx),
                 ]),
               ], panelWidth=8)
             )
    }
}