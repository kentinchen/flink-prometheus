local g = import '../g.libsonnet';
local base = import '../base/panels.libsonnet';

{
  timeSeries: base.timeSeries +{
      cpuUsage: self.long,
      memUsage: self.long,
      diskUsage: self.long,
      diskInodeUsage: self.long,
      vmProcessCount: self.short,
      vmLoadAverage: self.long,
      vmLoadAverage5: self.long,
      vmDiskIORead: self.short,
      vmDiskIOWrite: self.short,
      vmInternetNetworkRX: self.short,
      vmInternetNetworkTX: self.short,
      vmTcpConn: self.short,
  }
}
